package com.av.gltrans.gles.graphics.uniform;


import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.Vector2f;

public class Float2Uniform extends Uniform<Vector2f> {

    public Float2Uniform(String name) {
        super(name);
    }

    public static Float2Uniform uniform2f(String name, boolean isOptional) {
        Float2Uniform float2Uniform = new Float2Uniform(name);
        float2Uniform.isOptional = isOptional;
        return float2Uniform;
    }

    @Override
    public void setValue(Vector2f value) {
        rationalChecks();
        GLES20.glUniform2f(getLocation(), value.x, value.y);
        cachedValue = value;
    }

    @Override
    public Vector2f getValue() {
        rationalChecks();
        float[] args = new float[2];
        GLES20.glGetUniformfv(program, getLocation(), args, 0);
        return Vector2f.fromFloatArray(args);
    }

}
