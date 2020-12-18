package com.av.gltrans.gles.shader.filter;

import com.av.gltrans.gles.graphics.uniform.FloatUniform;
import com.av.gltrans.gles.shader.TextureShader;

public class BrightnessFilterShader extends TextureShader {

    private float brightness = 0.0f;

    private FloatUniform brightnessUniform = new FloatUniform("brightness");

    public BrightnessFilterShader(float brightness) {
        this.brightness = brightness;
        autoInit(brightnessUniform);
    }

    @Override
    public void beforeDrawVertices() {
        brightnessUniform.setValue(brightness);
    }

    @Override
    protected String getFragmentShaderSource() {
        return "precision mediump float;\n" +
                "uniform sampler2D texture;\n" +
                "uniform float brightness;\n" +
                "varying vec2 texCoord;\n" +
                "void main() {\n" +
                "     vec4 textureColor = texture2D(texture, texCoord);\n" +
                "     textureColor.rgb += vec3(brightness);\n" +
                "     gl_FragColor = textureColor;\n" +
                " }";
    }
}
