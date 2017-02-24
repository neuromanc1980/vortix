package com.example.xavib.vortix;

//activitat principal del joc

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //request landscape
        //holder.id = (TextView) myView.findViewById(R.id.id);

        //creem una vista de tipus gridview
        GridView gridView = (GridView) findViewById(R.id.grid);
        ImageView background = (ImageView) findViewById(R.id.background);

        //gridView.setBackgroundResource(findViewById(R.drawable.lvl1));
        //grid.setBackground(R.drawable.lvl1);
        //gridView.setBackground(R.drawable.lvl1);
        //gridView.setBackgroundResource(R.drawable.lvl1);
        //background.setImageResource(R.drawable.lvl1);
        background.setBackgroundResource(R.drawable.lvl1);
        //grid.setBackground(R.drawable.lvl1);



    }


}
