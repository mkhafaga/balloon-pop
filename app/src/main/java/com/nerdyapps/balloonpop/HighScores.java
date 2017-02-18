package com.nerdyapps.balloonpop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nerdyapps.balloonpop.utils.HighScoreHelper;

public class HighScores extends AppCompatActivity {
    private TextView[] scoreViews = new TextView[4];
    private int[] scores = new int[4];
    private HighScoreHelper highScoreHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.highscore_ad_view);
        adView.loadAd(new AdRequest.Builder().build());
        scoreViews[0] = (TextView) findViewById(R.id.scoreView1);
        scoreViews[1] = (TextView) findViewById(R.id.scoreView2);
        scoreViews[2] = (TextView) findViewById(R.id.scoreView3);
        scoreViews[3] = (TextView) findViewById(R.id.scoreView4);
        highScoreHelper = HighScoreHelper.getHighScoreHelper(this.getApplicationContext());
        scores =  highScoreHelper.getScores();
        for(int i=0;i<scores.length;i++){
            scoreViews[i].setText(scores[i]+"");
        }

    }


}
