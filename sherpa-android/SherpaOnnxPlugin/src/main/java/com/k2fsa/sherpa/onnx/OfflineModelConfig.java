package com.k2fsa.sherpa.onnx;

public class OfflineModelConfig {
    public OfflineTransducerModelConfig transducer = new OfflineTransducerModelConfig();
    public OfflineParaformerModelConfig paraformer = new OfflineParaformerModelConfig();
    public OfflineWhisperModelConfig whisper = new OfflineWhisperModelConfig();
    public OfflineFireRedAsrModelConfig fireRedAsr = new OfflineFireRedAsrModelConfig();
    public OfflineMoonshineModelConfig moonshine = new OfflineMoonshineModelConfig();
    public OfflineNemoEncDecCtcModelConfig nemo= new OfflineNemoEncDecCtcModelConfig();
    public OfflineSenseVoiceModelConfig senseVoice = new OfflineSenseVoiceModelConfig();
    public OfflineDolphinModelConfig dolphin = new OfflineDolphinModelConfig();
    public OfflineZipformerCtcModelConfig zipformerCtc = new OfflineZipformerCtcModelConfig();
    public String teleSpeech = "";
    public int numThreads = 1;
    public boolean debug = false;
    public String provider = "cpu";
    public String modelType = "";
    public String tokens = "";
    public String modelingUnit = "";
    public String bpeVocab = "";

    public OfflineModelConfig() {}
}
