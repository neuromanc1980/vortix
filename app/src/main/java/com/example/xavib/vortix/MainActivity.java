package com.example.xavib.vortix;

//activitat principal del joc

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private int length=0, highscore, score, credits, maxEnergy, maxHp, maxShields, shieldRegen;
    TextView mensaje;
    public Button toWorkShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toWorkShop = (Button)findViewById(R.id.toWorkShop);

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

        //li passem el gameState a la vista
        gridView.setMainActivity(this);
        gridView.setGameState(gameState);
        background = (ImageView) findViewById(R.id.background);
        background.setImageResource(gameState.getLevel().getBackground());
        mensaje = (TextView) findViewById(R.id.info);

        //refreshStats();
        highscore = gameState.getLevel().getLevel();


    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = prefs.edit();
        playerShip = gameState.getPlayerShip();
        level = gameState.getLevel();
        highscore = gameState.getLevel().getLevel();
        credits = gameState.getPlayerShip().getCredits();
        ed.putInt("ShipX", playerShip.getShipX());
        ed.putInt("ShipZ", playerShip.getShipZ());
        ed.putInt("ShipHP", playerShip.getHp());
        ed.putInt("ShipScanner", playerShip.getScanner());
        ed.putInt("ShipShield", playerShip.getShields());
        ed.putInt("ShipEngine", playerShip.getEngine());
        ed.putInt("Imatge", playerShip.getImatge());
        ed.putInt("Level", level.getLevel());
        int maxScore = prefs.getInt("HighScore", 0);
        if (maxScore < highscore)   {      ed.putInt("HighScore", 5);             }
        ed.putInt("Score", score);
        ed.putInt("Credits", credits);
        ed.putInt("MaxEnergy", playerShip.getMaxenergy());
        ed.putInt("MaxShields", playerShip.getMaxshields());
        ed.putInt("MaxHp", playerShip.getMaxhp());
        ed.putInt("Energy", playerShip.getEnergy());
        ed.putInt("ShieldRegen", playerShip.getShieldregen());
        song.pause();
        length = song.getCurrentPosition();

        ed.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!newGame) {    loadGameData();     }
        playSong(gameState.getLevel().getLevel());
        //refreshStats();

    }

    public void loadGameData(){

        //carreguem dades
        SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int shipX = gameData.getInt("ShipX", gameState.getLevel().getStartingX());
        int shipZ = gameData.getInt("ShipZ", gameState.getLevel().getStartingZ());
        int shipHP = gameData.getInt("ShipHP", 100);
        int shipShield = gameData.getInt("ShipShield", 100);
        int shipScanner = gameData.getInt("ShipScanner", 1);
        int shipEngine = gameData.getInt("ShipEngine", 1);
        int levelSaved = gameData.getInt("Level", 1);
        int shipImatge;
        if (levelSaved <16){
             shipImatge = gameData.getInt("Imatge", (R.drawable.ship1_s));
        }   else {     shipImatge = gameData.getInt("Imatge", (R.drawable.ship2_2));     }


        int shipCredits = gameData.getInt("Credits", 0);
        int maxEnergy = gameData.getInt("MaxEnergy", 100);
        int energy = gameData.getInt("Energy", 100);
        int maxShields = gameData.getInt("MaxShields", 100);
        int maxhp = gameData.getInt("MaxHp", 100);
        int shieldRegen = gameData.getInt("ShieldRegen", 1);
        highscore = gameData.getInt("HighScore", 0);
        score = gameData.getInt("Score", 0);
        gameState.setLevel(new Level(levelSaved));

        //reconstruim la nau en funció a les dades que carreguem, si no existia creem una de nova
        if (playerShip==null){            playerShip = new Ship();        }

        playerShip.setImatge(shipImatge);     playerShip.setEngine(shipEngine);
        playerShip.setShipX(shipX);           playerShip.setShipZ(shipZ);
        playerShip.setHp(shipHP);             playerShip.setScanner(shipScanner);
        playerShip.setShields(shipShield);    playerShip.setCredits(shipCredits);
        playerShip.setMaxenergy(maxEnergy);   playerShip.setMaxhp(maxhp);
        playerShip.setMaxshields(maxShields); playerShip.setEnergy(energy);
        playerShip.setShieldregen(shieldRegen);

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
        playerShip.setCredits(0);          playerShip.setMaxenergy(100);
        playerShip.setMaxshields(100);     playerShip.setMaxhp(100);
        playerShip.setEnergy(100);         playerShip.setShieldregen(1);

        gameState.setLevel(level);
        gameState.updateShip(playerShip);


    }

    public void updateBackground(int id){
        background.setImageResource(id);
    }

    public void updateHPShield(int damage){

        if (gameState.getPlayerShip().getShields() > damage)
        {            gameState.getPlayerShip().setShields(playerShip.getShields()-damage);        }

        if (gameState.getPlayerShip().getShields() <= damage)
        {
            gameState.getPlayerShip().setHp(gameState.getPlayerShip().getHp()-damage+gameState.getPlayerShip().getShields());
            gameState.getPlayerShip().setShields(0);
        }

        refreshStats();
    }

    public GameState getGameState () {   return gameState;        }

    public void playSound(int sound){
        effect = MediaPlayer.create(MainActivity.this, sound);
        effect.setVolume(0.8f,0.8f);
        effect.start();  }

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
            default:              song =  MediaPlayer.create(MainActivity.this, R.raw.song1);                  break;

        }
        song.setVolume(0.3f,0.3f);
        song.seekTo(length);
        song.start();
    }

    public int getHighscore() {        return highscore;    }
    public void setHighscore(int highscore) {        this.highscore = highscore;    }

    public int getScore() {        return score;    }
    public void setScore(int score) {        this.score = score;    }

    public void refreshStats(){
        TextView vidamomento = (TextView) findViewById(R.id.vidaNave);
        TextView escudosmomento = (TextView) findViewById(R.id.escudonave);
        TextView lvlmomento = (TextView) findViewById(R.id.lvlEnJuego);
        TextView fuelmomento = (TextView) findViewById(R.id.fuel);
        TextView creditsmomento = (TextView) findViewById(R.id.monedas);

        lvlmomento.setText("Level: "+gameState.getLevel().getLevel());
        vidamomento.setText(""+gameState.getPlayerShip().getHp());
        escudosmomento.setText(""+gameState.getPlayerShip().getShields());
        fuelmomento.setText(""+gameState.getPlayerShip().getEnergy());
        creditsmomento.setText(""+gameState.getPlayerShip().getCredits());
    }

    public void infoBox(String message){
        mensaje.setText(message);
    }

    public void updateFuel(){

        int fuelCost;
        fuelCost = 6 - gameState.getPlayerShip().getEngine();
        if (fuelCost < 1) fuelCost = 1;

        //si està regenerant escuts
        if (gameState.getPlayerShip().getShields()<gameState.getPlayerShip().getMaxshields()){
            fuelCost = fuelCost + 1;
            gameState.getPlayerShip().setShields(gameState.getPlayerShip().getShields()+gameState.getPlayerShip().getShieldregen());
            if (gameState.getPlayerShip().getShields()>gameState.getPlayerShip().getMaxshields()){
                gameState.getPlayerShip().setShields(gameState.getPlayerShip().getMaxshields());
            }
        }

       gameState.getPlayerShip().setEnergy(gameState.getPlayerShip().getEnergy()-fuelCost);

    }

    public void gameOver(){

        //highscore check
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = prefs.edit();

        highscore = gameState.getLevel().getLevel();

        int maxScore = prefs.getInt("HighScore", 0);
        if (maxScore < highscore)   {
            ed.putInt("HighScore", highscore);
            ed.commit();
        }

        //dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.gameover);

        //treiem el marc
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("");

        TextView text = (TextView) dialog.findViewById(R.id.levelReached);
        text.setFocusable(false);
        text.setClickable(true);
        highscore = gameState.getLevel().getLevel();
        text.setText(""+highscore);

        ImageView image = (ImageView) dialog.findViewById(R.id.gameOverBackground);
        image.setImageResource(R.drawable.gameover);

        Button dialogButton = (Button) dialog.findViewById(R.id.returnutton);
        // tanquem dialeg on a click
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGameData();
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

    }

    public void ocultarBoton(){
        toWorkShop.setVisibility(View.INVISIBLE);
    }



    public void crearBoton(){

        toWorkShop.setVisibility(View.VISIBLE);

        toWorkShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), WorkshopActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });

    }




}



