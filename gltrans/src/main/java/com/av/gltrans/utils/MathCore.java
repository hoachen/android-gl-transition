package com.av.gltrans.utils;

public class MathCore {

    /**
     * Constraint output value from input value.
     * @param min Minimum output value
     * @param max Maximum output value
     */
    public static float clamp(float value ,float min, float max) {
        return Math.min(max, Math.max(min, value));
    }

    public static float smoothStep(float value, float edge0, float edge1) {
        float temp = ((value - edge0) / (edge1 - edge0));
        float x = clamp(temp, 0f, 1f);
        return (x * x * (3f - 2f * x));
    }

    /**
     * Linear Interpolate between two values
     * @param start Start value
     * @param end End value
     * @receiver a float range value between 0f..1f
     */
    public static float lerp(float value ,float start, float end) {
        return (1 - value) * start + value * end;
    }


    public static float toRadians(float value) {
        return (float) (value * Math.PI * 1.0f / 180f);
    }

}
