package com.k2fsa.sherpa.onnx;

public class SpeechSegment {
    public final int start;
    public final float[] samples;

    public SpeechSegment(int start, float[] samples) {
        this.start = start;
        this.samples = samples;
    }
}
