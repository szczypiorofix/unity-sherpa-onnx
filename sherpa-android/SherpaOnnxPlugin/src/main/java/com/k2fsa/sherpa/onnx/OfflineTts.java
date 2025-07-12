package com.k2fsa.sherpa.onnx;

import android.content.res.AssetManager;
import android.util.Log;

public class OfflineTts {
    static {
        System.loadLibrary("sherpa-onnx-jni");
    }

    private long ptr;
    private final OfflineTtsConfig config;

    public OfflineTts(AssetManager assetManager, OfflineTtsConfig config) {
        this.config = config;
        if (assetManager != null) {
            this.ptr = newFromAsset(assetManager, config);
        } else {
            this.ptr = newFromFile(config);
        }
    }

    public int getSampleRate() {
        return getSampleRate(ptr);
    }

    public int getNumSpeakers() {
        return getNumSpeakers(ptr);
    }

    public GeneratedAudio generateWithCallback(String text, int sid, float speed, AudioCallback callback) {
        Object[] objArray = generateWithCallbackImpl(ptr, text, sid, speed, callback);
        return new GeneratedAudio((float[]) objArray[0], (int) objArray[1]);
    }

    public void allocate(AssetManager assetManager) {
        if (ptr == 0L) {
            if (assetManager != null) {
                ptr = newFromAsset(assetManager, config);
            } else {
                ptr = newFromFile(config);
            }
        }
    }

    public void free() {
        if (ptr != 0L) {
            delete(ptr);
            ptr = 0;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (ptr != 0L) {
            delete(ptr);
            ptr = 0;
        }
        super.finalize();
    }

    public void release() {
        try {
            finalize();
        } catch (Throwable t) {
            Log.e(SherpaOnnxTTSPlugin.TAG, "An error occurred while releasing memory: " + t.getMessage());
        }
    }

    private native long newFromAsset(AssetManager assetManager, OfflineTtsConfig config);
    private native long newFromFile(OfflineTtsConfig config);
    private native void delete(long ptr);
    private native int getSampleRate(long ptr);
    private native int getNumSpeakers(long ptr);

    // The returned array has two entries:
    // - the first entry is an 1-D float array containing audio samples.
    // Each sample is normalized to the range [-1, 1]
    // - the second entry is the sample rate
    private native Object[] generateImpl(long ptr, String text, int sid, float speed);
    private native Object[] generateWithCallbackImpl(long ptr, String text, int sid, float speed, AudioCallback callback);
}
