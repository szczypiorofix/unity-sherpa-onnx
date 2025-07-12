package com.k2fsa.sherpa.onnx;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.unity3d.player.UnityPlayer;

public class SherpaOnnxSTTPlugin {
    public static final String TAG = "sherpa-onnx-stt-plugin (Java)";
    private final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private Activity activity = null;
    private final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private final String[] permissions = { Manifest.permission.RECORD_AUDIO };
    private AudioRecord audioRecord = null;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Thread recordingThread = null;
    private final int sampleRateInHz = 16000;
    private final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private volatile boolean mIsRecording = false;
    private String lastText = "";
    private OfflineRecognizer offlineRecognizer;
    private String unityGameObjectName;
    private String unityMethodName;
    private String unityErrorMethodName;
    private boolean debugMode = false;

    public SherpaOnnxSTTPlugin() {}

    public void setActivity(Activity activity) {
        this.activity = activity;
        if (activity == null) {
            Log.e(TAG, "An error occurred! Activity object is null !!!");
        }
    }

    public void initialize() {
        if (debugMode) {
            Log.i(TAG, "Start to initialize STT");
            Log.i(TAG, "Checking audio record permissions...");
        }
        activity.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        if (debugMode) {
            Log.i(TAG, "Start to initialize non-streaming recognizer");
        }
        initOfflineRecognizer();

        if (debugMode) {
            Log.i(TAG, "Finished initializing non-streaming recognizer");
        }
    }

    public void setUnityCallback(String gameObjectName, String methodName, String errorMethodName) {
        this.unityGameObjectName = gameObjectName;
        this.unityMethodName = methodName;
        this.unityErrorMethodName = errorMethodName;
    }

    private void sendErrorToUnity(String errorMessage) {
        if (unityGameObjectName != null && !unityGameObjectName.isEmpty() && unityErrorMethodName != null && !unityErrorMethodName.isEmpty()) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, unityErrorMethodName, errorMessage);
        } else {
            Log.e(TAG, "Unity error callback not set. Error cannot be sent.");
        }
    }

    public void startRecording() {
        if (mIsRecording) {
            Log.w(TAG, "Audio is recording already");
            return;
        }

        boolean microphoneInitialized = initMicrophone();
        if (!microphoneInitialized) {
            Log.e(TAG, "Failed to initialize microphone");
            return;
        }
        if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            sendErrorToUnity("Brak uprawnień do nagrywania.");
            return;
        }
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                minBufferSize * 2
        );

        mIsRecording = true;
        audioRecord.startRecording();

        recordingThread = new Thread(() -> {
            processAudioStream(minBufferSize);
        });
        recordingThread.start();
    }

    public void stopRecording() {
        if (debugMode) {
            Log.i(TAG, "Recording stopped");
        }

        if (!mIsRecording) {
            return;
        }

        mIsRecording = false;
        try {
            if (recordingThread != null) {
                recordingThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e(TAG, "Recording thread interrupted", e);
        }

        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }

        if (debugMode) {
            Log.i(TAG, "Stopped recording.");
        }
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public void setRecording(boolean isRecording) {
        mIsRecording = isRecording;
    }

    public String getLastText() {
        return lastText;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void release() {
        if (debugMode) {
            Log.i(TAG, "Releasing allocated memory");
        }
        if (offlineRecognizer != null) {
            offlineRecognizer.release();
            offlineRecognizer = null;
        }
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
    }

    private void processAudioStream(int minBufferSize) {
        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        short[] audioData = new short[minBufferSize / 2];

        while (mIsRecording) {
            int bytesRead = audioRecord.read(audioData, 0, audioData.length);
            if (bytesRead > 0) {
                try {
                    byte[] byteBuffer = new byte[bytesRead * 2];
                    ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(audioData, 0, bytesRead);
                    bufferStream.write(byteBuffer);
                } catch (IOException e) {
                    Log.e(TAG, "Error writing to buffer stream", e);
                }
            }
        }

        decodeWholeBuffer(bufferStream.toByteArray());
    }

    private void decodeWholeBuffer(byte[] wholeAudioData) {
        if (wholeAudioData.length == 0) {
            sendErrorToUnity("Bufor audio jest pusty.");
            return;
        }

        executor.execute(() -> {
            try {
                short[] shortSamples = new short[wholeAudioData.length / 2];
                ByteBuffer.wrap(wholeAudioData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortSamples);

                float[] floatSamples = new float[shortSamples.length];
                for (int i = 0; i < shortSamples.length; i++) {
                    floatSamples[i] = shortSamples[i] / 32768.0f;
                }

                if (debugMode) {
                    Log.i(TAG, "Decoding " + (floatSamples.length / (float) sampleRateInHz) + " seconds of audio.");
                }

                OfflineStream stream = offlineRecognizer.createStream();
                stream.acceptWaveform(floatSamples, sampleRateInHz);
                offlineRecognizer.decode(stream);
                lastText = offlineRecognizer.getResult(stream).text;
                stream.release();

                sendResultToUnity(lastText);
            } catch (Exception e) {
                Log.e(TAG, "Failed to decode audio", e);
                sendErrorToUnity("Błąd podczas dekodowania: " + e.getMessage());
            }
        });
    }

    private void sendResultToUnity(String message) {
        if (unityGameObjectName != null && !unityGameObjectName.isEmpty() && unityMethodName != null && !unityMethodName.isEmpty()) {
            UnityPlayer.UnitySendMessage(unityGameObjectName, unityMethodName, message);
        } else {
            Log.e(TAG, "Unity callback not set. Result cannot be sent.");
        }
    }

    private void initOfflineRecognizer() {
        String modelDir = "sherpa-onnx-nemo-fast-conformer-ctc-be-de-en-es-fr-hr-it-pl-ru-uk-20k";
        OfflineModelConfig modelConfig = new OfflineModelConfig();
        modelConfig.nemo.model = modelDir + "/model.onnx";
        modelConfig.tokens = modelDir + "/tokens.txt";

        OfflineRecognizerConfig config = new OfflineRecognizerConfig(
                FeatureConfig.getFeatureConfig(sampleRateInHz, 80),
                modelConfig
        );

        offlineRecognizer = new OfflineRecognizer(activity.getAssets(), config);
    }

    private boolean initMicrophone() {
        if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            return false;
        }

        int numBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

        if (debugMode) {
            Log.i(TAG, "buffer size in milliseconds: " + (numBytes * 1000.0f / sampleRateInHz));
        }

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                numBytes * 2 // a sample has two bytes as we are using 16-bit PCM
        );
        return true;
    }
}
