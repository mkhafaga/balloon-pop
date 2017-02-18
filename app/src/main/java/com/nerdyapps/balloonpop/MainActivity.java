package com.nerdyapps.balloonpop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

public class MainActivity extends AppCompatActivity {
    private Button playNow;
    private Button highScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.main_ad_view);
        adView.loadAd(new AdRequest.Builder().build());
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
            Intent highScoresIntent = new Intent(MainActivity.this,HighScores.class);
            startActivity(highScoresIntent);
        }
    });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close Game")
                .setMessage("Do you really want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
