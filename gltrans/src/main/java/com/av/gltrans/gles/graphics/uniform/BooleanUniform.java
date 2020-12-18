package com.av.gltrans.gles.graphics.uniform;

import android.opengl.GLES20;

public class BooleanUniform extends Uniform<Boolean> {

    public BooleanUniform(String name) {
        super(name);
    }



    @Override
    public void setValue(Boolean value) {
        rationalChecks();
        GLES20.glUniform1i(
                getLocation(), value ? GLES20.GL_TRUE : GLES20.GL_FALSE);
    }

    @Override
    public Boolean getValue() {
        rationalChecks();
        int[] args = new int[1];
        GLES20.glGetUniformiv(program, getLocation(), args, 0);
        return args[0] == GLES20.GL_TRUE;
    }
}
