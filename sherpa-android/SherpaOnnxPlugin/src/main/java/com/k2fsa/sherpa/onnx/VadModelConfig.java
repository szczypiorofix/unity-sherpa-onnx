package com.k2fsa.sherpa.onnx;

public class VadModelConfig {
    public SileroVadModelConfig sileroVadModelConfig = new SileroVadModelConfig();
    public int sampleRate = 16000;
    public int numThreads = 1;
    public String provider = "cpu";
    public boolean debug = false;

    public VadModelConfig() {}
}
