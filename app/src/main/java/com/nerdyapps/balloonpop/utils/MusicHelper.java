package com.nerdyapps.balloonpop.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;

import com.nerdyapps.balloonpop.R;

import java.io.IOException;

/**
 * Created by mohamedkhafaga on 2/4/17.
 */

public class MusicHelper {
    private MediaPlayer mediaPlayer;
    private SoundPool mSoundPool;
    private int mSoundID;
    private boolean mLoaded;
    private float mVolume;
    private int position;

    public MusicHelper(Activity activity) {

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        } else {
            //noinspection deprecation
            mSoundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });
        mSoundID = mSoundPool.load(activity, R.raw.balloon_pop, 1);
    }

    public void playSound() {
        if (mLoaded) {
            mSoundPool.play(mSoundID, mVolume, mVolume, 1, 0, 1f);
        }
    }
    public void prepareMusic(Context context){
        mediaPlayer =  MediaPlayer.create(context.getApplicationContext(), R.raw.pleasant_music);
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.setLooping(true);
    }
    public void playMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
        }
    }
    public void pauseMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            mediaPlayer.getCurrentPosition();
        }
    }

    public void stopMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            position = mediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            mediaPlayer.seekTo(position);
        }
    }

}
