package com.nerdyapps.balloonpop;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nerdyapps.balloonpop.listeners.GameListener;
import com.nerdyapps.balloonpop.sprites.Balloon;
import com.nerdyapps.balloonpop.sprites.Pin;
import com.nerdyapps.balloonpop.utils.MusicHelper;
import com.nerdyapps.balloonpop.views.GameView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements GameListener {
    private static final int BALLOONS_PER_LEVEL = 10;
    ViewGroup contentView;
    private int colors[];
    //  private int score;
    private int pinsUsed;
    public static final int MIN_ANIMATION_DELAY = 500;
    public static final int MAX_ANIMATION_DELAY = 1500;
    public static final int MIN_ANIMATION_DURATION = 1000;
    public static final int MAX_ANIMATION_DURATION = 8000;
    private int level;
    private int currentColor, screenWidth, screenHeight;
    private TextView levelDisplay, scoreDisplay;
    private List<Pin> pins = new ArrayList<>();
    private List<Balloon> balloons = new ArrayList<>();
    private Button goButton;
    private boolean playing;
    private boolean gameStopped = true;
    private int balloonPoppedNumber;
    private MusicHelper musicHelper;
    private GameView gameView;
    private ViewGroup parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setToFullScreen();
        colors = new int[3];
        colors[0] = Color.argb(255, 255, 0, 0);
        colors[1] = Color.argb(255, 0, 255, 0);
        colors[2] = Color.argb(255, 0, 0, 255);
        parentLayout = (ViewGroup) findViewById(R.id.activity_game);
        gameView = new GameView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.toolBar);
        gameView.setLayoutParams(params);
        parentLayout.addView(gameView);
        gameView.addGameListener(this);
//
//
//        parentLayout.addView(gameView);

//        final ViewTreeObserver observer = contentView.getViewTreeObserver();
//        if (observer.isAlive()) {
//            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    screenHeight = contentView.getHeight();
//                    screenWidth = contentView.getWidth();
//                }
//            });
//        }
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setToFullScreen();
//            }
//        }); contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setToFullScreen();
//            }
//        });
        goButton = (Button) findViewById(R.id.go_button);
        //TODO
//        pins.add((ImageView) findViewById(R.id.pushpin1));
//        pins.add((ImageView) findViewById(R.id.pushpin2));
//        pins.add((ImageView) findViewById(R.id.pushpin3));
//        pins.add((ImageView) findViewById(R.id.pushpin4));
//        pins.add((ImageView) findViewById(R.id.pushpin5));
        levelDisplay = (TextView) findViewById(R.id.level_display);
        scoreDisplay = (TextView) findViewById(R.id.score_display);
        musicHelper = new MusicHelper(this);

        musicHelper.prepareMusic(this);

    }

    public void setToFullScreen() {
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.activity_game);
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
        gameView.resume();
         musicHelper.resumeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
         musicHelper.stopMusic();
        gameView.stopLevel();
    }


    public void goButtonCLickHandler(View view) {
        Button button = (Button) view;
        if (button.getText().equals(getString(R.string.play_game))) {
            gameView.startLevel();
            button.setText(R.string.pause_game);
        } else if (button.getText().equals(getString(R.string.pause_game))) {
            gameView.pause();
            button.setText(R.string.resume_game);
        } else if(button.getText().equals(getString(R.string.resume_game))){
            gameView.resume();
            button.setText(R.string.pause_game);
        }else{
            gameView.startLevel();
            button.setText(R.string.pause_game);
        }

    }


    @Override
    public void gameOver() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goButton.setText("Game Over");
            }
        });

    }

    @Override
    public void scoreUpdated(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreDisplay.setText(Integer.toString(score));
            }
        });
    }

    @Override
    public void levelUpdated(final int level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                levelDisplay.setText(Integer.toString(level));
            }
        });
    }

    @Override
    public void levelFinished(final int level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goButton.setText(String.format("Start Level %d", level + 1));
            }
        });

    }
}
