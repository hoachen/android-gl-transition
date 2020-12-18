package com.av.gltrans.gles.graphics.texture;

import android.opengl.GLES20;

public class Texture2d extends Texture {

    public Texture2d() {
        this(NO_TEXTURE);
    }

    public Texture2d(int id) {
        super(id);
    }

    public void createTexture() {
        super.createTexture();
    }

    public void configure(int textureTarget) {
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }
}
