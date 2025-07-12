package com.k2fsa.sherpa.onnx;

public class OfflineRecognizerResult {
    public String text;
    public String[] tokens;
    public float[] timestamps;
    public String lang;
    public String emotion;
    public String event;

    public OfflineRecognizerResult(String text, String[] tokens, float[] timestamps, String lang, String emotion, String event) {
        this.text = text;
        this.tokens = tokens;
        this.timestamps = timestamps;
        this.lang = lang;
        this.emotion = emotion;
        this.event = event;
    }
}
