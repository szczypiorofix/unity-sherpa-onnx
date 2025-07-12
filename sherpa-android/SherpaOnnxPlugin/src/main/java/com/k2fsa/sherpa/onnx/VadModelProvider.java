package com.k2fsa.sherpa.onnx;

public final class VadModelProvider {
    private VadModelProvider() {}

    public static VadModelConfig getVadModelConfig(int type) {
        if (type == 0) {
            VadModelConfig config = new VadModelConfig();
            config.sampleRate = 16000;
            config.numThreads = 1;
            config.provider = "cpu";

            config.sileroVadModelConfig.model = "silero_vad.onnx";
            config.sileroVadModelConfig.threshold = 0.5F;
            config.sileroVadModelConfig.minSilenceDuration = 0.25F;
            config.sileroVadModelConfig.minSpeechDuration = 0.25F;
            config.sileroVadModelConfig.windowSize = 512;

            return config;
        }
        return null;
    }
}
