package com.av.gltrans.gles;

import android.content.Context;
import android.util.Size;

import com.av.gltrans.gles.egl.EglCore;

public interface RenderTarget {

    void initialize(EglCore eglCore);

    void swapBuffers();

    void makeCurrent();

    void release();

    Context context();

    Size getViewport();
}
