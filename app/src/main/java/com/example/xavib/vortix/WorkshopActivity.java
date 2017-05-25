package com.example.xavib.vortix;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    public TextView engineText, engineDesc, scannerText, scannerDesc, shieldText, shieldDesc, hullText, hullDesc, money, shieldd,scannerr, hull;
    public int engineCost, hullCost, shieldCost, scannerCost, maxHp, maxShields, maxEnergy, shipHP, shipShield, shipScanner, shipEngine, shipCredits, energy;
    SharedPreferences gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workshop);

        gameData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
        eng.setImageResource(R.drawable.workshopbg);

        //superior

        money = (TextView) findViewById(R.id.money);
        shieldd = (TextView) findViewById(R.id.shieldd);
        scannerr = (TextView) findViewById(R.id.scannerr);
        hull = (TextView) findViewById(R.id.hull);


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
         shipHP = gameData.getInt("ShipHP", 100);
         maxHp = gameData.getInt("MaxHp", 100);

         shipShield = gameData.getInt("ShipShield", 100);
         maxShields = gameData.getInt("MaxShields", 100);

         shipScanner = gameData.getInt("ShipScanner", 1);
         shipEngine = gameData.getInt("ShipEngine", 1);
         shipCredits = gameData.getInt("Credits", 3);

         energy = gameData.getInt("Energy", 100);
         maxEnergy = gameData.getInt("MaxEnergy", 100);

        //costos
        scannerCost = shipEngine*1;
        engineCost = shipEngine*10;
        shieldCost = shipEngine*1;
        scannerCost = shipEngine*1;
        hullCost = shipEngine*1;

        hullText.setText("Upgrade cost: "+hullCost);
        hullDesc.setText("The damage your ship can take. \n Current points: "+shipHP+"\nMaximum points: "+maxHp);

        scannerText.setText("Upgrade cost: "+scannerCost);
        scannerDesc.setText("How far you can detect objects in space. \n Current scanner range: "+shipScanner);

        shieldText.setText("Upgrade cost: "+shieldCost);
        shieldDesc.setText("Shields absorb damage and regenerate slowly. \n Current shield points: "+shipHP+"\nMaximum shield points: "+maxHp);

        engineText.setText("Upgrade cost: "+engineCost);
        engineDesc.setText("Improving engine reduces energy cost of movement and increases shields regeneration. \n Current movement cost: "+(6-shipEngine)+"\nShield regeneration: "+shipEngine);

        money.setText("Credits: "+shipCredits);
        shieldd.setText("Shield: "+shipShield+"/"+maxShields);
        scannerr.setText("Scanner Level: "+shipScanner);
        hull.setText("HP: "+shipHP+"/"+maxHp);






        engineButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = gameData.edit();
            @Override
            public void onClick(View v) {

                if (shipCredits >= engineCost){

                    ed.putInt("ShipEngine", shipEngine + 1);
                    ed.putInt("Credits", shipCredits-engineCost);
                    engineText.setText("Engine succesfully upgraded!");
                    ed.commit();
                    loadStats();
                }

                else {

                    engineText.setText("Not enough credits! ");
                }



            }
        });

        shieldButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = gameData.edit();
            @Override
            public void onClick(View v) {

                if (shipCredits >= shieldCost){

                    ed.putInt("ShipShield", shieldCost + 1);
                    ed.putInt("Credits", shipCredits-shieldCost);
                    shieldText.setText("Shield succesfully upgraded!");
                    ed.commit();
                    loadStats();
                }

                else {

                    shieldText.setText("Not enough credits! ");
                }



            }
        });

        scannerButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = gameData.edit();
            @Override
            public void onClick(View v) {

                if (shipCredits >= scannerCost){

                    ed.putInt("ShipScanner", scannerCost + 1);
                    ed.putInt("Credits", shipCredits-scannerCost);
                    scannerText.setText("Scanner succesfully upgraded!");
                    ed.commit();
                    loadStats();
                }

                else {

                    scannerText.setText("Not enough credits! ");
                }



            }
        });

        hullButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = gameData.edit();
            @Override
            public void onClick(View v) {

                if (shipCredits >= hullCost){

                    ed.putInt("ShipHP", hullCost + 1);
                    ed.putInt("Credits", shipCredits-scannerCost);
                    hullText.setText("Hull succesfully upgraded!");
                    ed.commit();
                    loadStats();
                }

                else {

                    hullText.setText("Not enough credits! ");
                }



            }
        });





    }

}
