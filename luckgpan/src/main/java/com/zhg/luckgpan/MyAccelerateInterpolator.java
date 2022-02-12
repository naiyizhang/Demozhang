package com.zhg.luckgpan;

import android.animation.TimeInterpolator;
import android.util.Log;

public class MyAccelerateInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float val =  (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
        Log.e("TAG", "getInterpolation: val = "+val+",input = "+input );
        if(val<input){
            return val;
        }
        return input;
    }
}
