package com.k2fsa.sherpa.onnx;

public class FeatureConfig {
    public int sampleRate = 16000;
    public int featureDim = 80;
    public float dither = 0.0f;

    public FeatureConfig() {}

    public FeatureConfig(int sampleRate, int featureDim) {
        this.sampleRate = sampleRate;
        this.featureDim = featureDim;
    }

    public static FeatureConfig getFeatureConfig(int sampleRate, int featureDim) {
        return new FeatureConfig(sampleRate, featureDim);
    }
}
