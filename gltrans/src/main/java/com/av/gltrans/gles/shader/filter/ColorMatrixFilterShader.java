package com.av.gltrans.gles.shader.filter;

import com.av.gltrans.gles.graphics.Matrix4f;
import com.av.gltrans.gles.graphics.uniform.FloatUniform;
import com.av.gltrans.gles.graphics.uniform.Mat4Uniform;
import com.av.gltrans.gles.shader.TextureShader;

import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;
import static com.av.gltrans.gles.graphics.uniform.Mat4Uniform.uniformMat4;

public class ColorMatrixFilterShader extends TextureShader {

    private Matrix4f colorMatrix = Matrix4f.mat4();

    private float intensity = 1.0f;

    private Mat4Uniform colorMatrixUniform = uniformMat4("colorMatrix");
    private FloatUniform intensityUniform = uniform1f("intensity", false);

    public ColorMatrixFilterShader() {
        autoInit(mvpMatrixUniform);
        autoInit(intensityUniform);
    }

    @Override
    public void beforeDrawVertices() {
        super.beforeDrawVertices();
        colorMatrixUniform.setValue(colorMatrix);
        intensityUniform.setValue(intensity);
    }


    @Override
    protected String getFragmentShaderSource() {
        return " precision mediump float;\n" +
                "uniform sampler2D texture;\n" +
                "uniform mat4 colorMatrix;\n" +
                "uniform float intensity;\n" +
                "varying vec2 texCoord;\n" +
                "void main() {\n" +
                "    vec4 textureColor = texture2D(texture, texCoord);\n" +
                "    vec4 outputColor = textureColor * colorMatrix;\n" +
                "    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n" +
                "}";
    }
}
