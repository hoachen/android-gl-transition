package com.av.gltrans.gles.graphics;

public class Vector2f {

    public float x = 0.0f;
    public float y = 0.0f;

    public Vector2f() {
        this(0.0f, 0.0f);
    }


    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f vec2(float x, float y) {
        return new Vector2f(x, y);
    }


    public static Vector2f vec2(float x) {
        return new Vector2f(x, x);
    }

    public static Vector2f vec2() {
        return new Vector2f();
    }

    public static Vector2f fromFloatArray(float[] values) {
        if (values == null || values.length == 0) {
            return vec2();
        } else if (values.length == 1) {
            return vec2(values[0]);
        }
        return new Vector2f(values[0], values[1]);
    }


}
