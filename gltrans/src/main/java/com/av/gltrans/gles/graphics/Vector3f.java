package com.av.gltrans.gles.graphics;

public class Vector3f {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vector3f() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3f vec3(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    public static Vector3f vec3(Vector2f vec2, float z) {
        return new Vector3f(vec2.x, vec2.y, z);
    }

    public static Vector3f vec3(float x) {
        return new Vector3f(x, x, x);
    }

    public static Vector3f vec3() {
        return new Vector3f();
    }

    public static Vector3f fromFloatArray(float[] values) {
        return new Vector3f(values[0], values[1], values[2]);
    }


}
