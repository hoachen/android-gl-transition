package com.av.gltrans.core;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class GLEngine implements TextureView.SurfaceTextureListener {

    private static final String TAG = "GLEngine";

    private int viewportWidth = 0;
    private int viewportHeight = 0;
    private GLRenderThread renderThread;

    private List<File> mFiles = new ArrayList<>();

    private void setViewport(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
    }

    public void setFiles(List<File> files) {
        mFiles.clear();
        mFiles.addAll(files);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        if (surfaceTexture == null)
            return;
        onSurfaceCreated(surfaceTexture, width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        onSurfaceSizeChanged(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        releaseSurface();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }

    public void onSurfaceCreated(SurfaceTexture surface, int width, int height) {
        setViewport(width, height);
        renderThread = new GLRenderThread(surface);
        renderThread.start();
    }

    private void onSurfaceSizeChanged(int width, int height) {
        setViewport(width, height);
    }

    private void releaseSurface() {
        renderThread.quit();
    }


    public void begin() {
        for (File file : mFiles) {
            Log.i(TAG, "file :" + file.getPath());
        }
    }
}
