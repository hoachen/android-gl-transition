package com.av.gltrans.gles.graphics.uniform;



import android.opengl.GLES20;

import com.av.gltrans.gles.graphics.Vector3f;

public class Float3Uniform extends Uniform<Vector3f> {

    public Float3Uniform(String name) {
        super(name);
    }


    public static Float3Uniform uniform3f(String name, boolean isOptional) {
        Float3Uniform float3Uniform = new Float3Uniform(name);
        float3Uniform.isOptional = isOptional;
        return float3Uniform;
    }

    @Override
    public void setValue(Vector3f value) {
        rationalChecks();
        GLES20.glUniform3f(getLocation(), value.x, value.y, value.z);
        cachedValue = value;
    }

    @Override
    public Vector3f getValue() {
        rationalChecks();
        float[] args = new float[3];
        GLES20.glGetUniformfv(program, getLocation(), args, 0);
        return Vector3f.fromFloatArray(args);
    }


}
