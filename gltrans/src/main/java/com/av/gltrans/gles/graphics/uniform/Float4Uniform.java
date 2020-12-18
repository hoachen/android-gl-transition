package com.av.gltrans.gles.graphics.uniform;



import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.Vector4f;

public class Float4Uniform extends Uniform<Vector4f> {

    public Float4Uniform(String name) {
        super(name);
    }

    public static Float4Uniform uniform4f(String name, boolean isOptional) {
        Float4Uniform float4Uniform = new Float4Uniform(name);
        float4Uniform.isOptional = isOptional;
        return float4Uniform;
    }

    @Override
    public void setValue(Vector4f value) {
        rationalChecks();
        GLES20.glUniform4f(getLocation(), value.x, value.y, value.z, value.w);
        cachedValue = value;
    }

    @Override
    public Vector4f getValue() {
        rationalChecks();
        float[] args = new float[4];
        GLES20.glGetUniformfv(program, getLocation(), args, 0);
        return Vector4f.fromFloatArray(args);
    }


}
