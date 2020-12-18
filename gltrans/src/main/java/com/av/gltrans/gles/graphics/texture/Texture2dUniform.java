package com.av.gltrans.gles.graphics.texture;

import android.opengl.GLES20;

public class Texture2dUniform extends TextureUniform {

    public Texture2d texture2d;

    public Texture2dUniform(String name, Texture2d texture2d) {
        super(name, texture2d);
        this.texture2d = texture2d;
        textureTarget = GLES20.GL_TEXTURE_2D;
    }

    @Override
    protected void configure() {
        texture2d.configure(textureTarget);
    }

    @Override
    public void setValue(Integer value) {
        rationalChecks();
        GLES20.glUniform1i(_location, value);
    }

    @Override
    public Integer getValue() {
        int[] args = new int[1];
        GLES20.glGetUniformiv(program, _location, args, 0);
        return args[0];
    }
}
