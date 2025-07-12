package com.k2fsa.sherpa.onnx;

class SileroVadModelConfig {
    public String model = "";
    public float threshold = 0.5F;
    public float minSilenceDuration = 0.25F;
    public float minSpeechDuration = 0.25F;
    public int windowSize = 512;
    public float maxSpeechDuration = 5.0F;

    public SileroVadModelConfig() {}
}
