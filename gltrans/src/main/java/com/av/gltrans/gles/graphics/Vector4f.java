package com.av.gltrans.gles.graphics;

public class Vector4f {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public float w = 0.0f;


    public Vector4f() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vector4f vec4(float x, float y, float z, float w) {
        return new Vector4f(x, y, z, w);
    }

    public static Vector4f vec3(Vector2f vec2, float z, float w) {
        return new Vector4f(vec2.x, vec2.y, z, w);
    }

    public static Vector4f vec3(Vector3f vec3, float w) {
        return new Vector4f(vec3.x, vec3.y, vec3.z, w);
    }

    public static Vector4f vec4(float x) {
        return new Vector4f(x, x, x, x);
    }

    public static Vector4f vec4() {
        return new Vector4f();
    }

    public static Vector4f fromFloatArray(float[] values) {
        return new Vector4f(values[0], values[1], values[2], values[3]);
    }


}
