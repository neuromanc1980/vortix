package com.example.xavib.vortix;

//aquesta classe conté la vista de joc, amb graella

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.codetome.hexameter.core.api.CubeCoordinate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.codetome.hexameter.core.api.Point;
import org.codetome.hexameter.core.api.contract.SatelliteData;
import org.codetome.hexameter.core.backport.Optional;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static org.codetome.hexameter.core.api.HexagonOrientation.POINTY_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.HEXAGONAL;

public class GridView extends View{

    private Paint linea = new Paint();
    private Paint blur = new Paint();

    private int lvl = 7, id = 1; private double mod = 0;
    private String center, first, last;

    Ship playerShip = new Ship();
    Optional<Hexagon> vei;

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
        if (lvl < 6){
            GRID_HEIGHT = 5;            GRID_WIDTH = 5;      mod = 8.1;     linea.setColor(Color.rgb(153, 255, 204));  center = "1,2";  first = "0,0";  last = "3,2";
            playerShip.setShipX(1);     playerShip.setShipZ(2);
        }

        if ( (lvl > 5) && (lvl < 10) ) {
            GRID_HEIGHT = 7;            GRID_WIDTH = 7;      mod = 11;      linea.setColor(Color.rgb(255, 204, 153));  center = "2,3";  first = "2,3";  last = "1,4";
            playerShip.setShipX(2);     playerShip.setShipZ(3);
        }

        if (lvl >9 ){
            GRID_HEIGHT = 9;            GRID_WIDTH = 9;      mod = 14;      linea.setColor(Color.rgb(204, 153, 255));  center = "2,4";  first = "2,3";  last = "1,4";
            playerShip.setShipX(2);     playerShip.setShipZ(4);
        }

        //fons segons el nivell
        switch (lvl) {
            case 6:
                this.setBackgroundResource(R.drawable.lvl1);
                break;

        }

    }

    public void setVisibility(){    //métode que estableix visibilitat de rang1

        Optional <Hexagon> playerShipPosition = grid.getByCubeCoordinate(playerShip.getCoordinates());   //hexagon de la nau del jugador


        if (playerShipPosition.isPresent()){

            HexagonSatelliteData dataCentral = new HexagonSatelliteData();
            dataCentral.setVisible(true);
            playerShipPosition.get().setSatelliteData(dataCentral);

            for (int i = 0; i <= 5 ; i++){      //6 veins per index
                if (grid.getNeighborByIndex(playerShipPosition.get(),i).isPresent() ){
                    vei = grid.getNeighborByIndex(playerShipPosition.get(), i);
                    HexagonSatelliteData data = new HexagonSatelliteData();
                    data.setVisible(true);
                    vei.get().setSatelliteData(data);
                    //Log.d("xxx", "\nVei N: " + i + " coordenada: "+vei.get().getCubeCoordinate()+" visibilitat: "+data.isVisible());
                }

            }
        }
    }

    @Override public void draw(Canvas canvas) {

        this.postInvalidateDelayed(50);

        //alçada modificada
        float grid_height = (float) ((float) RADIUS * mod);
        //compensem tamany de pantalla
        float factor_conversio = ((float)(this.getHeight())/grid_height);

        //construim el grid
        HexagonalGridBuilder builder = new HexagonalGridBuilder<HexagonSatelliteData>()
                .setGridHeight(GRID_HEIGHT)
                .setGridWidth(GRID_WIDTH)
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS*factor_conversio);

        //construim la graella
        grid = builder.build();

        //pinzell de les línices
        linea.setStrokeWidth(4);        linea.setAlpha(60);        linea.setStyle(Paint.Style.FILL);

        //pinzell del blur
        blur.setStyle(Paint.Style.FILL);        blur.setColor(Color.DKGRAY);        blur.setAlpha(200);

        //tots els hexagons
        final List<Hexagon> hexas = new ArrayList<Hexagon>();
        Observable<Hexagon> hexagons = grid.getHexagons();
        hexagons.forEach(new Action1<Hexagon>() {
            @Override
            public void call(Hexagon hexagon) {
                hexas.add(hexagon);
            }
        });

        int count = 0;



        for(Hexagon hexagon : hexas){
            HexagonSatelliteData dataInicial = new HexagonSatelliteData();
            dataInicial.setVisible(false);
            hexagon.setSatelliteData(dataInicial);
        }

        setVisibility();

        //els recorrem
        for(Hexagon hexagon : hexas) {

//            //amaguem tots els hexagons
//            if (hexagon.getSatelliteData().isPresent()){
//                HexagonSatelliteData dataInicial = (HexagonSatelliteData) hexagon.getSatelliteData().get();
//                dataInicial.setVisible(false);
//                hexagon.setSatelliteData(dataInicial);
//            }

            //posem els veins a visible

            if (hexagon.getSatelliteData().isPresent()){count++;}

            //Log.d("xxx",  "contador de hexagonos que tienen satelita data vinculado: "+count);


            //blurrejem els no visibles
            if (hexagon.getSatelliteData().isPresent()){
                HexagonSatelliteData data = (HexagonSatelliteData) hexagon.getSatelliteData().get();

                //Log.d("xxx",  "hexagono visible: "+data.isVisible());

                if (data.isVisible()==false) {
                    //canvas.drawCircle((float) hexagon.getCenterX(), (float) hexagon.getCenterY(), (float) RADIUS/2, blur);

                    //canvas.drawPaint(linea);
                    Path path = new Path();
                    float x_inicial = 0;    float next_x = 0;
                    float y_inicial = 0;    float next_y = 0;

                    for (Object pointObj : hexagon.getPoints()){
                        if (x_inicial == 0) { x_inicial = (float) ( (Point) pointObj).getCoordinateX();
                                              y_inicial = (float) ( (Point) pointObj).getCoordinateY();
                                              path.moveTo( (float) ( (Point) pointObj).getCoordinateX(), (float) ( (Point) pointObj).getCoordinateY());
                        }
                        else {
                            path.lineTo( (float) ( (Point) pointObj).getCoordinateX(), (float) ( (Point) pointObj).getCoordinateY());
                        }


                    }
                    path.lineTo(x_inicial,y_inicial);   //traç final
                    path.close();
                    canvas.drawPath(path, blur);
                }
            }

            //coordenades dels vertexs
            float x_origen=0;     float y_origen=0;     float x_final=0;
            float x_inicial=0;    float y_inicial=0;    float y_final=0;

//            //satellite data
//            HexagonSatelliteData myData = new HexagonSatelliteData();
//
//            myData.setMovementCost(2);
//            myData.setOpaque(true);
//            //myData.setVisible(false);

            //hexagon.setSatelliteData(myData);

//            //boolean test = hexagon.setSatelliteData(isOpaque());
//            Optional<SatelliteData> data = hexagon.getSatelliteData();
//
//            if (data.isPresent()){
//                SatelliteData satelliteData = data.get();
//
//                double coste = satelliteData.getMovementCost();
//            }

            //si és l'hexagon on és la nau
            if (hexagon.getCubeCoordinate().equals(playerShip.getCoordinates())){

                //RotateBitmap(playerShipBm,145);
                //canvas.drawBitmap(RotateBitmap(playerShipBm, 120), (float) hexagon.getCenterX() - playerShipBm.getWidth() / 2, (float) hexagon.getCenterY() - playerShipBm.getHeight() / 2, null);

                canvas.drawBitmap(rotate(playerShipBm, 120), (float) hexagon.getCenterX() - playerShipBm.getWidth() / 2, (float) hexagon.getCenterY() - playerShipBm.getHeight() / 2, null);

//                Matrix matrix = new Matrix();
//                matrix.reset();
//
//                matrix.postRotate(120, playerShipBm.getHeight()/2 , playerShipBm.getWidth()/2 );
//
//                Bitmap test =  Bitmap.createBitmap(playerShipBm, 0, 0, playerShipBm.getWidth(), playerShipBm.getHeight(), matrix, true);
//                canvas.drawBitmap(test, (float) hexagon.getCenterX() - playerShipBm.getWidth() / 2, (float) hexagon.getCenterY() - playerShipBm.getHeight() / 2, null);

                //Log.d("xxx",  playerShip.getCoordinates().toString());


                //--felix--
//                Bitmap destBitmap = Bitmap.createBitmap(playerShipBm.getWidth(), playerShipBm.getHeight(), playerShipBm.getConfig());
//                Canvas canvas2 = new Canvas(destBitmap);
//                Matrix matrix = new Matrix();
//                matrix.setRotate(180, playerShipBm.getWidth()/2, playerShipBm.getHeight()/2);
//                canvas2.drawBitmap(playerShipBm, matrix, linea);
            }

            //dibuixem la graella
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

            //reiniciem origen
            x_origen = 0; y_origen = 0;
            id += 1;    //salt del bucle
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
                //Log.d("xxx", "DOWN en " + x + "," + y);
                //Log.d("xxx", "\nDOWN en y:" + relative_y + "%, x:" + relative_x + "%");


                Optional<Hexagon> touchedHexagon =  grid.getByPixelCoordinate(x,y);
                Optional<HexagonSatelliteData> dataTouched = touchedHexagon.get().getSatelliteData();
                Log.d("xxx", "\nHexagon: " + touchedHexagon.get().getCubeCoordinate() + " visible?: "+dataTouched.get().isVisible());

                if (dataTouched.get().isVisible()){ //ens movem a la casella
                    playerShip.setShipX(touchedHexagon.get().getGridX());
                    playerShip.setShipZ(touchedHexagon.get().getGridZ());

                }

                break;


        }
        return true; //rebem array de punts de contacte i si tenene esdeveniment down, move, up

    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){  //encarar al nou hexagon
        Matrix matrix = new Matrix();
        matrix.reset();
        //matrix.postTranslate(-source.getWidth() / 2, -source.getHeight() / 2);  //centra
        matrix.postRotate(angle, source.getHeight()/2 , source.getWidth()/2 );
        //matrix.postTranslate(source.getWidth(), source.getWidth());

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        //matrix.postRotate(0);

        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(source,source.getWidth(),source.getHeight(),true);

        //Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);


        //matrix.postRotate(angle, source.getWidth()/2,  source.getHeight()/2);
        //return rotatedBitmap;




        //Canvas canvas2 = new Canvas();
        //canvas2.drawBitmap(source, matrix, new Paint())
//        int width = source.getWidth();
//        int height = source.getHeight();
//
//        Matrix matrix = new Matrix();
//        matrix.preRotate(angle, source.getWidth()/2,  source.getHeight()/2);
//
//        Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
//        Canvas canvas = new Canvas(rotatedBitmap);
//        //canvas.drawBitmap(rotatedBitmap, 5.0f, 0.0f, null);

        //return rotatedBitmap;

    }

//    public static Matrix RotateMatrix(Bitmap source, float angle){
//        Matrix matrix = new Matrix();
//        matrix.setRotate(angle, source.getWidth()/2,  source.getHeight()/2);
//        return matrix;
//    }

    public Bitmap rotate(Bitmap paramBitmap, float angle)
    {
        if (angle% 360 == 0) {
            return paramBitmap;
        }
        Matrix localMatrix = new Matrix();
        float f1 = paramBitmap.getWidth() / 2;
        float f2 = paramBitmap.getHeight() / 2;
        localMatrix.postTranslate(-paramBitmap.getWidth() / 2, -paramBitmap.getHeight() / 2);
        localMatrix.postRotate(angle);
        localMatrix.postTranslate(f1, f2);
        paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
        new Canvas(paramBitmap).drawBitmap(paramBitmap, 0.0F, 0.0F, null);
        return paramBitmap;
    }



}
