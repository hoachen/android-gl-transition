package com.av.gltrans.gles.shader.filter;

import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.FrameBuffer;
import com.av.gltrans.gles.graphics.texture.Texture;
import com.av.gltrans.gles.shader.TextureShader;

import java.util.ArrayList;
import java.util.List;

public class CompositeShader {

    private int width = -1;
    private int height = -1;
    private List<TextureShader> shaders = new ArrayList<>();
    private List<FrameBuffer> frameBuffers = new ArrayList<>();

    private void invalidate() {
        for (FrameBuffer fb : frameBuffers) {
            fb.release();
        }
        frameBuffers.clear();
        for (TextureShader shader : this.shaders) {
            shader.trySetup();
            if (width != -1 && height != -1) {
                shader.setResolution((float)width, (float)height);
            }
            FrameBuffer frameBuffer = new FrameBuffer();
            frameBuffer.setup(width, height);
            frameBuffers.add(frameBuffer);
        }
    }

    public void add(List<TextureShader> shaders) {
        this.shaders.addAll(shaders);
        invalidate();
    }

    public void setup(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(Texture texture) {
        for (int i = 0; i < shaders.size(); i++) {
            final int index = i;
            final TextureShader shader = shaders.get(index);
            if (index == 0) {
                frameBuffers.get(0).use(new GLContextTask() {
                    @Override
                    public void run() {
                        shader.draw(texture);
                    }
                });
                continue;
            }
            if (index == shaders.size() - 1) {
                shader.setIsFlipVertical(true);
                shader.draw(frameBuffers.get(index-1).asTexture());
                continue;
            }
            frameBuffers.get(index).use(new GLContextTask() {
                @Override
                public void run() {
                    shader.draw(frameBuffers.get(index-1).asTexture());
                }
            });
        }
    }

//
//    public void draw(Texture texture, Texture tex2) {
//        for (int i = 0; i < shaders.size(); i++) {
//            final TextureShader shader = shaders.get(i);
//            final int index = i;
//            if (index == 0) {
//                frameBuffers.get(0).use(new GLContextTask() {
//                    @Override
//                    public void run() {
//                        if (shader instanceof TransitionalTextureShader) {
//                            shader.draw(texture, tex2);
//                        } else {
//                            shader.draw(texture);
//                        }
//                    }
//                });
//                continue;
//            }
//
//            if (index == shaders.size() - 1) {
//                shader.setIsFlipVertical(true);
//                shader.draw(frameBuffers.get(index - 1).asTexture());
//                continue;
//            }
//
//            frameBuffers.get(index).use(new GLContextTask() {
//                @Override
//                public void run() {
//                    shader.draw(frameBuffers.get(index - 1).asTexture());
//                }
//            });
//        }
//    }
}
