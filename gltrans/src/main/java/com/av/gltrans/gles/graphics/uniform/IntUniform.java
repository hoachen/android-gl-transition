package com.av.gltrans.gles.graphics.uniform;

import android.opengl.GLES20;

public class IntUniform extends Uniform<Integer> {

    public IntUniform(String name) {
        super(name);
    }

    public static IntUniform uniform1i(String name, boolean isOptional) {
        IntUniform uniform = new IntUniform(name);
        uniform.isOptional = isOptional;
        return uniform;
    }

    @Override
    public void setValue(Integer value) {
        rationalChecks();
        GLES20.glUniform1i(getLocation(), value);
    }

    @Override
    public Integer getValue() {
        rationalChecks();
        int args[] = new int[1];
        GLES20.glGetUniformiv(program, getLocation(), args, 0);
        return args[0];
    }
}
