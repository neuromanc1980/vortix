package com.example.xavib.vortix;

//vista del menú

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



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
                 //launch();
                 Intent intent = new Intent(getContext(), MainActivity.class);
                 getContext().startActivity(intent);

             }

             if ((relative_y > 24) && (relative_y < 33)){
                 Log.d("xxx", "continue");
             }

             if ((relative_y > 37) && (relative_y < 46)){
                 Log.d("xxx", "score");
             }

             if ((relative_y > 50) && (relative_y < 59)){
                 Log.d("xxx", "workshop");
             }

             if ((relative_y > 63) && (relative_y < 72)){
                 Log.d("xxx", "instructions");
                 Intent intent = new Intent(getContext(), Instructions.class);
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
