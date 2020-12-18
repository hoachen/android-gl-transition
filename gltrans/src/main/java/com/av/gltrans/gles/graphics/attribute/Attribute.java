package com.av.gltrans.gles.graphics.attribute;

import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.InputValue;

public abstract class Attribute<T> extends InputValue<T> {

    public Attribute(String name) {
        super(name);
    }

    @Override
    protected int loadLocation() {
        return GLES20.glGetAttribLocation(program, name);
    }

    @Override
    protected void rationalChecks() {
        if (program == -1) throw new RuntimeException("Invalid program");
        if (_location == -1) throw new RuntimeException("Attribute name: " + name + " is not found!");
    }

}
