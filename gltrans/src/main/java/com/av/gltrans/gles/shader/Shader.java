package com.av.gltrans.gles.shader;


import android.opengl.GLES20;

import com.av.gltrans.gles.egl.GLContextTask;
import com.av.gltrans.gles.graphics.InputValue;
import com.av.gltrans.utils.GlUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Shader {

    protected int vertexShader = -1;
    protected int fragmentShader = -1;
    protected int program = -1;

    protected boolean isProgramCreated = false;

    protected List<InputValue> inputValues = new ArrayList<>();

    protected abstract void onCreate();

    protected abstract String loadVertexShaderSource();

    protected abstract String loadFragmentShaderSource();

    protected void createProgram() {
        GlUtils.ShaderData info = GlUtils.createProgram(loadVertexShaderSource(), loadFragmentShaderSource());
        if (info != null) {
            program = info.program;
            vertexShader = info.vertexShader;
            fragmentShader = info.fragmentShader;
            isProgramCreated = true;
        }
    }

    protected InputValue<?> autoInit(InputValue<?> inputValue) {
        inputValues.add(inputValue);
        return inputValue;
    }

    protected boolean isValidProgram() {
        return program != -1;
    }

    protected void validateProgram() throws RuntimeException {
        if (!isValidProgram() || !isProgramCreated) {
            throw new RuntimeException("Program is invalid!");
        }
    }

    protected void enable() {
        validateProgram();
        GLES20.glUseProgram(program);
    }

    protected void disable() {
        GLES20.glUseProgram(0);
    }

    protected void initializeInputValues() {
        for (InputValue input : inputValues) {
            input.initialize(program);
        }
    }

    public void setup() {
        createProgram();
        initializeInputValues();
        onCreate();
    }

    public boolean trySetup() {
        if (isProgramCreated) return false;
        setup();
        return true;
    }

    public void releaseShaders() {
        GLES20.glDetachShader(program, vertexShader);
        GLES20.glDetachShader(program, fragmentShader);
    }

    public void releaseProgram() {
        releaseShaders();
        GLES20.glDeleteProgram(program);
        isProgramCreated = false;
        program = -1;
    }


    public void release() {
        releaseProgram();
    }

    void use(GLContextTask task) {
        enable();
        task.run();
        disable();
    }



}
