package com.nerdyapps.balloonpop.utils;

/**
 * Created by mohamedkhafaga on 2/4/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreHelper {
    public int[] getScores() {
        return scores;
    }

    private int[] scores = new int[4];
    private static HighScoreHelper helper;

    private static final String PREFS_GLOBAL = "com.nerdyapps.balloonpop.prefs";
    Context context;
    SharedPreferences prefs;

    private HighScoreHelper(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_GLOBAL, Context.MODE_PRIVATE);
        for(int i=0;i<scores.length;i++){
            scores[i] = prefs.getInt("score"+i,0);
        }
    }


    public static HighScoreHelper getHighScoreHelper(Context context){
        if(helper==null){
            helper = new HighScoreHelper(context);
        }
        return helper;
    }
    public void saveScore(int newScore) {
        SharedPreferences.Editor editor = prefs.edit();
        for(int i=0;i<4;i++){
            if(newScore>scores[i]){
                scores[i]= newScore;
                break;
            }
        }
        for(int i=0;i<scores.length;i++){
            editor.putInt("score"+i,scores[i]);
        }
        editor.apply();

    }

}