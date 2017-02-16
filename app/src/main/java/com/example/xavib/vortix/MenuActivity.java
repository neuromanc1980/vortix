package com.example.xavib.vortix;

//activitat del menu

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        MediaPlayer song2 = MediaPlayer.create(MenuActivity.this, R.raw.song2);
        MediaPlayer song1 = MediaPlayer.create(MenuActivity.this, R.raw.song1);
        song2.start();





    }

}
