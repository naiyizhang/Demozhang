package com.zhg.luckgpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout lucky;
    private Button button;

    private long duration = 1000;

    private float currentDegree;

    private AnimatorSet runningAnimator;

    private boolean isRunning;

    private int mMaxSpeed = 12;
    private int mDuration = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lucky = findViewById(R.id.lucky);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRunning){
                    endAnimation(2);
                }else {
                    startAnimation();
                }
                isRunning = !isRunning;
            }
        });
    }

    private void startAnimation1(){
        ViewCompat.postOnAnimation(lucky, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void startAnimation() {
        ValueAnimator accelerateAnimator = ValueAnimator.ofFloat(0,360);
        accelerateAnimator.setDuration(duration);
        accelerateAnimator.setInterpolator(new AccelerateInterpolator());

        accelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                lucky.setRotation(value);
                currentDegree = value;
                Log.d(TAG, "accelerate animator onAnimationUpdate: value is = "+value);
            }
        });
        ValueAnimator repeatAnimator = ValueAnimator.ofFloat(0,360);
        repeatAnimator.setDuration(duration/2);
        repeatAnimator.setInterpolator(new LinearInterpolator());
        repeatAnimator.setRepeatCount(ValueAnimator.INFINITE);
        repeatAnimator.setRepeatMode(ValueAnimator.RESTART);
        repeatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                lucky.setRotation(value);
                currentDegree = value;
                Log.d(TAG, "repeat onAnimationUpdate: value is = "+value);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(accelerateAnimator,repeatAnimator);
        set.start();
        runningAnimator = set;
    }

    private void endAnimation(int index){
        index = 5-index;
        runningAnimator.cancel();
        Log.e(TAG, "endAnimation:current= "+currentDegree +","+lucky.getRotation());
        float lastCircle = 360-currentDegree;
        float hitMinDegree = 60*0.2f;
        float hitMaxDegree = 60*0.8f;
        float hitDegree = (float) (hitMinDegree+(hitMaxDegree - hitMinDegree)*Math.random());
        float endDegree = lastCircle+index*60+hitDegree;
        Log.e(TAG, "endAnimation: value = "+endDegree );
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentDegree, currentDegree+endDegree);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) valueAnimator.getAnimatedValue();
                lucky.setRotation(value);
                Log.d(TAG, "end onAnimationUpdate: value is = "+value);

            }
        });
        valueAnimator.start();
    }
}