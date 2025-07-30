package com.k2fsa.sherpa.onnx;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.unity3d.player.UnityPlayer;

public class SherpaOnnxTTSPlugin {
    public static final String TAG = "sherpa-onnx-tts-plugin (Java)";
    private OfflineTts tts = null;
    private boolean stopped = false;
    private MediaPlayer mediaPlayer = null;
    private AudioTrack track = null;
    private Activity activity = null;
    private String unityGameObjectName = "";
    private String unityMethodName = "";
    private boolean debugMode = false;

    public SherpaOnnxTTSPlugin() {
        Log.i(TAG, "calling SherpaOnnxTTSPlugin - constructor");
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void initialize() {
        if (debugMode) {
            Log.i(TAG, "Start to initialize TTS");
        }
        initTts();
        if (debugMode) {
            Log.i(TAG, "Finish initializing TTS");
        }

        if (debugMode) {
            Log.i(TAG, "Start to initialize AudioTrack");
        }
        initAudioTrack();
        if (debugMode) {
            Log.i(TAG, "Finish initializing AudioTrack");
        }

        // TODO: This method should return a value or throw an error
        if (activity == null) {
            Log.e(TAG, "Activity object is null!");
            return;
        }
        activity.runOnUiThread(() -> {
            if (debugMode) {
                Log.i(TAG, "Sherpa ONNX TTS library initialized successfully.");
            }
        });
    }

    public void setOnCompleteCallback(String gameObjectName, String methodName) {
        this.unityGameObjectName = gameObjectName;
        this.unityMethodName = methodName;
    }

    public void generateAndPlay(int speakerId, float speed, String text) {
        if (speakerId < 0) {
            Log.w(TAG, "Please input a non-negative integer for speaker ID!");
            return;
        }

        if (speed <= 0) {
            Log.w(TAG, "Please input a positive number for speech speed!");
            return;
        }

        String textStr = text.trim();
        if (textStr.isEmpty()) {
            Log.w(TAG, "Please input a non-empty text!");
            return;
        }

        if (track == null) {
            Log.w(TAG, "Track object (track) is null!");
            return;
        }

        track.pause();
        track.flush();
        track.play();

        stopped = false;

        new Thread(() -> {
            GeneratedAudio audio = tts.generateWithCallback(
                    textStr,
                    speakerId,
                    speed,
                    this::callback
            );

            String filename = activity.getFilesDir().getAbsolutePath() + "/generated.wav";
            boolean ok = audio.getSamples().length > 0 && audio.save(filename);
            if (ok) {
                activity.runOnUiThread(() -> {
                    track.stop();
                    if (debugMode) {
                        Log.i(TAG, "Trying to send a message to unity");
                    }
                    sendResultToUnity("stop");
                });
            }
        }).start();
    }

    public void play() {
        String filename = this.activity.getFilesDir().getAbsolutePath() + "/generated.wav";
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(
                activity,
                Uri.fromFile(new File(filename))
        );
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        // TODO: this method should return a value
    }

    public void stop() {
        stopped = true;

        track.pause();
        track.flush();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // TODO: this method should return a value
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isPlaying() {
        boolean isTrackPlaying = track != null && track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
        boolean isMediaPlayerPlaying = false;
        if (mediaPlayer != null) {
            try {
                isMediaPlayerPlaying = mediaPlayer.isPlaying();
            } catch (IllegalStateException e) {
                Log.w(TAG, "MediaPlayer in illegal state while checking isPlaying().");
                isMediaPlayerPlaying = false;
            }
        }
        return isTrackPlaying || isMediaPlayerPlaying;
    }

    /**
     * Returns the duration of the audio file in milliseconds.
     * @return duration in milliseconds
     */
    public int getDuration() {
        String filename = this.activity.getFilesDir().getAbsolutePath() + "/generated.wav";
        File audioFile = new File(filename);

        if (!audioFile.exists()) {
            if (debugMode) {
                Log.w(TAG, "Cannot get duration. File not found: " + filename);
            }
            return 0;
        }

        MediaPlayer tempPlayer = new MediaPlayer();
        int duration = 0;
        try {
            tempPlayer.setDataSource(filename);
            tempPlayer.prepare();
            duration = tempPlayer.getDuration();
        } catch (IOException | IllegalStateException e) {
            Log.e(TAG, "Failed to get duration from audio file.", e);
            return 0;
        } finally {
            tempPlayer.release();
        }

        return duration;
    }

    public void release() {
        if (debugMode) {
            Log.i(TAG, "Releasing allocated memory");
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (track != null) {
            track.release();
            track = null;
        }
        if (tts != null) {
            tts.release();
            tts = null;
        }
    }

    private void sendResultToUnity(String message) {
        if (unityGameObjectName != null && !unityGameObjectName.isEmpty() && unityMethodName != null && !unityMethodName.isEmpty()) {
            if (debugMode) {
                Log.i(TAG, "Sending stop message to unity");
            }
            UnityPlayer.UnitySendMessage(unityGameObjectName, unityMethodName, message);
        } else {
            Log.e(TAG, "Unity callback not set. Result cannot be sent.");
        }
    }

    private void initTts() {
        String acousticModelName = "";
        String vocoder = "";
        String voices = "";
        String ruleFars = "";

        String modelDir = "vits-piper-pl_PL-darkman-medium";
        String modelName = "pl_PL-darkman-medium.onnx";
        String dataDir = modelDir + "/espeak-ng-data";

        String newDataDir = copyDataDir(dataDir);
        dataDir = newDataDir + "/" + dataDir;

        OfflineTtsConfig config = TtsConfigUtil.getOfflineTtsConfig(
                modelDir,
                modelName,
                acousticModelName,
                vocoder,
                voices,
                "",
                dataDir,
                "",
                "",
                ruleFars
        );

        tts = new OfflineTts(this.activity.getAssets(), config);

        Log.i(TAG, "Number of speakers: " + tts.getNumSpeakers());
    }

    private void initAudioTrack() {
        int sampleRate = tts.getSampleRate();
        int bufLength = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_FLOAT
        );
        if (debugMode) {
            Log.i(TAG, "sampleRate: " + sampleRate + ", buffLength: " + bufLength);
        }

        AudioAttributes attr = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        AudioFormat format = new AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(sampleRate)
                .build();

        track = new AudioTrack(
                attr, format, bufLength, AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
        );
        track.play();
    }

    // this function is called from C++
    private int callback(float[] samples) {
        if (!stopped) {
            track.write(samples, 0, samples.length, AudioTrack.WRITE_BLOCKING);
            return 1;
        } else {
            track.stop();
            return 0;
        }
    }

    private String copyDataDir(String dataDir) {
        if (debugMode) {
            Log.i(TAG, "data dir is " + dataDir);
        }
        copyAssets(dataDir);

        File f = this.activity.getExternalFilesDir(null);
        if (f == null) {
            Log.w(TAG, "Cannot get external files directory!");
            return "";
        }
        String newDataDir = f.getAbsolutePath();
        if (debugMode) {
            Log.i(TAG, "New data directory: " + newDataDir);
        }

        return newDataDir;
    }

    private void copyAssets(String path) {
        String[] assets;
        try {
            assets = activity.getAssets().list(path);
            if (assets == null || assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = activity.getExternalFilesDir(null) + "/" + path;
                File dir = new File(fullPath);
                boolean makeDirs = dir.mkdirs();
                for (String asset : assets) {
                    String p = path.isEmpty() ? "" : path + "/";
                    copyAssets(p + asset);
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "Failed to copy " + path + ". " + ex);
        }
    }

    private void copyFile(String filename) {
        File file = activity.getExternalFilesDir(null);
        if (file == null) {
            Log.e(TAG, "Failed to get a main content directory");
            return;
        }
        try (InputStream istream = activity.getAssets().open(filename);
             OutputStream ostream = new FileOutputStream(file + "/" + filename)) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = istream.read(buffer)) != -1) {
                ostream.write(buffer, 0, read);
            }
            ostream.flush();
        } catch (Exception ex) {
            Log.e(TAG, "Failed to copy " + filename + ", " + ex);
        }
    }
}
