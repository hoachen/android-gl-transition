package com.av.gltrans.gles.transition;

import com.av.gltrans.gles.graphics.uniform.FloatUniform;

import static com.av.gltrans.gles.graphics.uniform.FloatUniform.uniform1f;

public class AngularTransition extends Transition {

    private float startingAngle = 90f;

    private FloatUniform startingAngleUniform = uniform1f("startingAngle", false);


    public AngularTransition() {
        super("angular", SOURCE, 1000L);
        autoInit(startingAngleUniform);
    }

    @Override
    public void onUpdateUniforms() {
        super.onUpdateUniforms();
        startingAngleUniform.setValue(startingAngle);
    }

    public static final String SOURCE =
            "#define PI 3.141592653589\n" +
            "uniform float startingAngle; // = 90;\n" +
            "vec4 transition (vec2 uv) {\n" +
            "  float offset = startingAngle * PI / 180.0;\n" +
            "  float angle = atan(uv.y - 0.5, uv.x - 0.5) + offset;\n" +
            "  float normalizedAngle = (angle + PI) / (2.0 * PI);\n" +
            "  normalizedAngle = normalizedAngle - floor(normalizedAngle);\n" +
            "  return mix(\n" +
            "    getFromColor(uv),\n" +
            "    getToColor(uv),\n" +
            "    step(normalizedAngle, progress)\n" +
            "    );\n" +
            "}";

}
