package com.k2fsa.sherpa.onnx;

public class OfflineWhisperModelConfig {
    public String encoder = "";
    public String decoder = "";
    public String language = "en"; // Used with multilingual model
    public String task = "transcribe"; // transcribe or translate
    public int tailPaddings  = 1000; // Padding added at the end of the samples
}
