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

        //botons
        //new game
        Button newGameBtn = (Button) findViewById(R.id.newGame);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //options
        Button optionsBtn = (Button) findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //continuee
        Button continueBtwn = (Button) findViewById(R.id.continueBtn);
        continueBtwn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //quit
        Button quitBtn = (Button) findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });



    }

}
