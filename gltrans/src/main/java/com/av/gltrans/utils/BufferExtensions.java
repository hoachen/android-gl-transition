package com.av.gltrans.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferExtensions {

    private static final int  FLOAT_SIZE_BYTES = 4;

    public static ByteBuffer toByteBuffer(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .put(bytes);
        byteBuffer.position(0);
        return byteBuffer;
    }

    public static FloatBuffer toFloatBuffer(float[] values) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(values.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(values);
        floatBuffer.position(0);
        return floatBuffer;
    }


    public static IntBuffer toIntBuffer(int[] values) {
        IntBuffer intBuffer = ByteBuffer.allocateDirect(values.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer().put(values);
        intBuffer.position(0);
        return intBuffer;
    }
}
