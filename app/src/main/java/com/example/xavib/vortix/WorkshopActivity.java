package com.example.xavib.vortix;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xaviB on 17/5/17.
 */

public class WorkshopActivity extends AppCompatActivity {

    ImageView eng;
    public GameState gameState;
    public MediaPlayer song;
    public int length = 0;
    public Button engineButton, scannerButton, shieldButton, hullButton;
    public TextView engineText, engineDesc, scannerText, scannerDesc, shieldText, shieldDesc, hullText, hullDesc;
    public int engineCost, hullCost, shieldCost, scannerCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workshop);

        gameState = new GameState();

        setContentView(R.layout.workshop);

        //botons
        engineButton = (Button) findViewById(R.id.enginebtn);
        scannerButton = (Button) findViewById(R.id.scannerbtn);
        shieldButton = (Button) findViewById(R.id.shieldbtn);
        hullButton = (Button) findViewById(R.id.hullbtn);

        //text boxes
         engineText = (TextView) findViewById(R.id.enginetxt);                scannerText = (TextView) findViewById(R.id.scannertxt);
         shieldText = (TextView) findViewById(R.id.shieldtxt);                hullText = (TextView) findViewById(R.id.hulltxt);
         engineDesc = (TextView) findViewById(R.id.enginedesc);               scannerDesc = (TextView) findViewById(R.id.scannerdesc);
         shieldDesc = (TextView) findViewById(R.id.shielddesc);               hullDesc = (TextView) findViewById(R.id.hulldesc);

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
        loadStats();
    }

    @Override
    public void onPause(){
        super.onPause();
        song.stop();
        length=song.getCurrentPosition();
    }

    public void loadStats(){
        //carreguem dades
        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int shipHP = gameData.getInt("ShipHP", 100);
        int maxHp = gameData.getInt("MaxHp", 100);

        int shipShield = gameData.getInt("ShipShield", 100);
        int maxShields = gameData.getInt("MaxShields", 100);

        int shipScanner = gameData.getInt("ShipScanner", 1);
        int shipEngine = gameData.getInt("ShipEngine", 1);
        int shipCredits = gameData.getInt("Credits", 0);

        int energy = gameData.getInt("Energy", 100);
        int maxEnergy = gameData.getInt("MaxEnergy", 100);

        //costos
        scannerCost = shipScanner*250;

        hullText.setText("Upgrade plating cost: "+scannerCost);
        hullDesc.setText("This is the amount of damage your ship can take. \n Current hull points: "+shipHP+"\nMaximum hull points: "+maxHp);

        scannerText.setText("Upgrade scanner cost: "+scannerCost);
        scannerDesc.setText("How far you can detect objects in space. \n Current scanner range: "+shipScanner);

        shieldText.setText("Upgrade shields cost: "+scannerCost);
        shieldDesc.setText("Shields absorb damage and regenerate slowly. \n Current shield points: "+shipHP+"\nMaximum shield points: "+maxHp);

        engineText.setText("Upgrade engine cost: "+scannerCost);
        engineDesc.setText("Improving engine reduces energy cost of movement and increases shields regeneration. \n Current movement cost: "+(6-shipEngine)+"\nShield regeneration: "+shipEngine);




    }

}
