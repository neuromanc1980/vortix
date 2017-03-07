package com.example.xavib.vortix;

//aquesta classe conté la vista de joc, amb graella

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import org.codetome.hexameter.core.api.contract.SatelliteData;
import org.codetome.hexameter.core.api.defaults.DefaultSatelliteData;
import org.codetome.hexameter.core.backport.Optional;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static android.R.attr.data;
import static org.codetome.hexameter.core.api.HexagonOrientation.FLAT_TOP;
import static org.codetome.hexameter.core.api.HexagonOrientation.POINTY_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.HEXAGONAL;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.RECTANGULAR;

public class GridView extends View{

    private Paint linea = new Paint();

    int lvl = 3; double mod = 0;
    double size_modifier;

    private static  int GRID_HEIGHT;
    private static  int GRID_WIDTH;
    private static final HexagonalGridLayout GRID_LAYOUT = HEXAGONAL;
    private static final HexagonOrientation ORIENTATION = POINTY_TOP;
    private static  double RADIUS = 100;

    Bitmap playerShipBm = BitmapFactory.decodeResource(getResources(),
            R.drawable.ship1_s);


    HexagonalGrid grid;

    public GridView(Context context) {        super(context);        init();    }
    public GridView(Context context, AttributeSet attrs) {        super(context, attrs);        init();    }
    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        init();    }

    public void init(){

        //dimensions segons el nivell
        if (lvl < 6){                               GRID_HEIGHT = 5;            GRID_WIDTH = 5;      mod = 8.1;   }
        if ( (lvl > 5) && (lvl < 10) ) {            GRID_HEIGHT = 7;            GRID_WIDTH = 7;      mod = 11;  }
        if (lvl >9 ){                               GRID_HEIGHT = 9;            GRID_WIDTH = 9;      mod = 14;   }

        switch (lvl) {
            case 6:
                this.setBackgroundResource(R.drawable.lvl1);
                break;

        }

    }


    @Override public void draw(Canvas canvas) {

        //alçada modificada
        float grid_height = (float) ((float) RADIUS * mod);
        //compensem tamany de pantalla
        float factor_conversio = ((float)(this.getHeight())/grid_height);

        //construim el grid
        HexagonalGridBuilder builder = new HexagonalGridBuilder<DefaultSatelliteData>()
                .setGridHeight(GRID_HEIGHT)
                .setGridWidth(GRID_WIDTH)
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS*factor_conversio);
        //Log.d("xxx",  "alçada "+Double.toString(RADIUS));
        //Log.d("xxx",  "alçada2 "+Double.toString(builder.getRadius()));
        grid = builder.build();

        linea.setColor(Color.rgb(153, 255, 204));
        linea.setStrokeWidth(8);
        linea.setAlpha(60);

        final List<Hexagon> hexas = new ArrayList<Hexagon>();
        Observable<Hexagon> hexagons = grid.getHexagons();
        hexagons.forEach(new Action1<Hexagon>() {
            @Override
            public void call(Hexagon hexagon) {
                hexas.add(hexagon);
            }
        });

        for(Hexagon hexagon : hexas) {

            //coordenades dels vertexs
            float x_origen=0;     float y_origen=0;     float x_final=0;
            float x_inicial=0;    float y_inicial=0;    float y_final=0;

            SatelliteData myData = new DefaultSatelliteData();

            myData.setMovementCost(2);
            myData.setOpaque(true);

            hexagon.setSatelliteData(myData);

            //boolean test = hexagon.setSatelliteData(isOpaque());
            Optional<SatelliteData> data = hexagon.getSatelliteData();

            if (data.isPresent()){
                SatelliteData satelliteData = data.get();

                double coste = satelliteData.getMovementCost();


            }




            //col.loquem una nau d prova
            canvas.drawBitmap(playerShipBm, (float) hexagon.getCenterX()-playerShipBm.getWidth()/2, (float) hexagon.getCenterY()-playerShipBm.getHeight()/2, null);

            for (Object pointObj : hexagon.getPoints()) {



                //punt inicial
                if (x_origen == 0){
                    //guardem les coordinades inicials
                    x_inicial = ((float) ((Point) pointObj).getCoordinateX());
                    y_inicial = ((float) ((Point) pointObj).getCoordinateY());

                }

                   //si hi ha origen, dibuixem entre una línia origen i el punt actual
                    if (x_origen != 0){
                        canvas.drawLine(x_origen, y_origen, ((float)((Point) pointObj).getCoordinateX()),
                                ((float)((Point) pointObj).getCoordinateY()), linea);
                    }

                    //nou origen
                    x_origen = ((float) ((Point) pointObj).getCoordinateX());
                    y_origen = ((float) ((Point) pointObj).getCoordinateY());

                  y_final = y_origen;   x_final = x_origen;
            }



            //dibuixem l'últim traç
            canvas.drawLine(x_inicial, y_inicial, x_final, y_final, linea);

            //Log.d("xxx",  "alçada "+Float.toString(grid_height));

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
