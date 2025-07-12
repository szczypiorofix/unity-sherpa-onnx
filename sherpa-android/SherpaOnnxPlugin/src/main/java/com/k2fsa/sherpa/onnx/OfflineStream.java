package com.k2fsa.sherpa.onnx;

import java.util.function.Consumer;

public class OfflineStream implements AutoCloseable {
    public long ptr;

    static {
        System.loadLibrary("sherpa-onnx-jni");
    }

    public OfflineStream(long ptr) {
        this.ptr = ptr;
    }

    public void acceptWaveform(float[] samples, int sampleRate) {
        acceptWaveform(this.ptr, samples, sampleRate);
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

    public void use(Consumer<OfflineStream> block) {
        try {
            block.accept(this);
        } finally {
            release();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private native void acceptWaveform(long ptr, float[] samples, int sampleRate);
    private native void delete(long ptr);
}
