package com.av.gltrans.gles.graphics.uniform;

import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.Matrix4f;

public class Mat4Uniform extends Uniform<Matrix4f> {

    public Mat4Uniform(String name) {
        super(name);
    }

    public static Mat4Uniform uniformMat4(String name) {
        return new Mat4Uniform(name);
    }

    @Override
    public void setValue(Matrix4f value) {
        rationalChecks();
        GLES20.glUniformMatrix4fv(getLocation(), 1, false, value.elements, 0);
        cachedValue = value;
    }

    @Override
    public Matrix4f getValue() {
        rationalChecks();
        float[] args = new float[4 * 4];
        GLES20.glGetUniformfv(program, getLocation(), args, 0);
        return new Matrix4f(args);
    }
}
