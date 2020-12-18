package com.av.gltrans.core;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;

import com.av.gltrans.gles.RenderContext;
import com.av.gltrans.gles.annotation.GlContext;
import com.av.gltrans.gles.egl.EglCore;
import com.av.gltrans.gles.egl.EglSurfaceBase;

import androidx.annotation.MainThread;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class Studio implements DefaultLifecycleObserver {

    private EglCore eglCore = null;
    private HandlerThread thread;
    private Handler handler = null;
    private OutputSurface outputSurface;
    private boolean isStarted = false;
    private RenderContext renderContext;
    private Size size = new Size(-1, -1);
    private ReadyCallBack mCallBack;

    public interface ReadyCallBack {
        void onReadyCallback(Studio studio, EglCore eglCore);
    }

    private Studio() {
    }

    public static Studio create(LifecycleOwner lifecycleOwner, ReadyCallBack callBack) {
        Studio studio = new Studio();
        studio.mCallBack = callBack;
        lifecycleOwner.getLifecycle().addObserver(studio);
        return studio;
    }

    @Override
    public void onCreate(LifecycleOwner owner) {
        initialize();
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        // Pause rendering
        // Queue pause action to handler thread
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        // Continue rendering
        // Queue resume action to handler thread
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        release();
    }

    public void initialize() {
        thread = new HandlerThread("GLThread");
        thread.start();
        handler = new Handler(thread.getLooper());
        isStarted = true;
        initializeEglCore();
    }

    @MainThread
    public void setRenderContext(RenderContext renderContext) {
        this.renderContext = renderContext;
        dispatchUpdate();
    }

    @MainThread
    public void setSize(Size size) {
        this.size = size;
        dispatchSizeUpdate();
    }

    @MainThread
    private void dispatchUpdate() {
        post(true, new Runnable() {
            @Override
            public void run() {
                renderContext.onCreated();
            }
        });
    }

    @MainThread
    private void dispatchSizeUpdate() {
        post(true, new Runnable() {
            @Override
            public void run() {
                if (!isValidSize() && outputSurface != null) {
                    int surfaceWidth = outputSurface.eglSurface != null ? outputSurface.eglSurface.getWidth() : -1;
                    int surfaceHeight = outputSurface.eglSurface != null ? outputSurface.eglSurface.getHeight() : -1;
                    size = new Size(surfaceWidth, surfaceHeight);
                }
                renderContext.onSizeChanged(size);
            }
        });
    }

    public void setOutputSurface(OutputSurface outputSurface) {
        this.outputSurface = outputSurface;
        post(true, new Runnable() {
            @Override
            public void run() {
                outputSurface.makeCurrent();
            }
        });
        dispatchSizeUpdate();
    }

    @MainThread
    public void release() {
        post(true, new Runnable() {
            @Override
            public void run() {
                eglCore.release();
                eglCore = null;
                thread.quitSafely();
                isStarted = false;
            }
        });
    }

    @MainThread
    public OutputSurface createOutputSurface()  {
        EglSurfaceBase eglSurfaceBase = new EglSurfaceBase(requireEglCore());
        return new OutputSurface(eglSurfaceBase, handler);
    }

    @MainThread
    public void dispatchDraw() {
        post(true, new Runnable() {
            @Override
            public void run() {
                draw();
            }
        });
    }



    public void directDraw(Runnable runnable) {
        if (outputSurface == null) {
            return;
        }
        outputSurface.makeCurrent();
        runnable.run();
        renderContext.onDraw();
        outputSurface.swapBuffers();
    }

    @GlContext
    public void draw() {
        if (outputSurface == null)
            return;
        outputSurface.makeCurrent();
        renderContext.onDraw();
        outputSurface.swapBuffers();
    }

    public void post(boolean check, Runnable task) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (check)
                    nullChecks();
                task.run();
            }
        });
    }

    private void initializeEglCore() {
        post(false, new Runnable() {
            @Override
            public void run() {
                eglCore = new EglCore();
                dispatchOnReady();
            }
        });
    }

    private void dispatchOnReady() {
        if (mCallBack != null) {
            mCallBack.onReadyCallback(this, requireEglCore());
        }
    }

    private boolean isValidSize() {
        return size.getWidth() > 0 && size.getHeight() > 0;
    }

    private void nullChecks() {
        if (eglCore == null) {
            throw new RuntimeException( "EglCore was null, please setup or create a new one");
        }
        if (handler == null) {
            throw new RuntimeException( "Handler was null, please setup or create a new one" );
        }
        if (!isStarted) {
            throw new RuntimeException( "Thread hasn't started yet!" );
        }
    }

    private EglCore requireEglCore() {
        return eglCore;
    }

    public class OutputSurface {

        private EglSurfaceBase eglSurface;
        private Handler handler;

        public OutputSurface(EglSurfaceBase elgSurface, Handler handler) {
            this.eglSurface = elgSurface;
            this.handler = handler;
        }

        public void makeCurrent() {
            eglSurface.makeCurrent();
        }

        public void swapBuffers() {
            eglSurface.swapBuffers();
        }

        /**
         * Can be called if the surface is released
         */
        public void fromSurfaceTexture(SurfaceTexture surfaceTexture) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    eglSurface.createWindowSurface(surfaceTexture);
                }
            });
        }

        public void release() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    eglSurface.releaseEglSurface();
                }
            });
        }

        public void fromSurface(Surface surface) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    eglSurface.createWindowSurface(surface);
                }
            });
        }
    }

}
