package com.k2fsa.sherpa.onnx;

import android.content.res.AssetManager;

public class OfflineRecognizer implements AutoCloseable {
    static {
        System.loadLibrary("sherpa-onnx-jni");
    }

    private long ptr;

    public OfflineRecognizer(AssetManager assetManager, OfflineRecognizerConfig config) {
        this.ptr = newFromAsset(assetManager, config);
    }

    public OfflineRecognizer(OfflineRecognizerConfig config) {
        this.ptr = newFromFile(config);
    }

    public void release() {
        if (ptr != 0L) {
            delete(ptr);
            ptr = 0;
        }
    }

    @Override
    public void close() {
        release();
    }

    public OfflineStream createStream() {
        long streamPtr = createStream(this.ptr);
        return new OfflineStream(streamPtr);
    }

    public void decode(OfflineStream stream) {
        decode(this.ptr, stream.ptr);
    }

    @SuppressWarnings("unchecked")
    public OfflineRecognizerResult getResult(OfflineStream stream) {
        Object[] objArray = getResult(stream.ptr);

        String text = (String) objArray[0];
        Object[] tokenObjects = (Object[]) objArray[1];
        String[] tokens = new String[tokenObjects.length];
        for (int i = 0; i < tokenObjects.length; i++) {
            tokens[i] = (String) tokenObjects[i];
        }

        float[] timestamps = (float[]) objArray[2];
        String lang = (String) objArray[3];
        String emotion = (String) objArray[4];
        String event = (String) objArray[5];

        return new OfflineRecognizerResult(text, tokens, timestamps, lang, emotion, event);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    private native long newFromAsset(AssetManager assetManager, OfflineRecognizerConfig config);
    private native long newFromFile(OfflineRecognizerConfig config);
    private native void delete(long ptr);
    private native long createStream(long ptr);
    private native void decode(long ptr, long streamPtr);
    private native Object[] getResult(long streamPtr);
}
