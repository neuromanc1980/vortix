package com.example.xavib.vortix;

//activitat principal del joc

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public GameState gameState;
    public GridView gridView;
    public ImageView background;
    public Ship playerShip;
    public Level level;
    private MediaPlayer effect;
    boolean newGame;
    public MediaPlayer song;
    private int length=0, highscore, score;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //creem una vista de tipus gridview
        gridView = (GridView) findViewById(R.id.grid);
        gameState = new GameState();

        //carregame el gameState, si no n'hi ha en creem un amb defaults
        Bundle extras = getIntent().getExtras();
         newGame = extras.getBoolean("new");
        if (newGame == true)    {   newGameData();                    }   else loadGameData();

        Log.d("xxx", "HIGHSCORE: " + highscore);
        Log.d("xxx", "Loaded data: level: " + gameState.getLevel().getLevel());
        Log.d("xxx", "Loaded data: imatge: " + gameState.getPlayerShip().getImatge());
        Log.d("xxx", "Loaded data: background: " + gameState.getLevel().getBackground());
        Log.d("xxx", "Loaded data: ship pos: " + gameState.getPlayerShip().getShipX() + " <= X/Z => " + gameState.getPlayerShip().getShipZ() );
        Log.d("xxx", "Loaded data: ship stats: " + gameState.getPlayerShip().getHp() + " <= HP/Shields => " + gameState.getPlayerShip().getShields() );
        Log.d("xxx", "Loaded data: start pos: " + gameState.getLevel().getStartingX() + " <= X/Z => " + gameState.getLevel().getStartingZ() );

        playerShip = gameState.getPlayerShip();
        level = gameState.getLevel();
        int hp = gameState.getPlayerShip().getHp();

        //li passem el gameState a la vista
        gridView.setMainActivity(this);
        gridView.setGameState(gameState);
        background = (ImageView) findViewById(R.id.background);
        background.setImageResource(gameState.getLevel().getBackground());


        TextView lvlmomento = (TextView) findViewById(R.id.lvlEnJuego);
        lvlmomento.setText("Level: "+gameState.getLevel().getLevel());

        TextView vida = (TextView) findViewById(R.id.vidaNave);
        vida.setText(""+hp);

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
        ed.putInt("HighScore", highscore);
        ed.putInt("Score", score);
        song.pause();
        length = song.getCurrentPosition();

        ed.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!newGame) {    loadGameData();     }
        playSong(gameState.getLevel().getLevel());

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
        highscore = gameData.getInt("HighScore", 0);
        score = gameData.getInt("Score", 0);
        gameState.setLevel(new Level(levelSaved));

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

        //nova nau
        playerShip = new Ship();
        this.score = 0;
        //nivell inicial
        this.level = new Level(1);

        playerShip.setImatge(R.drawable.ship1_s);
        playerShip.setShipX(level.getStartingX());           playerShip.setShipZ(level.getStartingZ());
        playerShip.setHp(100);             playerShip.setScanner(1);
        playerShip.setShields(100);        playerShip.setEngine(1);

        gameState.setLevel(level);
        gameState.updateShip(playerShip);


    }

    public void updateBackground(int id){
        background.setImageResource(id);
    }

    public void updateLVL(){
        int level = gameState.getLevel().getLevel();
        TextView lvlmomento = (TextView) findViewById(R.id.lvlEnJuego);
        lvlmomento.setText("Level: "+String.valueOf(level));
    }

    public void updateHPShield(){


        if (playerShip.getShields() > 0)
        {
            gameState.getPlayerShip().setShields(playerShip.getShields()-20);
        }

        if (playerShip.getShields() <= 0)
        {
            gameState.getPlayerShip().setHp(gameState.getPlayerShip().getHp()-15);
        }

        int hp = gameState.getPlayerShip().getHp();

        TextView vidamomento = (TextView) findViewById(R.id.vidaNave);
        vidamomento.setText(String.valueOf(hp));
    }

    public GameState getGameState () {   return gameState;        }

    public void playSound(int sound){        effect = MediaPlayer.create(MainActivity.this, sound); effect.setVolume(0.8f,0.8f);   effect.start();  }

    public void playSong(int level){
        switch (level) {
            case 1:               song =  MediaPlayer.create(MainActivity.this, R.raw.song2);                  break;
            case 2:               song =  MediaPlayer.create(MainActivity.this, R.raw.song3);                  break;
            case 3:               song =  MediaPlayer.create(MainActivity.this, R.raw.song4);                  break;
            case 4:               song =  MediaPlayer.create(MainActivity.this, R.raw.song5);                  break;
            case 5:               song =  MediaPlayer.create(MainActivity.this, R.raw.song6);                  break;
            case 6:               song =  MediaPlayer.create(MainActivity.this, R.raw.song7);                  break;
            case 7:               song =  MediaPlayer.create(MainActivity.this, R.raw.song8);                  break;
            case 8:               song =  MediaPlayer.create(MainActivity.this, R.raw.song9);                  break;
            case 9:               song =  MediaPlayer.create(MainActivity.this, R.raw.song10);                  break;
            case 10:              song =  MediaPlayer.create(MainActivity.this, R.raw.song11);                  break;
            case 11:              song =  MediaPlayer.create(MainActivity.this, R.raw.song12);                  break;
            case 12:              song =  MediaPlayer.create(MainActivity.this, R.raw.song13);                  break;
            case 13:              song =  MediaPlayer.create(MainActivity.this, R.raw.song14);                  break;
            case 14:              song =  MediaPlayer.create(MainActivity.this, R.raw.song15);                  break;
            case 15:              song =  MediaPlayer.create(MainActivity.this, R.raw.song1);                  break;

        }
        song.setVolume(0.3f,0.3f);
        song.seekTo(length);
        song.start();
    }

    public int getHighscore() {        return highscore;    }
    public void setHighscore(int highscore) {        this.highscore = highscore;    }

    public int getScore() {        return score;    }
    public void setScore(int score) {        this.score = score;    }
}

