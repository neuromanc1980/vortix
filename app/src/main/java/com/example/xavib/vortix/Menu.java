package com.example.xavib.vortix;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        MediaPlayer song2 = MediaPlayer.create(Menu.this, R.raw.song2);
        MediaPlayer song1 = MediaPlayer.create(Menu.this, R.raw.song1);
        song2.start();





    }

}
