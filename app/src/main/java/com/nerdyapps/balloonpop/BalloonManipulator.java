package com.nerdyapps.balloonpop;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.os.SystemClock;

import com.nerdyapps.balloonpop.sprites.Balloon;
import com.nerdyapps.balloonpop.views.GameView;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by mohamedkhafaga on 2/14/17.
 */

public class BalloonManipulator extends Thread{
    private static final int MIN_ANIMATION_DURATION = 5000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    private boolean playing;
    private int level;
    private int screenWdith;
    private int screenHeight;
    private int[] colors;
    private Random generator;
    private Context context;
    private List<Balloon> balloons;

    /**
     *
     * @param context
     * @param balloons
     * @param level
     * @param screenWdith
     * @param screenHeight
     * @param playing
     */
    public BalloonManipulator(Context context, List<Balloon> balloons, int level,int screenWdith, int screenHeight, boolean playing){
        colors = new int[10];
        colors[0] = Color.argb(255, 255, 0, 0);//red.
        colors[1] = Color.argb(255, 0, 255, 0);//green.
        colors[2] = Color.argb(255, 0, 0, 255);//blue.
        colors[3] = Color.argb(255, 255, 20, 147);//deep pink.
        colors[4] = Color.argb(255, 138, 43, 226);//blue violet.
        colors[5] = Color.argb(255, 0, 255, 255);//cyan.
        colors[6] = Color.argb(255, 255, 255, 0);//yellow.
        colors[7] = Color.argb(255, 255, 165, 0);//orange.
        colors[8] = Color.argb(255, 220, 20, 60);//crimson.
        colors[9] = Color.argb(255, 47, 79, 79);// dark slate gray.
        generator = new Random();
        this.context = context;
        this.balloons =  balloons;
        this.screenWdith = screenWdith;
        this.screenHeight = screenHeight;
        this.playing = playing;
        this.level = level;
    }

    @Override
    public void run() {
        Looper.prepare();
        int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
        int minDelay = maxDelay / 2;

        int balloonsLaunched = 0;
        while (playing && balloonsLaunched < 3* level) {

//              Get a random horizontal position for the next balloon
            Random random = new Random(new Date().getTime());
            launchBalloon();
            balloonsLaunched++;

//              Wait a random number of milliseconds before looping
            int delay = random.nextInt(minDelay) + minDelay;
            SystemClock.sleep(delay);
//            try {
//                Thread.sleep(delay);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        Looper.loop();

    }

    public void launchBalloon(){
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
        Balloon balloon = new Balloon(context, screenWdith, screenHeight, colors[generator.nextInt(10)], duration);
        balloons.add(balloon);
    }
}
