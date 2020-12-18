package com.av.gltrans.gles.shader;

import android.opengl.Matrix;

import com.av.gltrans.gles.graphics.Matrix4f;
import com.av.gltrans.gles.graphics.uniform.FloatUniform;

import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;

public class AlphaOverlayTextureShader extends TextureShader {

    private static final String ALPHA_UNIFORM = "alpha";

    private float alpha  = 1.0f;

    protected float scaleX = 1.0f;
    protected float scaleY = 1.0f;
    protected float translateX = 0.0f;
    protected float translateY = 0.0f;
    protected float rotation = 0.0f;

    protected FloatUniform alphaUniform = uniform1f(ALPHA_UNIFORM, false);

    private float[] scaleMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] translateMatrix = new float[16];

    public AlphaOverlayTextureShader() {
        autoInit(alphaUniform);
    }

    public void setIdentities() {
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.setIdentityM(translateMatrix, 0);
    }

    public void setMatrix(Matrix4f matrix) {
        this.mvpMatrix.elements = matrix.elements.clone();
        setIdentities();
        Matrix.translateM(translateMatrix, 0, translateX, translateY, 0f);
        Matrix.multiplyMM(
                mvpMatrix.elements, 0, mvpMatrix.elements, 0,
                translateMatrix, 0);
    }

    @Override
    public void writeUniforms() {
        super.writeUniforms();
        alphaUniform.setValue(alpha);
    }

    @Override
    protected String getFragmentShaderSource() {
        return  "precision mediump float;\n" +
                "uniform sampler2D texture;\n" +
                "uniform float " + ALPHA_UNIFORM +";\n" +
                "varying vec2 texCoord;\n" +
                "void main() {\n" +
                "    vec4 textureColor = texture2D(texture, texCoord);\n" +
                "    gl_FragColor = mix(vec4(0.0), textureColor, " + ALPHA_UNIFORM + ");\n" +
                "}";
    }
}
