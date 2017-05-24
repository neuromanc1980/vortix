package com.example.xavib.vortix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by xaviB on 17/5/17.
 */

public class WorkshopActivity extends AppCompatActivity {

    ImageView eng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workshop);

        eng = (ImageView) findViewById(R.id.workshopbg);
        eng.setImageResource(R.drawable.shipbgengine);

    }



}
