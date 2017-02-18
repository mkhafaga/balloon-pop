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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nerdyapps.balloonpop.R;
import com.nerdyapps.balloonpop.listeners.GameListener;
import com.nerdyapps.balloonpop.listeners.GameStatePublisher;
import com.nerdyapps.balloonpop.sprites.Balloon;
import com.nerdyapps.balloonpop.sprites.Pin;
import com.nerdyapps.balloonpop.states.State;
import com.nerdyapps.balloonpop.utils.HighScoreHelper;
import com.nerdyapps.balloonpop.utils.MusicHelper;
import com.nerdyapps.balloonpop.utils.PixelHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mohamedkhafaga on 2/11/17.
 */

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private boolean newGame;
    private Thread gameThread;
    private volatile boolean playing = true;
    private int width;
    private int launchedBalloons;
    private int balloonsForCurrentLevel;
    private int height;
    private List<Balloon> balloons;
    private int level;
    private Context context;
    private int usedPins;
    private volatile boolean gameOver;
    private int score;
    private Random generator;
    private int[] colors;
    private boolean forcedPause;
    private MusicHelper musicHelper;
    private volatile boolean paused;
    private long now;
    private long before;
    private Bitmap background;
    private List<Pin> pins;
    private GameStatePublisher gameStatePublisher = new GameStatePublisher();
    private HighScoreHelper highScoreHelper;

    public GameView(Context context) {
        super(context);
        //interstitialAd.setAdUnitId(context.getString(R.string.balloon_pop_interstitial_ad_unit_id));
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
        pins = new ArrayList<>();

        getHolder().addCallback(this);
        this.context = context;
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_background);
      //  background = Bitmap.createScaledBitmap(background, PixelHelper.pixelsToDp(background.getWidth(), context), PixelHelper.pixelsToDp(background.getHeight(),context), true);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = getHeight(); //height is ready
                width = getWidth();
                int pinX = width;
                int pinY = 5;

//                for (int i = 0; i < 5; i++) {
//                    pins.add(new Pin(GameView.this.context, i));
//                }


            }
        });
        balloons = new ArrayList<Balloon>();
        musicHelper = new MusicHelper((Activity) context);
        newGame = true;

        highScoreHelper = HighScoreHelper.getHighScoreHelper(this.context.getApplicationContext());

    }


    @Override
    public void run() {
        before = System.currentTimeMillis();
        while (playing) {
            LaunchBalloons();
            update();
            draw();
            control();

        }

    }

    private void LaunchBalloons() {
        now = System.currentTimeMillis();
        if (now - before >= (350 + generator.nextInt(1000)) && launchedBalloons < balloonsForCurrentLevel) {
            before = now;
            Balloon balloon = new Balloon(context, width, height, colors[generator.nextInt(10)], level);
            balloons.add(balloon);
            launchedBalloons++;
        }
    }

    private void update() {

        if (launchedBalloons == balloonsForCurrentLevel && balloons.isEmpty() && !gameOver) {
            gameStatePublisher.fireLevelFinished(level);
            playing = false;
        }
        for (int i = 0; i < balloons.size(); i++)
            balloons.get(i).update();

        for (int i = balloons.size() - 1; i >= 0; i--) {
            if(!balloons.isEmpty()){
                Balloon balloon = balloons.get(i);
                if (balloon != null && balloon.getBounds().top <= 0) {
                    balloons.remove(i);
                    musicHelper.playSound();
                    usedPins++;
                    if (!pins.isEmpty())
                        pins.remove(pins.size() - 1);
                }
            }

        }
        if (usedPins == 5) {
            playing = false;
            gameOver = true;
            gameStatePublisher.fireGameOver();
            highScoreHelper.saveScore(score);

        }


    }

    public void startGame() {
        level = 0;
        score = 0;
        gameOver = false;
        gameStatePublisher.fireScoreUpdated(0);
        gameStatePublisher.fireLevelUpdated(1);
        usedPins = 0;
        for(int i=0;i<5;i++){
            pins.add(new Pin(this.context,i));
        }
        if (!balloons.isEmpty())
            balloons.clear();
        startLevel();
    }


    public boolean draw() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            background = Bitmap.createScaledBitmap(background,canvas.getWidth(),canvas.getHeight(),true);
            canvas.drawBitmap(background, 0, 0, null);
            Paint paint = new Paint();
            paint.setTextSize(50);
            for (int i = balloons.size() - 1; i >= 0; i--) {
                Balloon balloon = balloons.get(i);
                paint.setColorFilter(new LightingColorFilter(balloon.getColor(), 0));
                canvas.drawBitmap(balloon.getBitmap(), balloon.getX(), balloon.getY(), paint);
            }
            //draw pins
            for (int i = 0; i < pins.size(); i++) {
                pins.get(i).draw(canvas);
            }

            if (gameOver) {
                paint = new Paint();
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
            }
            holder.unlockCanvasAndPost(canvas);
            return true;
        } else {
            return false;
        }

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
        Log.d("Level!", level + "");
        gameStatePublisher.fireLevelUpdated(level);
        balloonsForCurrentLevel = level * 10;
        launchedBalloons = 0;
        Log.d("B!", balloonsForCurrentLevel + "");
        gameThread = new Thread(this);
        gameThread.start();

    }

    public void resume(State state) {
        if ((state == State.NORMAL && paused) || (state == State.FORCED && !paused)) {

            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
            paused = false;


        }


    }

    public void pause(State state) {
        playing = false;
        if (state == State.NORMAL) {
            paused = true;
        } else {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        Log.d("surface!", "created!");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("surface!", "destroyed!");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if(playing){
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
       }
        return true;
    }

    public void addGameListener(GameListener gameListener) {
        gameStatePublisher.addGameListener(gameListener);
    }

}
