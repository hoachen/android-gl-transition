package com.av.gltrans.core;

import com.av.gltrans.gles.RenderContext;

public abstract class GLDrawable implements RenderContext {


    public abstract void onSetup();

    public abstract void renderAtProgress(float progress);

    public void onCreated() {
        onSetup();
    }
}
