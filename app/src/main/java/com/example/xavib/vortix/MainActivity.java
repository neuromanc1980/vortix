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

        //creem una vista de tipus gridview
        GridView gridView = (GridView) findViewById(R.id.grid);
        ImageView background = (ImageView) findViewById(R.id.background);

        background.setBackgroundResource(R.drawable.lvl1);



    }


}
