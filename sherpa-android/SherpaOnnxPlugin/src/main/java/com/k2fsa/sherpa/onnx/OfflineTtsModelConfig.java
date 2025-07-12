package com.k2fsa.sherpa.onnx;

public class OfflineTtsModelConfig {
    public OfflineTtsVitsModelConfig vits = new OfflineTtsVitsModelConfig();
    public OfflineTtsMatchaModelConfig matcha = new OfflineTtsMatchaModelConfig();
    public OfflineTtsKokoroModelConfig kokoro = new OfflineTtsKokoroModelConfig();
    public int numThreads = 1;
    public boolean debug = false;
    public String provider = "cpu";

    public OfflineTtsModelConfig() {
    }
}
