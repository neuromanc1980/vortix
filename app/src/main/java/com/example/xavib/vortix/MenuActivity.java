package com.example.xavib.vortix;

//activitat del menu

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    public MediaPlayer song;
    private int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        //MediaPlayer song2 = MediaPlayer.create(MenuActivity.this, R.raw.song2);
        //MediaPlayer song = MediaPlayer.create(MenuActivity.this, R.raw.song1);
        //song2.setVolume(0.3f,0.3f);
        //song.start();
    }


    @Override
    public void onResume(){
        super.onResume();
        song = MediaPlayer.create(MenuActivity.this, R.raw.song1);
        song.setVolume(0.3f,0.3f);
        song.seekTo(length);
        song.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        song.stop();
        length=song.getCurrentPosition();
    }

    public SharedPreferences obtainPreferences(){
        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this);
        return gameData;
    }

}
