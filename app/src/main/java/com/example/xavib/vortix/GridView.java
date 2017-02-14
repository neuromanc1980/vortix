package com.example.xavib.vortix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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


    private static final int GRID_HEIGHT = 7;
    private static final int GRID_WIDTH = 7;
    private static final HexagonalGridLayout GRID_LAYOUT = HEXAGONAL;
    private static final HexagonOrientation ORIENTATION = POINTY_TOP;
    private static final double RADIUS = 80;


    HexagonalGrid grid;

    public GridView(Context context) {
        super(context);
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){

        // ...

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

            for (Object pointObj : hexagon.getPoints()) {

                //punt inicial
                if (x_origen == 0){
                    //guardem les coordinades inicials
                    x_inicial = (float) ((Point) pointObj).getCoordinateX();
                    y_inicial = (float) ((Point) pointObj).getCoordinateY();

                }

                   //si hi ha origen, dibuixem entre una línia origen i el punt actual
                    if (x_origen != 0){
                        canvas.drawLine(x_origen, y_origen, (float)((Point) pointObj).getCoordinateX(),
                                (float)((Point) pointObj).getCoordinateY(), linea);
                    }
//
 //                   Point point = (Point) pointObj;
//
//                   //nou origen
                    x_origen = (float) ((Point) pointObj).getCoordinateX();
                    y_origen = (float) ((Point) pointObj).getCoordinateY();
//
                  y_final = y_origen;   x_final = x_origen;
//
                    System.out.println(((Point) pointObj).getCoordinateX());
                    System.out.println(((Point) pointObj).getCoordinateY());
//                }



            }

            //dibuixem l'últim traç
            canvas.drawLine(x_inicial, y_inicial, x_final, y_final, linea);

            //reiniciem origen
            x_origen = 0; y_origen = 0;
        }



    }
}
