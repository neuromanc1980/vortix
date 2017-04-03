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
import android.widget.ImageView;

import org.codetome.hexameter.core.api.CubeCoordinate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridCalculator;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.codetome.hexameter.core.api.Point;
import org.codetome.hexameter.core.api.RotationDirection;
import org.codetome.hexameter.core.api.contract.SatelliteData;
import org.codetome.hexameter.core.backport.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;

import static org.codetome.hexameter.core.api.HexagonOrientation.POINTY_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.HEXAGONAL;

public class GridView extends View{

    private Paint blur = new Paint();
    GameState gameState;
    int id = 0;
    Level level;

    private Paint linea = gameState.getLevel().getLinea();
    Ship playerShip = gameState.getPlayerShip();
    int rang = playerShip.getScanner();

    //paràmetres del grid comuns a tots els nivells
    private static final HexagonalGridLayout GRID_LAYOUT = HEXAGONAL;
    private static final HexagonOrientation ORIENTATION = POINTY_TOP;
    private static  double RADIUS = 100;

    Bitmap playerShipBm = BitmapFactory.decodeResource(getResources(), gameState.getImatge());

    HexagonalGrid grid;

    public GridView(Context context) {        super(context);        init();    }
    public GridView(Context context, AttributeSet attrs) {        super(context, attrs);        init();    }
    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        init();    }

    public void init(){

        playerShip.setImatge(R.drawable.ship1_s);
        playerShipBm = BitmapFactory.decodeResource(getResources(),
                playerShip.getImatge());
        //ImageView background = (ImageView) findViewById(R.id.background);
        //Log.d("xxx", "DOWN en " +level.getBackground());

        //myActivity.setBackground();



    }

    public void setVisibility(){    //métode que estableix visibilitat de rang1

        Optional <Hexagon> playerShipPosition = grid.getByCubeCoordinate(playerShip.getCoordinates());   //hexagon de la nau del

        if (playerShipPosition.isPresent()){

            //hexagon jugador visible
            HexagonSatelliteData dataCentral = new HexagonSatelliteData();

            playerShipPosition.get().setSatelliteData(dataCentral);

            //de fora cap a endins

            Optional<Hexagon> vei;            Optional<Hexagon> vei2;

            for (int i = 0; i <= 5 ; i++){      //6 veins per index
                if (grid.getNeighborByIndex(playerShipPosition.get(),i).isPresent() ){

                    vei = grid.getNeighborByIndex(playerShipPosition.get(), i);

                    //rang2
                    if (rang == 2){
                        for (int y = 0; y <= 5 ; y++){
                            if (grid.getNeighborByIndex(vei.get(),y).isPresent()){
                                 vei2 = grid.getNeighborByIndex(vei.get(),y);
                                HexagonSatelliteData data2 = new HexagonSatelliteData();
                                data2.setVisible(true);         data2.setMoveable(false);       vei2.get().setSatelliteData(data2);
                            }
                        }
                    }
                }
            }

            //rang1
            for (int i = 0; i <= 5 ; i++){
                if (grid.getNeighborByIndex(playerShipPosition.get(),i).isPresent() ){
                    vei = grid.getNeighborByIndex(playerShipPosition.get(), i);
                    HexagonSatelliteData data = new HexagonSatelliteData();
                    data.setVisible(true);                    data.setMoveable(true);           vei.get().setSatelliteData(data);
                }
            }

            //hexagon jugador
            dataCentral.setVisible(true);
            dataCentral.setMoveable(false);
            playerShipPosition.get().setSatelliteData(dataCentral);
        }
    }

    @Override public void draw(Canvas canvas) {


        //background segons nivell
        //myActivity.setBackground(level.getBackground());
        this.setBackgroundResource(level.getBackground());

        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),level.getBackground()),0,0,null);


        this.postInvalidateDelayed(50); //kjdfhkjsdfhkj
        if (gameState == null) return;

        level = gameState.getLevel();

        //alçada modificada
        float grid_height = (float) ((float) RADIUS * level.getMod());
        //compensem tamany de pantalla
        float factor_conversio = ((float)(this.getHeight())/grid_height);

        HexagonalGridBuilder builder = new HexagonalGridBuilder<HexagonSatelliteData>()
                .setGridHeight(level.getGridH())
                .setGridWidth(level.getGridW())
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS*factor_conversio);

        //construim la graella
        grid = builder.build();
        HexagonalGridCalculator hexCalc = builder.buildCalculatorFor(grid);



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

        for(Hexagon hexagon : hexas){
            HexagonSatelliteData dataInicial = new HexagonSatelliteData();
            dataInicial.setVisible(false);
            hexagon.setSatelliteData(dataInicial);
        }

        setVisibility();



        //els recorrem
        for(Hexagon hexagon : hexas) {

            //blurrejem els no visibles
            if (hexagon.getSatelliteData().isPresent()){
                HexagonSatelliteData data = (HexagonSatelliteData) hexagon.getSatelliteData().get();

                if (data.isVisible()==false) {

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

            //si és l'hexagon on és la nau
            if (hexagon.getCubeCoordinate().equals(playerShip.getCoordinates())){

                rotate(playerShipBm, playerShip.getOrientacio()*60, canvas, (int) hexagon.getCenterX(), (int) hexagon.getCenterY());
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

    public void setBackground (int backgroundID){
        this.setBackgroundResource(backgroundID);
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
                if (touchedHexagon.isPresent()){
                Optional<HexagonSatelliteData> dataTouched = touchedHexagon.get().getSatelliteData();
                Log.d("xxx", "\nHexagon: " + touchedHexagon.get().getCubeCoordinate() + " visible?: "+dataTouched.get().isVisible() + " moveable?: "+dataTouched.get().isMoveable());

                    if (dataTouched.get().isMoveable()){ //ens movem a la casella

                        //coordinades cúbiques origen i destí
                        int origenX = playerShip.getShipX();                    int origenZ = playerShip.getShipZ();
                        int destiX = touchedHexagon.get().getGridX();                    int destiZ = touchedHexagon.get().getGridZ();

                        //orientem
                        if (origenX == destiX){    if (origenZ > destiZ)   {    playerShip.setOrientacio(4);     }   if (origenZ < destiZ) {    playerShip.setOrientacio(1);  } }// Log.d("xxx", "\nHexagon:1") ;
                        if (origenX > destiX){     if (origenZ == destiZ)  {    playerShip.setOrientacio(3);     }   else {    playerShip.setOrientacio(2);  } }// Log.d("xxx", "\nHexagon:2") ;    }
                        if (origenX < destiX){     if (origenZ > destiZ)   {    playerShip.setOrientacio(5);     }   else {    playerShip.setOrientacio(6);  }  }// Log.d("xxx", "\nHexagon:3") ;   }
                        if ( (origenX == destiX) &&  (origenZ == destiZ) )   {    playerShip.setOrientacio(playerShip.getOrientacio()+1);     }

                        playerShip.setShipX(destiX);
                        playerShip.setShipZ(destiZ);
                    }

                }

                break;


        }
        return true; //rebem array de punts de contacte i si tenene esdeveniment down, move, up

    }


    public void rotate(Bitmap paramBitmap, float angle, Canvas canvas, int x, int y)
    {
        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(angle);
        canvas.translate(-paramBitmap.getWidth() / 2, -paramBitmap.getHeight() / 2);
        canvas.drawBitmap(paramBitmap, 0, 0, new Paint());
        canvas.restore();

    }

    public void setGameState(GameState gameState){        this.gameState = gameState;    }

}
