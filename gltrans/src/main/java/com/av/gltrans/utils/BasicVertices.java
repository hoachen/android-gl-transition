package com.av.gltrans.utils;

public class BasicVertices {

    public static float[] RECT_STRIP = {
            -1.0f, -1.0f, 0f,
            1.0f, -1.0f, 0f,
            -1.0f, 1.0f, 0f,
            1.0f, 1.0f, 0f
    };

    public static float[] RECT_STRIP_TEXTURE = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    public static float[] FULL_RECTANGLE = {
            -1f, -1f, 0f,
            -1f, 1f, 0f,
            1f, 1f, 0f,

            -1f, -1f, 0f,
            1f, 1f, 0f,
            1f, -1f, 0f
    };

    public static float[] NORMAL_TEXTURE_COORDINATES = {
            0f, 0f,
            0f, 1f,
            1f, 1f,

            0f, 0f,
            1f, 1f,
            1f, 0f
    };
}
