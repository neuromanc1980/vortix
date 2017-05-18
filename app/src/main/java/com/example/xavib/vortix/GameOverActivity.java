package com.example.xavib.vortix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by xaviB on 18/5/17.
 */

public class GameOverActivity extends AppCompatActivity {

    public Button returnButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnButton = (Button) findViewById(R.id.returnutton);
        setContentView(R.layout.gameover);
        returnButton.setOnClickListener(salir);

    }

    View.OnClickListener salir = new View.OnClickListener() {

        @Override
        public void onClick(View v) {



        }
    };



}


