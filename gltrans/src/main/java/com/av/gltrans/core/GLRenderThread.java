package com.av.gltrans.core;

import android.graphics.SurfaceTexture;

import com.av.gltrans.gles.egl.EglCore;
import com.av.gltrans.gles.egl.EglWindowSurface;

public class GLRenderThread extends Thread {

    private SurfaceTexture surfaceTexture;
    private EglCore eglCore;
    private EglWindowSurface windowSurface;
    private boolean isRunning = true;
    private GLDrawable drawable;
    private int width = -1;
    private int height =  -1;

    public GLRenderThread(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setGLDrawable(GLDrawable drawable) {
        this.drawable = drawable;
    }

    private void setup() {
        eglCore = new EglCore();
        windowSurface = new EglWindowSurface(eglCore, surfaceTexture);
    }

    public void recreate() {
        windowSurface.recreate(eglCore);
    }

    private void swapBuffers() {
        windowSurface.swapBuffers();
    }

    private void makeCurrent() {
        windowSurface.makeCurrent();
    }

    private void release() {
        windowSurface.release();
        eglCore.release();
    }

    private boolean drawFrame() {
        if (drawable != null) {
            return drawable.onDraw();
        }
        return false;
    }

    @Override
    public void run() {
        setup();
        makeCurrent();
        drawable.onSetup();
        while (isRunning && !interrupted()) {
            makeCurrent();
            if (drawFrame())
                swapBuffers();
        }
        release();
    }

    public void quit() {
        isRunning = false;
    }
}
