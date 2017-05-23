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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        rl = (ImageView) findViewById(R.id.imageView);
        rl.setImageResource(R.drawable.instrucciones);

        nextIns = (Button) findViewById(R.id.derecha);
        backIns = (Button) findViewById(R.id.izquierda);


        nextIns.setOnClickListener(derecha);
        backIns.setOnClickListener(izquierda);

    }

        View.OnClickListener derecha = new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentImage++;
                if(currentImage == 0){
                    rl.setImageResource(R.drawable.instrucciones);

                }
                else if(currentImage == 1){
                rl.setImageResource(R.drawable.instrucciones2);

                }
                else if (currentImage == 2){
                rl.setImageResource(R.drawable.instrucciones3);

                }
                else if (currentImage == 3){
                    rl.setImageResource(R.drawable.instrucciones4);
                }
                if (currentImage>3){
                    currentImage = 3;
                }
            }
        };

    View.OnClickListener izquierda = new OnClickListener() {

        public void onClick(View v) {
            currentImage--;
            if(currentImage == 0){
                rl.setImageResource(R.drawable.instrucciones);

            }
            else if(currentImage == 1){
                rl.setImageResource(R.drawable.instrucciones2);

            }
            else if (currentImage == 2){
                rl.setImageResource(R.drawable.instrucciones3);

            }
            else if (currentImage == 3){
                rl.setImageResource(R.drawable.instrucciones4);

            }
            if (currentImage<0){
                currentImage = 0;
            }

        }

    };

    }
