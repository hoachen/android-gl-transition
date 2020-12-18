package com.av.gltrans.gles.graphics.texture;

import android.opengl.GLES20;

import com.av.gltrans.gles.annotation.GlContext;
import com.av.gltrans.gles.egl.GLContextTask;

public class Texture {

    public static final int NO_TEXTURE = -1;

    private int id = NO_TEXTURE;

    public Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    @GlContext
    public void initialize() {
        this.initialize(false);
    }

    @GlContext
    public void initialize(boolean force) {
        if (id == NO_TEXTURE || force) {
            createTexture();
        }
    }

    @GlContext
    public void createTexture() {
        int[] args = new int[1];
        GLES20.glGenTextures(args.length, args, 0);
        id = args[0];
    }

    @GlContext
    public void enable(int textureTarget) {
        GLES20.glBindTexture(textureTarget, id);
    }

    @GlContext
    public void disable(int textureTarget) {
        GLES20.glBindTexture(textureTarget, 0);
    }

    public void use(int textureTarget, GLContextTask task) {
        enable(textureTarget);
        task.run();
        disable(textureTarget);
    }

    public void release() {
        if (id == NO_TEXTURE)
            return;
        int args[] = new int[1];
        args[0] = id;
        GLES20.glDeleteTextures(1, args, 0);
    }


}
