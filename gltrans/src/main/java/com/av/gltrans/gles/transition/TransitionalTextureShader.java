package com.av.gltrans.gles.transition;

import android.opengl.GLES20;

import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.texture.Texture;
import com.av.gltrans.gles.graphics.uniform.FloatUniform;
import com.av.gltrans.gles.graphics.uniform.IntUniform;
import com.av.gltrans.gles.graphics.uniform.Uniform;
import com.av.gltrans.gles.shader.TextureShader;

import java.util.LinkedList;
import java.util.Queue;

import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;
import static com.av.gltrans.gles.graphics.uniform.IntUniform.uniform1i;

public class TransitionalTextureShader extends TextureShader {

    private Transition transition;
    protected float progress = 0f;

    private FloatUniform progressUniform = uniform1f("progress", false);
    private IntUniform textureFromUniform = uniform1i("texture", false);
    private IntUniform textureToUniform = uniform1i("texture2", false);
    private Queue<Runnable> runOnPreDraw = new LinkedList();

    public TransitionalTextureShader(Transition transition) {
        this.transition = transition;
        for (Uniform<?> uniform : transition.getUniforms()) {
            autoInit(uniform);
        }
        autoInit(progressUniform);
        autoInit(textureFromUniform);
        autoInit(textureToUniform);
    }

    @Override
    public void beforeDrawVertices() {
        super.beforeDrawVertices();
        transition.onUpdateUniforms();
        progressUniform.setValue(progress);
        textureFromUniform.setValue(1);
        textureToUniform.setValue(2);
    }

    // Don't forget to use the program
    public void draw(Texture tex1, Texture tex2) {
        enable();
        while (!runOnPreDraw.isEmpty()) {
            Runnable runnable = runOnPreDraw.poll();
            runnable.run();
        }
        positionAttr.use(new GLContextTask() {
            @Override
            public void run() {
                beforeDraw();
                textureCoordinateAttr.use(new GLContextTask() {
                     @Override
                     public void run() {
                           GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                           tex1.use(GLES20.GL_TEXTURE_2D, new GLContextTask() {
                                    @Override
                                    public void run() {
                                        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
                                        tex2.use(GLES20.GL_TEXTURE_2D, new GLContextTask() {
                                             @Override
                                             public void run() {
                                                  writeUniforms();
                                                  beforeDrawVertices();
                                                  textureCoordinateAttr.drawArrays(GLES20.GL_TRIANGLES);
                                                  afterDrawVertices();
                                             }
                                        });
                                    }
                           });
                     }
                });
                afterDraw();
            }
        });
        disable();
    }

    protected String getFragmentShaderSource() {
        return FRAGMENT_SHADER + "\n" + transition.source;
    }

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
            "uniform sampler2D texture;\n" +
            "uniform sampler2D texture2;\n" +
            "uniform sampler2D textureQuote;\n" +
            "uniform float progress;\n" +
            "uniform float ratio;\n" +
            "varying vec2 texCoord;\n" +
            "vec4 getFromColor(vec2 uv) {\n" +
            "     return texture2D(texture, uv);\n" +
            "}\n" +
            "vec4 getToColor(vec2 uv) {\n" +
            "     return texture2D(texture2, uv);\n" +
            "}\n" +
            "vec4 transition(vec2 uv);\n" +
            "void main() {\n" +
            "     gl_FragColor = transition(texCoord);\n" +
            " }";
}
