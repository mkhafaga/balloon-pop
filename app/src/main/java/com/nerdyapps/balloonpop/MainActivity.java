package com.nerdyapps.balloonpop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button playNow;
    private Button highScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playNow = (Button) findViewById(R.id.play_now_button);
        highScores = (Button) findViewById(R.id.high_scores_button);
        playNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(gameIntent);
            }
        });
    highScores.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent highScoresIntent = new Intent(MainActivity.this,GameActivity.class);
            startActivity(highScoresIntent);
        }
    });
    }

}
