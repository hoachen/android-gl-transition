package com.av.gltrans.gles.graphics.uniform;

import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.InputValue;

public abstract class Uniform<T> extends InputValue<T> {

    protected boolean isOptional = false;

    public Uniform(String name) {
        super(name);
    }

    @Override
    protected int loadLocation() {
        return GLES20.glGetUniformLocation(program, name);
    }

    public void rationalChecks() {
        if (program == -1) throw new RuntimeException("Invalid program");
        if (_location == -1 && !isOptional)
            throw new RuntimeException("Uniform name: $name is not found! Did you Initialize it yet?");
    }
}
