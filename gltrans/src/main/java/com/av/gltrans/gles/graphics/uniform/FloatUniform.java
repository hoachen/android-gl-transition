package com.av.gltrans.gles.graphics.uniform;

import android.opengl.GLES20;

public class FloatUniform extends Uniform<Float> {

    public FloatUniform(String name) {
        this(name, false);
    }

    public FloatUniform(String name, boolean isOptional) {
        super(name);
        this.isOptional = isOptional;
    }


    public static FloatUniform uniform1f(String name, boolean isOptional) {
        FloatUniform floatUniform = new FloatUniform(name);
        floatUniform.isOptional = isOptional;
        return floatUniform;
    }


    @Override
    public void setValue(Float value) {
        rationalChecks();
        if (cachedValue != null && cachedValue.floatValue() == value.floatValue())
            return;
        GLES20.glUniform1f(getLocation(), value);
        cachedValue = value;
    }

    @Override
    public Float getValue() {
        rationalChecks();
        float[] args = new float[1];
        GLES20.glGetUniformfv(program, getLocation(), args, 0);
        return args[0];
    }
}
