package com.av.gltrans.gles;

import android.util.Size;

public interface RenderContext {

    void onCreated();

    boolean onDraw();

    void onSizeChanged(Size size);
}
