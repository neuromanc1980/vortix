package com.example.xavib.vortix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by xaviB on 18/5/17.
 */

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);


    }

    View.OnClickListener derecha = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener izquierda = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


        }

    };

}


