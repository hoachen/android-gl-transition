package com.av.gltrans.gles.transition;


import com.av.gltrans.gles.graphics.Vector4f;
import com.av.gltrans.gles.graphics.uniform.Float4Uniform;
import com.av.gltrans.gles.graphics.uniform.FloatUniform;

import static com.av.gltrans.gles.graphics.uniform.Float4Uniform.uniform4f;
import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;

public class BounceTransition extends Transition {

    private Vector4f shadowColour = new Vector4f(0f, 0f, 0f, 0.6f);
    private Float4Uniform shadowColourUniform = uniform4f("shadow_colour", false);
    private float shadowHeight = 0.075f;
    private FloatUniform shadowHeightUniform = uniform1f("shadow_height", false);
    private float bounces = 3f;
    private FloatUniform bouncesUniform = uniform1f("bounces", false);

    public BounceTransition() {
        super("angular", SOURCE, 1000L);
        autoInit(shadowColourUniform);
        autoInit(shadowHeightUniform);
        autoInit(bouncesUniform);
    }

    @Override
    public void onUpdateUniforms() {
        super.onUpdateUniforms();
        shadowColourUniform.setValue(shadowColour);
        shadowHeightUniform.setValue(shadowHeight);
        bouncesUniform.setValue(bounces);
    }

    public static final String SOURCE =
                    "uniform vec4 shadow_colour; // = vec4(0.,0.,0.,.6)\n" +
                    "uniform float shadow_height; // = 0.075\n" +
                    "uniform float bounces; // = 3.0\n" +
                    "const float PI = 3.14159265358;\n" +
                    "vec4 transition (vec2 uv) {\n" +
                    "  float time = progress;\n" +
                    "  float stime = sin(time * PI / 2.);\n" +
                    "  float phase = time * PI * bounces;\n" +
                    "  float y = (abs(cos(phase))) * (1.0 - stime);\n" +
                    "  float d = uv.y - y;\n" +
                    "  return mix(\n" +
                    "    mix(\n" +
                    "      getToColor(uv),\n" +
                    "      shadow_colour,\n" +
                    "      step(d, shadow_height) * (1. - mix(\n" +
                    "        ((d / shadow_height) * shadow_colour.a) + (1.0 - shadow_colour.a),\n" +
                    "        1.0,\n" +
                    "        smoothstep(0.95, 1., progress) // fade-out the shadow at the end\n" +
                    "      ))\n" +
                    "    ),\n" +
                    "    getFromColor(vec2(uv.x, uv.y + (1.0 - y))),\n" +
                    "    step(d, 0.0)\n" +
                    "  );\n" +
                    "}";

}
