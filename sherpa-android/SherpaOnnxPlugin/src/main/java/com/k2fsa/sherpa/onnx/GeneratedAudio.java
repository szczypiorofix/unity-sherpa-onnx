package com.k2fsa.sherpa.onnx;

public class GeneratedAudio {
    private final float[] samples;
    private final int sampleRate;

    public GeneratedAudio(float[] samples, int sampleRate) {
        this.samples = samples;
        this.sampleRate = sampleRate;
    }

    public float[] getSamples() {
        return samples;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public boolean save(String filename) {
        return saveImpl(filename, samples, sampleRate);
    }

    private native boolean saveImpl(String filename, float[] samples, int sampleRate);
}
