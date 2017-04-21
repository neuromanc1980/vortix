package com.example.xavib.vortix;

//activitat principal del joc

import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public GameState gameState;
    public GridView gridView;
    public ImageView background;
    public Ship playerShip;
    public Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //creem una vista de tipus gridview
        gridView = (GridView) findViewById(R.id.grid);
        gameState = new GameState();

        //carregame el gameState, si no n'hi ha en creem un amb defaults
        Bundle extras = getIntent().getExtras();
        boolean newGame = extras.getBoolean("new");
        if (newGame == true)    {   newGameData();                    }   else loadGameData();


        Log.d("xxx", "Loaded data: level: " + gameState.getLevel().getLevel());
        Log.d("xxx", "Loaded data: imatge: " + gameState.getPlayerShip().getImatge());
        Log.d("xxx", "Loaded data: background: " + gameState.getLevel().getBackground());
        Log.d("xxx", "Loaded data: ship pos: " + gameState.getPlayerShip().getShipX() + " <= X/Z => " + gameState.getPlayerShip().getShipZ() );
        Log.d("xxx", "Loaded data: ship stats: " + gameState.getPlayerShip().getHp() + " <= HP/Shields => " + gameState.getPlayerShip().getShields() );
        Log.d("xxx", "Loaded data: start pos: " + gameState.getLevel().getStartingX() + " <= X/Z => " + gameState.getLevel().getStartingZ() );

        playerShip = gameState.getPlayerShip();
        level = gameState.getLevel();

        //li passem el gameState a la vista
        gridView.setGameState(gameState);
        gridView.setMainActivity(this);
        background = (ImageView) findViewById(R.id.background);
        background.setImageResource(gameState.getLevel().getBackground());
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = prefs.edit();
        playerShip = gameState.getPlayerShip();
        level = gameState.getLevel();
        ed.putInt("ShipX", playerShip.getShipX());
        ed.putInt("ShipZ", playerShip.getShipZ());
        ed.putInt("ShipHP", playerShip.getHp());
        ed.putInt("ShipScanner", playerShip.getScanner());
        ed.putInt("ShipShield", playerShip.getShields());
        ed.putInt("ShipEngine", playerShip.getEngine());
        ed.putInt("Imatge", playerShip.getImatge());
        ed.putInt("Level", level.getLevel());

        ed.commit();

    }

    @Override
    public void onResume(){
        super.onResume();
        loadGameData();
        //updateBackground();
    }

    public void loadGameData(){
        //carreguem dades
        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this);
        int shipX = gameData.getInt("ShipX", gameState.getLevel().getStartingX());
        int shipZ = gameData.getInt("ShipZ", gameState.getLevel().getStartingZ());
        int shipHP = gameData.getInt("ShipHP", 100);
        int shipShield = gameData.getInt("ShipShield", 100);
        int shipScanner = gameData.getInt("ShipScanner", 1);
        int shipEngine = gameData.getInt("ShipEngine", 1);
        int shipImatge = gameData.getInt("Imatge", (R.drawable.ship1_s));
        int levelSaved = gameData.getInt("Level", 1);
        gameState.updateLevel(new Level(levelSaved));

        //reconstruim la nau en funció a les dades que carreguem, si no existia creem una de nova
        if (playerShip==null){            playerShip = new Ship();        }

        playerShip.setImatge(shipImatge);     playerShip.setEngine(shipEngine);
        playerShip.setShipX(shipX);           playerShip.setShipZ(shipZ);
        playerShip.setHp(shipHP);             playerShip.setScanner(shipScanner);
        playerShip.setShields(shipShield);

        //construim el nivell en funció a les dades que carreguem
        this.level = new Level(levelSaved);
        gameState.setLevel(level);
        gameState.updateShip(playerShip);

    }

    public void newGameData(){
        //gnerem dades inicials
        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this);
        int shipX = gameData.getInt("", gameState.getLevel().getStartingX());
        int shipZ = gameData.getInt("", gameState.getLevel().getStartingZ());
        int shipHP = gameData.getInt("", 100);
        int shipShield = gameData.getInt("", 100);
        int shipScanner = gameData.getInt("", 1);
        int shipEngine = gameData.getInt("", 1);
        int shipImatge = gameData.getInt("", (R.drawable.ship1_s));
        int levelSaved = gameData.getInt("", 1);
        gameState.updateLevel(new Level(levelSaved));

        //nova nau
        if (playerShip==null){            playerShip = new Ship();           }

        playerShip.setImatge(shipImatge);     playerShip.setEngine(shipEngine);
        playerShip.setShipX(shipX);           playerShip.setShipZ(shipZ);
        playerShip.setHp(shipHP);             playerShip.setScanner(shipScanner);
        playerShip.setShields(shipShield);

        //nivell inicial
        this.level = new Level(1);
        gameState.setLevel(level);
        gameState.updateShip(playerShip);
    }

    public void updateBackground(int id){
        background.setImageResource(id);
    }

}
