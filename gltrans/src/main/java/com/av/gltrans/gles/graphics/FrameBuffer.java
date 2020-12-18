package com.av.gltrans.gles.graphics;

import android.opengl.GLES20;

import com.av.gltrans.gles.annotation.GlContext;
import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.texture.Texture;
import com.av.gltrans.utils.GlUtils;


public class FrameBuffer {

    protected int width;
    protected int height;
    protected int frameBufferName = 0;
    protected int renderBufferName = 0;
    protected int texName = 0;
    protected Texture texture = null;

    @GlContext
    public void setup() {
        setup(width, height);
    }

    public void setup(int width, int height) {

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid width and height!");
        }

        this.width = width;
        this.height = height;

        int[] args = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, args, 0);

        if (width > args[0] || height > args[0]) {
            throw new IllegalArgumentException("Width or height is higher than GL_MAX_RENDER_BUFFER");
        }

        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, args, 0);
        int savedFrameBuffer = args[0];

        GLES20.glGetIntegerv(GLES20.GL_RENDERBUFFER_BINDING, args, 0);
        int savedRenderBuffer = args[0];

        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, args, 0);
        int savedTexName = args[0];

        release();

        try {
            GLES20.glGenFramebuffers(args.length, args, 0);
            frameBufferName = args[0];

            GLES20.glGenRenderbuffers(args.length, args, 0);
            renderBufferName = args[0];

            GLES20.glGenTextures(args.length, args, 0);
            texName = args[0];

            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName);
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBufferName);

            GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
            GLES20.glFramebufferRenderbuffer(
                    GLES20.GL_FRAMEBUFFER,
                    GLES20.GL_DEPTH_ATTACHMENT,
                    GLES20.GL_RENDERBUFFER,
                    renderBufferName
            );

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName);
            GlUtils.setupTextureSampler(GLES20.GL_TEXTURE_2D, GLES20.GL_LINEAR, GLES20.GL_NEAREST);
            GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA,
                    width,
                    height,
                    0,
                    GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,
                    null
            );
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texName, 0);

            int frameBufferStatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);

            if (frameBufferStatus != GLES20.GL_FRAMEBUFFER_COMPLETE) {
                throw new RuntimeException("Failed to initialize framebuffer object: $frameBufferStatus");
            }
            texture = new Texture(texName);
        } catch (RuntimeException e) {
            release();
            throw e;
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, savedFrameBuffer);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, savedRenderBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, savedTexName);
    }

    /**
     * Switch to our framebuffer object.
     */
    public void enable() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferName);
    }

    /**
     * This will revert to the default framebuffer object.
     */

    public void disable() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void use(GLContextTask task) {
        enable();
        task.run();
        disable();
    }

    public void release() {
        int[] args = new int[1];

        args[0] = texName;
        GLES20.glDeleteTextures(args.length, args, 0);
        texName = 0;

        args[0] = renderBufferName;
        GLES20.glDeleteRenderbuffers(args.length, args, 0);
        renderBufferName = 0;

        args[0] = frameBufferName;
        GLES20.glDeleteFramebuffers(args.length, args, 0);
        frameBufferName = 0;
    }

    public Texture asTexture() {
        if (texture == null) {
            throw new RuntimeException("Texture was null did you setup it yet?");
        }
        return texture;
    }

}
