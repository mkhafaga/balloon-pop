package com.nerdyapps.balloonpop.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.nerdyapps.balloonpop.BalloonListener;
import com.nerdyapps.balloonpop.R;
import com.nerdyapps.balloonpop.listeners.GameListener;
import com.nerdyapps.balloonpop.listeners.GameStatePublisher;
import com.nerdyapps.balloonpop.sprites.Balloon;
import com.nerdyapps.balloonpop.utils.MusicHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by mohamedkhafaga on 2/11/17.
 */

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private static final int MIN_ANIMATION_DURATION = 5000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;
    private long timeElapsed;
    private Thread gameThread;
    private volatile boolean playing = true;
    private int width;
    private int launchedBalloons;
    private int balloonsForCurrentLevel;
    private int height;
    // private Bitmap background;
    private List<Balloon> balloons;
    //private Iterator<Balloon> balloonsIterator;
    private int level;
    private Context context;
    private int usedPins;
    private volatile boolean gameOver;
    private int score;
    private Random generator;
    private int[] colors;
    private MusicHelper musicHelper;
    private volatile boolean paused;
    private GameStatePublisher gameStatePublisher = new GameStatePublisher();

    public GameView(Context context) {
        super(context);
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
        getHolder().addCallback(this);
        this.context = context;
        // background = BitmapFactory.decodeResource(context.getResources(), R.drawable.modern_background);
        ///  setBackgroundResource(R.drawable.modern_background);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = getHeight(); //height is ready
                width = getWidth();
                balloons = new ArrayList<Balloon>();


            }
        });
        musicHelper = new MusicHelper((Activity) context);
    }

    @Override
    public void run() {
        while (playing) {
            //launchBalloons(timeElapsed);
            //timeElapsed=System.currentTimeMillis();
            update();
            draw();
            control();
            // timeElapsed =  System.currentTimeMillis()-timeElapsed;

        }

    }

//    private void launchBalloons(long timeElapsed) {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                //   if(timeElapsed>2000 && launchedBalloons<=3){
//               // Log.d("Time!",timeElapsed+"");
//                int duration = Math.min(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
//                Balloon balloon = new Balloon(GameView.this.context, width, height, colors[generator.nextInt(10)],duration);
//                balloons.add(balloon);
//                launchedBalloons++;
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        //  }
//    }

    private void update() {

        if (balloons.isEmpty() && !gameOver) {
            gameStatePublisher.fireLevelFinished(level);
        }
//            for (int i=0;i<balloons.size();i++)
//                balloons.get(i).update();

        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon balloon = balloons.get(i);
            Log.d("Top!",balloon.getBounds().top+"");
            if (balloon!=null && balloon.getBounds().top <= 0) {
                balloons.remove(i);
                musicHelper.playSound();
                usedPins++;
            }
        }
       // Log.d("PINS!", usedPins + "");
        if (usedPins == 5) {
            // ((Button)((Activity)context).findViewById(R.id.go_button)).setText("Game Over");
            playing = false;
            gameOver = true;
            gameStatePublisher.fireGameOver();
        }


    }


    public void draw() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        //Log.d("BEW!","X: "+balloon.getX()+" - Y: "+balloon.getY());
        // canvas.drawBitmap(background, 0, 0, null);
        Paint paint = new Paint();
        paint.setTextSize(50);
        //paint.setTextAlign(Paint.Align.CENTER);

        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon balloon = balloons.get(i);
            paint.setColorFilter(new LightingColorFilter(balloon.getColor(), 0));
            canvas.drawBitmap(balloon.getBitmap(), balloon.getX(), balloon.getY(), paint);
        }

        if (gameOver) {
            paint = new Paint();
            paint.setTextSize(150);
            paint.setTextAlign(Paint.Align.CENTER);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startLevel() {
        playing = true;
        level++;
        Log.d("Level!",level+"");
        BalloonLauncher launcher = new BalloonLauncher();
        launcher.execute(level);
        //int balloonsLimit =
//        for (int i = 0; i < 3; i++) {
//            Log.d("BOUNDS!:", "Width: " + width + " - Height: " + height);
//            int duration = Math.min(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
//            Balloon balloon = new Balloon(GameView.this.context, width, height, colors[generator.nextInt(10)],duration);
//            //Log.d("BALLOON!", i + " - X: " + balloon.getX() + " - Y: " + balloon.getY() + " Speed: " + balloon.getSpeed());
//            balloons.add(balloon);
////                    try {
////                        Thread.sleep(100);
////
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//        }
        gameThread = new Thread(this);
        gameThread.start();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 3; i++) {
//                    Log.d("BOUNDS!:", "Width: " + width + " - Height: " + height);
//                    int duration = Math.min(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
//                    Balloon balloon = new Balloon(GameView.this.context, width, height, colors[generator.nextInt(10)],duration);
//                    //Log.d("BALLOON!", i + " - X: " + balloon.getX() + " - Y: " + balloon.getY() + " Speed: " + balloon.getSpeed());
//                    balloons.add(balloon);
////                    try {
////                        Thread.sleep(100);
////
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }
//        },100);


    }

    public void resume() {
        if (paused) {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
            paused = false;
        }

    }

    public void pause() {
        paused = true;
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopLevel() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        // Log.d("BEW!","X: "+balloon.getX()+" - Y: "+balloon.getY());
        //canvas.drawBitmap(background, 0, 0, null);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon balloon = balloons.get(i);
            if (balloon.getBounds().intersect(new Rect(x - 2, y - 2, x + 2, y + 2))) {
                balloons.remove(i);
                musicHelper.playSound();
                score++;
                gameStatePublisher.fireScoreUpdated(score);
                break;
            }
        }
        return true;
    }

    public void addGameListener(GameListener gameListener) {
        gameStatePublisher.addGameListener(gameListener);
    }

    private class BalloonLauncher extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                    (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (playing && balloonsLaunched < 1 * level) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                publishProgress();
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
            Balloon balloon = new Balloon(GameView.this.context, width, height, colors[generator.nextInt(10)], duration);
            balloons.add(balloon);
        }


    }

}
