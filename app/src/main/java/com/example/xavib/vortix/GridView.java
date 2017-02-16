package com.example.xavib.vortix;

//aquesta classe conté la vista de joc, amb graella

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.codetome.hexameter.core.api.Point;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static org.codetome.hexameter.core.api.HexagonOrientation.FLAT_TOP;
import static org.codetome.hexameter.core.api.HexagonOrientation.POINTY_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.HEXAGONAL;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.RECTANGULAR;


public class GridView extends View{

    private Paint linea = new Paint();

    int lvl = 6;

    private static  int GRID_HEIGHT;
    private static  int GRID_WIDTH;
    private static final HexagonalGridLayout GRID_LAYOUT = HEXAGONAL;
    private static final HexagonOrientation ORIENTATION = POINTY_TOP;
    private static  double RADIUS;


    HexagonalGrid grid;

    public GridView(Context context) {        super(context);        init();    }
    public GridView(Context context, AttributeSet attrs) {        super(context, attrs);        init();    }
    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        init();    }

    public void init(){

        //dimensions segons el nivell
        if (lvl < 6){                               GRID_HEIGHT = 5;            GRID_WIDTH = 5;      RADIUS = 100;  }
        if ( (lvl > 5) && (lvl < 10) ) {            GRID_HEIGHT = 7;            GRID_WIDTH = 7;      RADIUS = 85;  }
        if (lvl >9 ){                               GRID_HEIGHT = 9;            GRID_WIDTH = 9;      RADIUS = 65; }

//        switch (lvl) {
//            case 1:
//                GRID_HEIGHT = 5;
//                GRID_WIDTH = 5;
//                break;
//
//        }


        //construim el grid
        HexagonalGridBuilder builder = new HexagonalGridBuilder()
                .setGridHeight(GRID_HEIGHT)
                .setGridWidth(GRID_WIDTH)
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS);
        grid = builder.build();


    }



    @Override public void draw(Canvas canvas) {


        linea.setColor(Color.rgb(255, 153, 51));
        linea.setStrokeWidth(10);



        final List<Hexagon> hexas = new ArrayList<Hexagon>();
        Observable<Hexagon> hexagons = grid.getHexagons();
        hexagons.forEach(new Action1<Hexagon>() {
            @Override
            public void call(Hexagon hexagon) {
                hexas.add(hexagon);
            }
        });

        for(Hexagon hexagon : hexas) {

            float x_origen=0;     float y_origen=0;     float x_final=0;
            float x_inicial=0;    float y_inicial=0;    float y_final=0;

            //calculem els marges verticals 10% a dalt i a vall
            //float grid_height = grid.getGridData().getGridHeight();
            float grid_height = (float) ((float) RADIUS * 10.85);
            //Log.d("xxx",  Float.toString(grid_height));
            float factor_conversio = ((float)(this.getHeight())/grid_height)*8/10;
            //l'alçada de la graella
            float offset =  (float) (this.getHeight()*0.1);
            float offset_hor =  (float) (this.getHeight()*0.04);


            for (Object pointObj : hexagon.getPoints()) {

                //punt inicial
                if (x_origen == 0){
                    //guardem les coordinades inicials
                    x_inicial = ((float) ((Point) pointObj).getCoordinateX() + offset_hor)*factor_conversio;
                    y_inicial = ((float) ((Point) pointObj).getCoordinateY() + offset)*factor_conversio;

                }

                   //si hi ha origen, dibuixem entre una línia origen i el punt actual
                    if (x_origen != 0){
                        canvas.drawLine(x_origen, y_origen, ((float)((Point) pointObj).getCoordinateX() + offset_hor)*factor_conversio,
                                ((float)((Point) pointObj).getCoordinateY() + offset)*factor_conversio, linea);
                    }

 //                   Point point = (Point) pointObj;

                //nou origen
                    x_origen = ((float) ((Point) pointObj).getCoordinateX() + offset_hor) * factor_conversio;
                    y_origen = ((float) ((Point) pointObj).getCoordinateY() + offset) * factor_conversio;
//
                  y_final = y_origen;   x_final = x_origen;
//





            }

            //dibuixem l'últim traç
            canvas.drawLine(x_inicial, y_inicial, x_final, y_final, linea);

            Log.d("xxx",  "alçada "+Float.toString(grid_height));

            //reiniciem origen
            x_origen = 0; y_origen = 0;
        }



    }

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
                }

                break;


        }
        return true; //rebem array de punts de contacte i si tenene esdeveniment down, move, up

    }
}
