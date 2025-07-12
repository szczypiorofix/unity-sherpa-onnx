package com.k2fsa.sherpa.onnx;

import android.content.res.AssetManager;

public class Vad implements AutoCloseable {
    static {
        System.loadLibrary("sherpa-onnx-jni");
    }

    private long ptr;
    public VadModelConfig config;

    public Vad(AssetManager assetManager, VadModelConfig config) {
        this.config = config;
        this.ptr = newFromAsset(assetManager, config);
    }

    public Vad(VadModelConfig config) {
        this.config = config;
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

    public void acceptWaveform(float[] samples) {
        acceptWaveform(ptr, samples);
    }

    public boolean empty() {
        return empty(ptr);
    }

    public void pop() {
        pop(ptr);
    }

    public SpeechSegment front() {
        Object[] segmentData = front(ptr);
        int start = (int) segmentData[0];
        float[] samples = (float[]) segmentData[1];
        return new SpeechSegment(start, samples);
    }

    public void clear() {
        clear(ptr);
    }

    public boolean isSpeechDetected() {
        return isSpeechDetected(ptr);
    }

    public void reset() {
        reset(ptr);
    }

    public void flush() {
        flush(ptr);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    private native long newFromAsset(AssetManager assetManager, VadModelConfig config);
    private native long newFromFile(VadModelConfig config);
    private native void delete(long ptr);
    private native void acceptWaveform(long ptr, float[] samples);
    private native boolean empty(long ptr);
    private native void pop(long ptr);
    private native void clear(long ptr);
    private native Object[] front(long ptr);
    private native boolean isSpeechDetected(long ptr);
    private native void reset(long ptr);
    private native void flush(long ptr);
}
