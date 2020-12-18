package com.av.gltrans.gles.graphics;

import android.opengl.Matrix;

public class Matrix4f {

    public float[] elements = new float[16];

    public Matrix4f() {
    }

    public static Matrix4f mat4() {
        return new Matrix4f();
    }

    public Matrix4f(float[] elements) {
        this.elements = elements;
        Matrix.setIdentityM(elements, 0);
    }

    public float get(int index) {
        return elements[index];
    }

    public void set(int index, float value) {
        elements[index] = value;
    }

    public Matrix4f times(Matrix4f mat4) {
        Matrix4f result = new Matrix4f();
        Matrix.multiplyMM(result.elements, 0, elements,
                0, mat4.elements, 0);
        return result;
    }


    public boolean equals(Matrix4f other) {
        if (this == other)
            return true;
        if (this.getClass() != other.getClass())
            return false;
        if (!elements.equals(other.elements))
            return false;

        return true;
    }

    public int hashCode() {
        return elements.hashCode();
    }

}


