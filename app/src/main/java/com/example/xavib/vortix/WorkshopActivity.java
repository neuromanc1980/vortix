package com.example.xavib.vortix;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xaviB on 17/5/17.
 */

public class WorkshopActivity extends AppCompatActivity {

    ImageView eng;
    public MediaPlayer song;
    private int length = 0;
    private Button engineButton, scannerButton, shieldButton, hullButton;
    private TextView engineText, engineDesc, scannerText, scannerDesc, shieldText, shieldDesc, hullText, hullDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workshop);

        //botons
        engineButton = (Button) findViewById(R.id.enginebtn);
        scannerButton = (Button) findViewById(R.id.scannerbtn);
        shieldButton = (Button) findViewById(R.id.shieldbtn);
        hullButton = (Button) findViewById(R.id.hullbtn);

        //text boxes
        TextView engineText = (TextView) findViewById(R.id.enginetxt);               TextView scannerText = (TextView) findViewById(R.id.scannertxt);
        TextView shieldText = (TextView) findViewById(R.id.shieldtxt);               TextView hullText = (TextView) findViewById(R.id.hulltxt);
        TextView engineDesc = (TextView) findViewById(R.id.enginedesc);              TextView scannerDesc = (TextView) findViewById(R.id.scannerdesc);
        TextView shieldDesc = (TextView) findViewById(R.id.shielddesc);              TextView hullDesc = (TextView) findViewById(R.id.hulldesc);

        //background
        eng = (ImageView) findViewById(R.id.workshopbg);
        eng.setImageResource(R.drawable.shipbgengine);

    }

    @Override
    public void onResume(){
        super.onResume();
        song = MediaPlayer.create(WorkshopActivity.this, R.raw.cosmos);
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
