package com.av.gltrans.gles.graphics.texture;

import android.opengl.GLES20;

import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.uniform.Uniform;

public abstract class TextureUniform extends Uniform<Integer> {

    protected int textureTarget;

    protected Texture texture;

    public TextureUniform(String name, Texture texture) {
        super(name);
        this.texture = texture;
    }

    public int getId() {
        return texture.getId();
    }

    public void setId(int id) {
        texture.setId(id);
    }

    protected abstract void configure();

    @Override
    public void initialize(int p) throws RuntimeException {
        super.initialize(p);
        texture.initialize(false);
        enable();
        configure();
        disable();
    }

    public void setTextureToSlot(int textureSlot) {
        GLES20.glActiveTexture(textureSlot);
        enable();
        setValue(textureSlot - GLES20.GL_TEXTURE0);
    }

    public void enable() {
        rationalChecks();
        texture.enable(textureTarget);
    }

    public void disable() {
        texture.disable(textureTarget);
    }

    public void use(GLContextTask task) {
        enable();
        task.run();
        disable();
    }
}
