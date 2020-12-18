package com.av.gltrans.gles.graphics.texture;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class BitmapTextureUniform extends Texture2dUniform {

    private Bitmap bitmap;

    public BitmapTextureUniform(String name, Bitmap bitmap) {
        super(name, new Texture2d(Texture.NO_TEXTURE));
        this.bitmap = bitmap;
    }

    @Override
    public void configure() {
        super.configure();
        GLUtils.texImage2D(textureTarget, 0, bitmap, 0);
    }
}
