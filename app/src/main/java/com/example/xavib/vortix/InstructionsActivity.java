package com.example.xavib.vortix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by alex on 29/3/17.
 */

public class InstructionsActivity extends Activity{


    public Button nextIns,backIns;

    ImageView rl;
    private int currentImage = 0;
    int[] images = { R.drawable.lvl1, R.drawable.lvl2, };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        rl = (ImageView) findViewById(R.id.imageView);
        rl.setBackgroundResource(R.drawable.ship1);

        nextIns = (Button) findViewById(R.id.derecha);
        backIns = (Button) findViewById(R.id.izquierda);


        nextIns.setOnClickListener(derecha);
        backIns.setOnClickListener(izquierda);

    }

        View.OnClickListener derecha = new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentImage++;
                currentImage = currentImage % images.length;
               rl.setBackgroundResource(images[currentImage]);

            }
        };

    View.OnClickListener izquierda = new OnClickListener() {

        public void onClick(View v) {
            currentImage--;
            currentImage = (currentImage + images.length) % images.length;
            rl.setBackgroundResource(images[currentImage]);

        }

    };

    }
