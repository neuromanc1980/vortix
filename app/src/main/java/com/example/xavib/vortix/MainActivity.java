package com.example.xavib.vortix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //request landscape
        //holder.id = (TextView) myView.findViewById(R.id.id);

        //creem una vista de tipus gridview
        GridView grid = (GridView) findViewById(R.id.grid);


    }


}
