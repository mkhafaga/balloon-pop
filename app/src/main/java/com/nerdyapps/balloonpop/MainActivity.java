package com.nerdyapps.balloonpop;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdyapps.balloonpop.utils.HighScoreHelper;
import com.nerdyapps.balloonpop.utils.MusicHelper;
import com.nerdyapps.balloonpop.utils.SimpleAlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements BalloonListener {
    private static final int BALLOONS_PER_LEVEL = 10;
    ViewGroup contentView;
    private int colors[];
    private int score;
    private int pinsUsed;
    public static final int MIN_ANIMATION_DELAY = 500;
    public static final int MAX_ANIMATION_DELAY = 1500;
    public static final int MIN_ANIMATION_DURATION = 1000;
    public static final int MAX_ANIMATION_DURATION = 8000;
    private int level;
    private int currentColor, screenWidth, screenHeight;
    private TextView levelDisplay, scoreDisplay;
    private List<ImageView> pins = new ArrayList<>();
    private List<Balloon> balloons = new ArrayList<>();
    private Button goButton;
    private boolean playing;
    private boolean gameStopped = true;
    private int balloonPoppedNumber;
    private MusicHelper musicHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors = new int[3];
        colors[0] = Color.argb(255, 255, 0, 0);
        colors[1] = Color.argb(255, 0, 255, 0);
        colors[2] = Color.argb(255, 0, 0, 255);

        getWindow().setBackgroundDrawableResource(R.drawable.modern_background);
        contentView = (ViewGroup) findViewById(R.id.activity_main);
        setToFullScreen();
        final ViewTreeObserver observer = contentView.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenHeight = contentView.getHeight();
                    screenWidth = contentView.getWidth();
                }
            });
        }
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });
        goButton = (Button) findViewById(R.id.go_button);
        pins.add((ImageView) findViewById(R.id.pushpin1));
        pins.add((ImageView) findViewById(R.id.pushpin2));
        pins.add((ImageView) findViewById(R.id.pushpin3));
        pins.add((ImageView) findViewById(R.id.pushpin4));
        pins.add((ImageView) findViewById(R.id.pushpin5));
        levelDisplay = (TextView) findViewById(R.id.level_display);
        scoreDisplay = (TextView) findViewById(R.id.score_display);
        updateDisplay();
        musicHelper = new MusicHelper(this);

        musicHelper.prepareMusic(this);

    }

    public void setToFullScreen() {
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.activity_main);
        rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
        musicHelper.playMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicHelper.stopMusic();

    }

    public void startGame() {
        setToFullScreen();
        score = 0;
        level = 0;
        pinsUsed = 0;
        for (ImageView pin : pins) {
            pin.setImageResource(R.drawable.pin);
        }
        gameStopped = false;
        startLevel();
        musicHelper.playMusic();
    }

    public void startLevel() {
        balloonPoppedNumber = 0;
        level++;
        updateDisplay();
        BalloonLauncher launcher = new BalloonLauncher();
        launcher.execute(level);
        playing = true;
        goButton.setText("Stop game");
    }

    public void finishLevel() {
        Toast.makeText(this, String.format("You finished level %d", level), Toast.LENGTH_SHORT).show();
        playing = false;
        goButton.setText(String.format("Start level %d", level + 1));
    }

    public void goButtonCLickHandler(View view) {
        if (playing) {
            gameOver(false);
        } else if (gameStopped) {
            startGame();
        } else {
            startLevel();
        }

    }

    @Override
    public void balloonPopped(Balloon b, boolean userTouch) {
        balloonPoppedNumber++;
        musicHelper.playSound();
        contentView.removeView(b);
        balloons.remove(b);
        if (userTouch) {
            score++;

        } else {
            pinsUsed++;
            if (pinsUsed <= pins.size()) {
                pins.get(pinsUsed - 1).setImageResource(R.drawable.pin_off);
            }
            if (pinsUsed == pins.size()) {
                gameOver(true);
                return;
            } else {
                Toast.makeText(this, "You missed this one!", Toast.LENGTH_SHORT).show();
            }
        }

        updateDisplay();
        if (balloonPoppedNumber == BALLOONS_PER_LEVEL*level) {
            finishLevel();
        }
    }

    private void gameOver(boolean allPinsUsed) {
        Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
        musicHelper.pauseMusic();
        for (Balloon balloon : balloons
                ) {
            contentView.removeView(balloon);
            balloon.setPopped(true);
        }
        balloons.clear();
        playing = false;
        goButton.setText("Start game");
        gameStopped = true;
        if (allPinsUsed) {
            if (HighScoreHelper.isTopScore(this, score)) {
                HighScoreHelper.setTopScore(this, score);
                SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance("New High Score", String.format("Your new score is %d", score));
                alertDialog.show(getSupportFragmentManager(), null);
            }
        }
    }

    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {

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
            while (playing && balloonsLaunched < BALLOONS_PER_LEVEL*level) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(screenWidth - 200);
                publishProgress(xPosition);
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Balloon balloon = new Balloon(this, colors[currentColor], 150);
        balloons.add(balloon);
        if (currentColor + 1 == colors.length) {
            currentColor = 0;
        } else {
            currentColor++;
        }

//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x);
        balloon.setY(screenHeight + balloon.getHeight());
        contentView.addView(balloon);

//      Let 'er fly
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
        balloon.releaseBalloon(screenHeight, duration);

    }

    public void updateDisplay() {
        levelDisplay.setText(Integer.toString(level));
        scoreDisplay.setText(Integer.toString(score));
    }
}
