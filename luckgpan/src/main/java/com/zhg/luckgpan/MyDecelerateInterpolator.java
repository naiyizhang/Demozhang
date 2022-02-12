package com.zhg.luckgpan;

import android.animation.TimeInterpolator;

public class MyDecelerateInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float val =  (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
        if(val>input){
            return val;
        }
        return input;
    }
}
