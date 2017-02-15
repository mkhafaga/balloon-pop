package com.nerdyapps.balloonpop.sprites;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nerdyapps.balloonpop.BalloonListener;
import com.nerdyapps.balloonpop.R;
import com.nerdyapps.balloonpop.utils.PixelHelper;

import java.util.Random;

/**
 * Created by mohamedkhafaga on 2/3/17.
 */

public class Balloon {
    private final int color;
    private int x;
    private int speed;
    private Rect bounds;
    private int y;
    private Bitmap bitmap;
    private Random generator;
    private int screenWidth;
    private int screenHeight;
    private long currentAnimationTime;

    //private  ValueAnimator animator;
    public Balloon(Context context, int screenWidth,int screenHeight, int color, int level){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.balloon);
        bitmap = Bitmap.createScaledBitmap(bitmap, PixelHelper.pixelsToDp(bitmap.getWidth(), context), PixelHelper.pixelsToDp(bitmap.getHeight(),context), true);
        generator = new Random();
        x = generator.nextInt(screenWidth-bitmap.getWidth());
        y = screenHeight;
        speed = 3;// 3+generator.nextInt(level*3);
//        animator =  ValueAnimator.ofInt(screenHeight,0);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.addUpdateListener(this);
//        animator.setTarget(this);
//        animator.setDuration(duration);
        bounds = new Rect(x,y,x+bitmap.getWidth(),y+bitmap.getHeight());
        this.color  = color;
        //animator.start();
    }

    public int getColor() {
        return color;
    }


    public Rect getBounds() {
        return bounds;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void update(){
        y-=speed;
        bounds.top = y;
    }


}