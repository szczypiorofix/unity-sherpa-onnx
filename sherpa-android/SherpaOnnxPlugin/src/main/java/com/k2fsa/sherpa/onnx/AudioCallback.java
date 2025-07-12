package com.k2fsa.sherpa.onnx;

@FunctionalInterface
public interface AudioCallback {
    Integer invoke(float[] samples);
}
