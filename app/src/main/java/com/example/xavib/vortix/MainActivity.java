package com.example.xavib.vortix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /* PROVA */
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //request landscape


        /*HexagonalGridBuilder builder = new HexagonalGridBuilder()
                .setGridHeight(GRID_HEIGHT)
                .setGridWidth(GRID_WIDTH)
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS);

        HexagonalGrid grid = builder.build();
        Observable<Hexagon> hexagons = grid.getHexagons();

        hexagons.forEach(new Action1<Hexagon>() {
            @Override
            public void call(Hexagon hexagon) {
                for (Object pointObj : hexagon.getPoints()) {
                    // do what you want
                }
            }
        });*/

        Grid grid = new Grid();
        grid.test();

    }


}
