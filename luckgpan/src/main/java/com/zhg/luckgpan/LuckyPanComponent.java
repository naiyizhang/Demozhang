package com.zhg.luckgpan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

public class LuckyPanComponent {

    private Context mContext;
    private ConstraintLayout mView;

    private long mDuration = 1000;

    private float hitOffsetRatio = 0.1f;

    private int mPrizeCount = 6;

    private float mCurrentDegree;

    private Animator mRunningAnimator;


    private void createView() {
        ConstraintLayout constraintLayout = new ConstraintLayout(mContext);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        Space center = new Space(mContext);
        center.setId(ViewCompat.generateViewId());
        center.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        set.connect(center.getId(), ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP);
        set.connect(center.getId(), ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM);
        set.connect(center.getId(), ConstraintSet.LEFT, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.LEFT);
        set.connect(center.getId(), ConstraintSet.RIGHT, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.RIGHT);

        set.applyTo(constraintLayout);
    }

    private void luckyStart() {
        cancelRunningAnimatorIfNeed();
        ValueAnimator accelerateAnimator = ValueAnimator.ofFloat(0, 360);
        accelerateAnimator.setDuration(mDuration);
        accelerateAnimator.setInterpolator(new AccelerateInterpolator());
        accelerateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                mView.setRotation(value);
                mCurrentDegree = value;
            }
        });
        ValueAnimator repeatAnimator = ValueAnimator.ofFloat(0, 360);
        repeatAnimator.setDuration(mDuration / 2);
        repeatAnimator.setInterpolator(new LinearInterpolator());
        repeatAnimator.setRepeatCount(ValueAnimator.INFINITE);
        repeatAnimator.setRepeatMode(ValueAnimator.RESTART);
        repeatAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                mView.setRotation(value);
                mCurrentDegree = value;
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(accelerateAnimator, repeatAnimator);
        set.start();
        mRunningAnimator = set;
    }

    private void luckEnd(int reverseIndex) {
        cancelRunningAnimatorIfNeed();
        reverseIndex = mPrizeCount - 1 - reverseIndex;
        float lastLapDegree = 360 - mCurrentDegree;//最后一圈剩余的角度
        float prizeDegree = 360f / mPrizeCount;
        float hitStartDegree = prizeDegree * hitOffsetRatio;
        float hitEndDegree = prizeDegree * (1 - hitOffsetRatio);
        float hitRandomDegree = (float) (hitStartDegree + (hitEndDegree - hitStartDegree) * Math.random());
        float rotationDegree = lastLapDegree + reverseIndex * 60 + hitRandomDegree;
        ValueAnimator endAnimator = ValueAnimator.ofFloat(mCurrentDegree, mCurrentDegree + rotationDegree);
        endAnimator.setInterpolator(new DecelerateInterpolator());
        endAnimator.setDuration((long) (mDuration * rotationDegree / 360f));
        endAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mView.setRotation(value);
            }
        });
        endAnimator.start();
        mRunningAnimator = endAnimator;
    }

    private void cancelRunningAnimatorIfNeed() {
        if (mRunningAnimator != null && mRunningAnimator.isRunning()) {
            mRunningAnimator.cancel();
        }
    }
}
