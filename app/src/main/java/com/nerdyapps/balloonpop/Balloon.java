package com.nerdyapps.balloonpop;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nerdyapps.balloonpop.utils.PixelHelper;

/**
 * Created by mohamedkhafaga on 2/3/17.
 */

public class Balloon extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator animator;
    private BalloonListener balloonListener;
    private boolean popped;
    public Balloon(Context context) {
        super(context);
    }
    public Balloon(Context context,int color,int rawHeight){
        super(context);
        balloonListener = (BalloonListener) context;
        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);
        int rawWidth = rawHeight/2;
        int dpHeight = PixelHelper.pixelsToDp(rawHeight,context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth,context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth,dpHeight);
        setLayoutParams(params);
    }
    public void releaseBalloon(int screenHeight,int duration){
        animator  = new ValueAnimator();
        animator.setDuration(duration);
        animator.setFloatValues(screenHeight,0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setTarget(this);
        animator.addListener(this);
        animator.addUpdateListener(this);
        animator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(!popped){
            balloonListener.balloonPopped(this,false);
            popped =true;
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
    setY((Float) animator.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!popped && event.getAction() == MotionEvent.ACTION_DOWN){
            balloonListener.balloonPopped(this,true);
            popped = true;
            animator.cancel();
        }
        return super.onTouchEvent(event);
    }

    public void setPopped(boolean b) {
        popped =b;
        if(popped)
            animator.cancel();
    }
}
