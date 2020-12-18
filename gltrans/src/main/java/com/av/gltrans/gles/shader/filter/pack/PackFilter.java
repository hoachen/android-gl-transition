package com.av.gltrans.gles.shader.filter.pack;


import androidx.annotation.FloatRange;

public class PackFilter {

    @FloatRange(from = 0.0, to = 1.0)
    float intensity = 1f;

    @FloatRange(from = -1.0, to = 1.0)
    float brightness = 0.0f;

    // That might not be true
    @FloatRange(from = 0.0, to = 2.0)
    float contrast = 1f;

    @FloatRange(from = 0.0, to = 2.0)
    float saturation =  1f;

    @FloatRange(from = -0.5, to = 0.5)
    float warmth = 0f;

    @FloatRange(from = 0.0, to = 1.0)
    float tint = 0f;

    @FloatRange(from = 0.0, to = 2.0)
    float gamma = 1f;

    @FloatRange(from = 0.0, to = 1.0)
    float vibrant = 0f;

    @FloatRange(from = 0.0, to = 1.0)
    float sepia = 0f;
}
