package com.av.gltrans.gles.kenburns;

import android.view.animation.LinearInterpolator;

import com.av.gltrans.utils.MathCore;

public class SimpleKenburns implements Kenburns {

    private float scaleFrom;
    private float scaleTo;
    private LinearInterpolator interpolator;

    public SimpleKenburns(float scaleFrom, float scaleTo) {
        scaleFrom = 1.0f;
        scaleTo = 1.0f;
        interpolator = new LinearInterpolator();
    }

    @Override
    public void flip() {
        float tmp = scaleFrom;
        scaleFrom = scaleTo;
        scaleTo = tmp;
    }

    @Override
    public float getValue(float progress) {
        float interpolation = MathCore.smoothStep(interpolator.getInterpolation(progress), 0.0f, 1.0f);
        // Scale down to a value
        return (scaleFrom >= scaleTo) ?
                scaleTo + (scaleFrom - scaleTo) * (1f - interpolation)
                : scaleFrom + (scaleTo - scaleFrom) * interpolation;
    }
}
