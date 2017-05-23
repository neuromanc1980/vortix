package com.example.xavib.vortix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xaviB on 18/5/17.
 */

public class GameOverActivity extends AppCompatActivity {

    public Button returnButton;
    public TextView levelReached = (TextView) findViewById(R.id.levelReached);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnButton = (Button) findViewById(R.id.returnutton);
        setContentView(R.layout.gameover);
        returnButton.setOnClickListener(salir);

        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this);
        int levelSaved = gameData.getInt("Level", 1);

        levelReached.setText(""+levelSaved);

    }

    View.OnClickListener salir = new View.OnClickListener() {

        @Override
        public void onClick(View v) {



        }
    };



}


