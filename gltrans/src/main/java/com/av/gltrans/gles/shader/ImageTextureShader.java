package com.av.gltrans.gles.shader;

import android.graphics.Bitmap;

import com.av.gltrans.gles.graphics.texture.BitmapTextureUniform;
import com.av.gltrans.gles.graphics.texture.Texture2dUniform;

public class ImageTextureShader extends TextureShader {

    Texture2dUniform texture2d;

    public ImageTextureShader(Bitmap bitmap) {
        texture2d = new BitmapTextureUniform("texture", bitmap);
    }

    protected void draw() {
        super.draw(texture2d.texture2d);

    }
}
