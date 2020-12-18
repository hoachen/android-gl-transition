package com.av.gltrans.gles.graphics.attribute;

import android.opengl.GLES20;

import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.utils.BufferExtensions;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class VertexAttribute extends Attribute<float[]> {

    private static final int FLOAT_SIZE_BYTES = 4;
    private float[] vertices;
    private int coordinatesPerVertex; // 2
    private FloatBuffer verticesBuffer;

    public VertexAttribute(String name, float[] vertices, int coordinatesPerVertex) {
        super(name);
        this.vertices = vertices;
        this.coordinatesPerVertex = coordinatesPerVertex;
        setValue(vertices);
    }


    @Override
    public void setValue(float[] values) {
        cachedValue = values;
        verticesBuffer = BufferExtensions.toFloatBuffer(values);
    }

    @Override
    public float[] getValue() {
        return vertices;
    }

    public void enable() {
        GLES20.glEnableVertexAttribArray(getLocation());
        GLES20.glVertexAttribPointer(
                getLocation(), coordinatesPerVertex, GLES20.GL_FLOAT,
                false, coordinatesPerVertex * FLOAT_SIZE_BYTES, verticesBuffer);
    }

//    GLES20.GL_TRIANGLES
    public void drawArrays(int mode) {
        GLES20.glDrawArrays(mode, 0, vertices.length / coordinatesPerVertex);
    }


    public void drawElements(int mode, int count, ByteBuffer indices) {
        if (count <= 0)
            throw new RuntimeException("Vertices count cannot be lower than 0");
        GLES20.glDrawElements(mode, count, GLES20.GL_UNSIGNED_BYTE, indices);
    }

//    public void drawElements(int mode, byte[] indices) {
//        ByteBuffer buffer = null;
//        if (lastIndices == indices) {
//            buffer = lastIndicesBuffer;
//        }
//        lastIndices = indices;
//        lastIndicesBuffer = byte
//        val buffer: ByteBuffer =
//                (if (lastIndices?.contentEquals(indices) == true) lastIndicesBuffer
//            else null) ?: indices.toByteBuffer()
//        drawElements(mode, indices.length, buffer);
//    }

//    void drawTriangleElements(vararg indices: Byte) {
//        drawElements(indices = indices)
//    }

    public void disable() {
        GLES20.glDisableVertexAttribArray(getLocation());
    }

    public void use(GLContextTask task) {
        enable();
        task.run();
        disable();
    }

}
