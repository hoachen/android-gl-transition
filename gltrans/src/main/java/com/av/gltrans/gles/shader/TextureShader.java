package com.av.gltrans.gles.shader;

import android.opengl.GLES20;

import com.av.gltrans.gles.annotation.GlContext;
import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.Matrix4f;
import com.av.gltrans.gles.graphics.Vector2f;
import com.av.gltrans.gles.graphics.attribute.VertexAttribute;
import com.av.gltrans.gles.graphics.texture.Texture;
import com.av.gltrans.gles.graphics.uniform.BooleanUniform;
import com.av.gltrans.gles.graphics.uniform.Float2Uniform;
import com.av.gltrans.gles.graphics.uniform.FloatUniform;
import com.av.gltrans.gles.graphics.uniform.IntUniform;
import com.av.gltrans.gles.graphics.uniform.Mat4Uniform;
import com.av.gltrans.utils.BasicVertices;

import static com.av.gltrans.gles.graphics.uniform.Float2Uniform.uniform2f;
import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;
import static com.av.gltrans.gles.graphics.uniform.IntUniform.uniform1i;

public class TextureShader extends Shader {

    protected boolean hasExternalTexture = false;
    protected boolean isFlipVertical = false;
    protected boolean isFlipHorizontal = false;

    private float[] coords = BasicVertices.FULL_RECTANGLE;

    public Matrix4f mvpMatrix = Matrix4f.mat4();

    // Default to square ratio 1:1
    protected float ratio = 1.0f;

    // Viewport
    protected Vector2f resolution = Vector2f.vec2(0f, 0f);

    protected VertexAttribute textureCoordinateAttr = new VertexAttribute("vTextureCoordinate",
            BasicVertices.NORMAL_TEXTURE_COORDINATES, 2);

    protected VertexAttribute positionAttr = new VertexAttribute("vPosition",  coords, 3);

    protected BooleanUniform flipVerticalUniform = new BooleanUniform("flipY");

    protected BooleanUniform flipHorizontalUniform  = new BooleanUniform("flipX");

    protected Mat4Uniform mvpMatrixUniform = new Mat4Uniform("mvpMatrix");

    protected FloatUniform ratioUniform = uniform1f("ratio", true);

    protected Float2Uniform resolutionUniform = uniform2f("resolution", true);

    protected IntUniform textureUniform = uniform1i("texture", false);


    public TextureShader() {
        autoInit(textureCoordinateAttr);
        autoInit(positionAttr);
        autoInit(flipVerticalUniform);
        autoInit(flipHorizontalUniform);
        autoInit(mvpMatrixUniform);
        autoInit(ratioUniform);
        autoInit(resolutionUniform);
        autoInit(textureUniform);
    }

    public void setIsFlipVertical(boolean isFlipVertical) {
        this.isFlipVertical = isFlipVertical;
    }

    @GlContext
    public void updatePositionAttr() {
        positionAttr = new VertexAttribute(
                "vPosition",
                coords, 3
        );
        positionAttr.initialize(program);
    }

    // Noop
    @Override
    protected void onCreate() {
    }

    protected void beforeDraw() {

    }

    protected void afterDraw() {

    }

    protected void beforeDrawVertices() {

    }

    protected void afterDrawVertices() {

    }

    protected void writeUniforms() {
        flipVerticalUniform.setValue(isFlipVertical);
        flipHorizontalUniform.setValue(isFlipHorizontal);
        mvpMatrixUniform.setValue(mvpMatrix);
        ratioUniform.setValue(ratio);
        resolutionUniform.setValue(resolution);
        textureUniform.setValue(5);
    }

    public void setResolution(float width, float height) {
        resolution.x = width;
        resolution.y = height;
    }

    public void draw(Texture texture) {
        enable();
        positionAttr.use(new GLContextTask() {
            @Override
            public void run() {
                beforeDraw();
                textureCoordinateAttr.use(new GLContextTask() {
                    @Override
                    public void run() {
                        GLES20.glActiveTexture(GLES20.GL_TEXTURE5);
                        texture.use(GLES20.GL_TEXTURE_2D, new GLContextTask() {
                            @Override
                            public void run() {
                                writeUniforms();
                                beforeDrawVertices();
                                textureCoordinateAttr.drawArrays(GLES20.GL_TRIANGLE_STRIP);
                                afterDrawVertices();
                            }
                        });
                    }
                });
                afterDraw();
            }
        });
        disable();
    }

    public String transformFragmentShader(String frag) {
        String str = frag;
        if (hasExternalTexture) {
            str = "#extension GL_OES_EGL_image_external : require\n" + str;
            str = str.replace("sampler2D", "samplerExternalOES");
        }
        return str;
    }

    @Override
    protected String loadVertexShaderSource() {
        return  "precision mediump float;\n" +
                "attribute vec4 vPosition;\n" +
                "attribute vec2 vTextureCoordinate;\n" +
                "varying vec2 texCoord;\n" +
                "uniform mat4 mvpMatrix;\n" +
                "uniform bool flipY;\n" +
                "uniform bool flipX;\n" +
                "uniform float ratio;\n" +
                "void main() {\n" +
                "    gl_Position = mvpMatrix * vPosition;\n" +
                "    texCoord = vTextureCoordinate;\n" +
                "    if (flipY) {\n" +
                "        texCoord.y = 1.0 - texCoord.y;\n" +
                "    }\n" +
                "    if (flipX) {\n" +
                "        texCoord.x = 1.0 - texCoord.x;\n" +
                "    }\n" +
                "}";
    }


    protected String getFragmentShaderSource() {
        return " precision mediump float;\n" +
                "uniform sampler2D texture;\n" +
                "varying vec2 texCoord;\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(texture, texCoord);\n" +
                "}";
    }

    @Override
    protected String loadFragmentShaderSource() {
        return transformFragmentShader(getFragmentShaderSource());
    }
}
