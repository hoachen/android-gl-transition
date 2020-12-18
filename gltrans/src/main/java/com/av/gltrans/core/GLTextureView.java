package com.av.gltrans.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GLTextureView extends TextureView {

    private GLEngine glEngine;

    public GLTextureView(@NonNull Context context) {
        this(context, null);
    }

    public GLTextureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLTextureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        glEngine = new GLEngine();
        setSurfaceTextureListener(glEngine);
    }


    public void setFiles(List<File> files) {
        glEngine.setFiles(files);
        glEngine.begin();
    }
}
