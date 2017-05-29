package com.example.xavib.vortix;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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
    public MediaPlayer song;
    public int length = 0;

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
                else if (currentImage == 4){
                    rl.setImageResource(R.drawable.instrucciones5);
                }
                if (currentImage>4){
                    currentImage = 4;
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
            else if (currentImage == 4){
                    rl.setImageResource(R.drawable.instrucciones5);
                }
            if (currentImage<0){
                    currentImage = 0;
            }

        }

    };

    @Override
    public void onResume(){
        super.onResume();
        song = MediaPlayer.create(InstructionsActivity.this, R.raw.cosmos);
        song.setVolume(0.3f,0.3f);
        song.seekTo(length);
        song.setLooping(true);
        song.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        song.stop();
        length=song.getCurrentPosition();
    }

}
