package com.av.gltrans.gles.transition;

import com.av.gltrans.gles.graphics.uniform.Uniform;

import java.util.ArrayList;
import java.util.List;

public class Transition {

    private String name;

    protected String source;

    protected long duration;

    private List<Uniform<?>> uniforms = new ArrayList<Uniform<?>>();

    public Transition(String name, String source, long duration) {
        this.name = name;
        this.source = source;
        this.duration = duration;
    }

    protected void autoInit(Uniform<?> uniform) {
        uniforms.add(uniform);
    }

    public List<Uniform<?>> getUniforms() {
        return uniforms;
    }

    public void onUpdateUniforms() {

    }
}
