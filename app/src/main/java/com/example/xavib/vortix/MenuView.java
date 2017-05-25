package com.example.xavib.vortix;

//vista del menú

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuView extends View {

    public MenuView(Context context) { this(context, null, 0); }
    public MenuView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*private void launch() {

        Intent intent = new Intent(getContext(), GameView.class);
        startActivity(intent);
    }*/


    @Override public boolean onTouchEvent(MotionEvent event){

    float x = event.getX(); //xy sobre la posició esquerre superior del control (de 0 al width/height)
    float y = event.getY();

    float relative_x = x/(this.getWidth())*100;
    float relative_y = y/(this.getHeight())*100;

    int action = event.getAction();
    switch (action) {
     case MotionEvent.ACTION_DOWN:
           Log.d("xxx", "DOWN en " + x + "," + y);
           Log.d("xxx", "\nDOWN en y:" + relative_y + "%, x:" + relative_x + "%");

         if ( (relative_x > 66)&&(relative_x < 94) ){

             if ((relative_y > 11) && (relative_y < 21)){
                 Log.d("xxx", "new game");
                 Intent intent = new Intent(getContext(), MainActivity.class);
                 intent.putExtra("new", true);      //reinicia el joc
                 getContext().startActivity(intent);

             }

             if ((relative_y > 24) && (relative_y < 33)){
                 Log.d("xxx", "continue");
                 Intent intent = new Intent(getContext(), MainActivity.class);
                 intent.putExtra("new", false);
                 getContext().startActivity(intent);

             }

             if ((relative_y > 37) && (relative_y < 46)){
                 Log.d("xxx", "score");
                 //carreguem dades per mostrar el highscore
                 SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this.getContext().getApplicationContext());
                 int highscore = gameData.getInt("HighScore",0);

                 //creem un dialog
                 final Dialog dialog = new Dialog(super.getContext());
                 dialog.setContentView(R.layout.highscore);

                 //treiem el marc
                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 dialog.setTitle("");

                 //customitzem el dialog
                 TextView text = (TextView) dialog.findViewById(R.id.levelReached);
                 text.setFocusable(false);
                 text.setClickable(true);
                 text.setText(""+highscore);

                 ImageView image = (ImageView) dialog.findViewById(R.id.gameOverBackground);
                 image.setImageResource(R.drawable.score);

                 Button dialogButton = (Button) dialog.findViewById(R.id.returnutton);
                 // tanquem dialog al clicar
                 dialogButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         dialog.dismiss();
                     }
                 });

                 dialog.show();
             }

             if ((relative_y > 50) && (relative_y < 59)){
                 Log.d("xxx", "workshop");


                 //carreguem dades per comprovar el workshop
                 SharedPreferences gameData = PreferenceManager.getDefaultSharedPreferences(this.getContext().getApplicationContext());
                 int station = gameData.getInt("Station",0);
                 if (station == 1){     //entrem al workshop
                     Intent intent = new Intent(getContext(), WorkshopActivity.class);
                     intent.putExtra("new", false);
                     getContext().startActivity(intent);
                 }  else    {

                     //sound
                     MediaPlayer song;
                     song = MediaPlayer.create(getContext(), R.raw.glitch);
                     song.setVolume(0.4f,0.4f);
                     song.start();

                     //creem un dialog
                     final Dialog dialog = new Dialog(super.getContext());
                     dialog.setContentView(R.layout.workshop_warning);

                     //treiem el marc
                     dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                     dialog.setTitle("");

                     ImageView image = (ImageView) dialog.findViewById(R.id.alert);
                     image.setImageResource(R.drawable.alertworkshop);

                     Button dialogButton = (Button) dialog.findViewById(R.id.returnutton);
                     // tanquem dialog al clicar
                     dialogButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             dialog.dismiss();
                         }
                     });

                     dialog.show();

                 }

             }

             if ((relative_y > 63) && (relative_y < 72)){
                 Log.d("xxx", "instructions");
                 Intent intent = new Intent(getContext(), InstructionsActivity.class);
                 getContext().startActivity(intent);
             }

             if ((relative_y > 75) && (relative_y < 84)){
                 Log.d("xxx", "quit");
                 android.os.Process.killProcess(android.os.Process.myPid());
                 System.exit(1);
             }

         }

         break;


     }
       return true; //rebem array de punts de contacte i si tenene esdeveniment down, move, up

    }


}
