package com.example.xavib.vortix;

//aquesta classe conté la vista de joc, amb graella

import android.content.Context;
import static java.lang.Math.*;
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
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;

import static org.codetome.hexameter.core.api.HexagonOrientation.POINTY_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.HEXAGONAL;

public class GridView extends View{

    private Paint blur = new Paint();   private Paint linea;
    GameState gameState;
    int rang = 0;
    public ImageView background2;
    Level level;
    Ship playerShip;
    Bitmap playerShipBm;
    boolean built = false;
    private List<Hexagon> hexas = new ArrayList<Hexagon>();
    Optional<Hexagon> touchedHexagon;
    Observable<Hexagon> hexagons;
    HexagonSatelliteData data = new HexagonSatelliteData();
    MainActivity mainActivity;

    //paràmetres del grid comuns a tots els nivells
    private static final HexagonalGridLayout GRID_LAYOUT = HEXAGONAL;
    private static final HexagonOrientation ORIENTATION = POINTY_TOP;
    private static  double RADIUS = 100;

    HexagonalGrid grid;

    public GridView(Context context) {        super(context);        init();    }
    public GridView(Context context, AttributeSet attrs) {        super(context, attrs);        init();    }
    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        init();    }



    public void init(){

    }

    public void setVisibility(){    //métode que estableix visibilitat de rang1

        Optional <Hexagon> playerShipPosition = grid.getByCubeCoordinate(playerShip.getCoordinates());   //hexagon de la nau del

        if (playerShipPosition.isPresent()){

            //hexagon jugador visible
            data = (HexagonSatelliteData) playerShipPosition.get().getSatelliteData().get();

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
                                HexagonSatelliteData data2 = (HexagonSatelliteData) vei2.get().getSatelliteData().get();
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
                    HexagonSatelliteData data = (HexagonSatelliteData) vei.get().getSatelliteData().get();
                    data.setVisible(true);                    data.setMoveable(true);           vei.get().setSatelliteData(data);
                }
            }

            //hexagon jugador
            data.setVisible(true);
            data.setMoveable(false);
            playerShipPosition.get().setSatelliteData(data);
        }
    }

    @Override public void draw(Canvas canvas) {

        if (gameState == null) return;
        this.postInvalidateDelayed(50); //taxa de refresc

        //gameState = mainActivity.getGameState();    //carreguem gameState

        if (!built) {
            built = true;       //només construirem una vegada

            Log.d("xxx", "\nBuilding grid: "            );



            linea = gameState.getLevel().getLinea();
            level = gameState.getLevel();
            playerShip = gameState.getPlayerShip();
            rang = playerShip.getScanner();
            playerShipBm = BitmapFactory.decodeResource(getResources(), playerShip.getImatge());
            playerShip.setImatge(gameState.getPlayerShip().getImatge());

            background2 = (ImageView) findViewById(R.id.background);

            //alçada modificada
            float grid_height = (float) ((float) RADIUS * level.getMod());
            //compensem tamany de pantalla
            float factor_conversio = ((float) (this.getHeight()) / grid_height);

            HexagonalGridBuilder builder = new HexagonalGridBuilder<HexagonSatelliteData>()
                    .setGridHeight(level.getGridH())
                    .setGridWidth(level.getGridW())
                    .setGridLayout(GRID_LAYOUT)
                    .setOrientation(ORIENTATION)
                    .setRadius(RADIUS * factor_conversio);

            //construim la graella
            grid = builder.build();
            HexagonalGridCalculator hexCalc = builder.buildCalculatorFor(grid);

            //tots els hexagons
            hexagons = grid.getHexagons();
            hexagons.forEach(new Action1<Hexagon>() {
                 @Override
                 public void call(Hexagon hexagon) {    hexas.add(hexagon);  }
                 }    );

            //pinzell del blur
            blur.setStyle(Paint.Style.FILL);
            blur.setColor(Color.DKGRAY);
            blur.setAlpha(200);

            for (Hexagon hexagon : hexas) {
                HexagonSatelliteData dataInicial = new HexagonSatelliteData();
                dataInicial.setVisible(false);
                hexagon.setSatelliteData(dataInicial);
            }
        }


        //els recorrem
        for(Hexagon hexagon : hexas) {

            if (hexagon.getSatelliteData().isPresent()){

                HexagonSatelliteData data = (HexagonSatelliteData) hexagon.getSatelliteData().get();
                //data.setVisible(false);     //tots invisibles de base
                data.setMoveable(false);    //tots son no travessables de base
                hexagon.setSatelliteData(data);

                setVisibility();    //visibles els veins segons el scanner

                //portal
                if (data.getElement() instanceof Portal){
                    Bitmap portalBm = BitmapFactory.decodeResource(getResources(), R.drawable.portal);
                    canvas.drawBitmap(portalBm, (float) hexagon.getCenterX() - portalBm.getHeight()/2, (float) hexagon.getCenterY() - portalBm.getWidth()/2, new Paint() );
                }

                //blurrejem els no visibles
                if (data.isVisible()==false) {

                    Path path = new Path();                    float x_inicial = 0;    float next_x = 0;                    float y_inicial = 0;    float next_y = 0;

                    for (Object pointObj : hexagon.getPoints()){
                        if (x_inicial == 0) { x_inicial = (float) ( (Point) pointObj).getCoordinateX();
                                              y_inicial = (float) ( (Point) pointObj).getCoordinateY();
                                              path.moveTo( (float) ( (Point) pointObj).getCoordinateX(), (float) ( (Point) pointObj).getCoordinateY());
                        }   else {
                            path.lineTo( (float) ( (Point) pointObj).getCoordinateX(), (float) ( (Point) pointObj).getCoordinateY());
                        }
                    }
                    path.lineTo(x_inicial,y_inicial);   //traç final
                    path.close();                    canvas.drawPath(path, blur);
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
            //id += 1;    //salt del bucle
        }

        //col.loquem el portal en una casella invisible si no està colocat ja
        if (!level.isPortalPlaced()){
            Log.d("xxx", "\nPortal is not placed yet, placing it.....: ");
            placePortal(hexas);
            level.setPortalPlaced(true);
        }
    }

    public void setBackground (int backgroundID){
        this.setBackgroundResource(backgroundID);
    }

    @Override public boolean onTouchEvent(MotionEvent event){

        Log.d("xxx", "level: " + gameState.getLevel().getLevel());

        float x = event.getX(); //xy sobre la posició esquerre superior del control (de 0 al width/height)
        float y = event.getY();

        float relative_x = x/(this.getWidth())*100;
        float relative_y = y/(this.getHeight())*100;

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                touchedHexagon =  grid.getByPixelCoordinate(x,y);
                if (touchedHexagon.isPresent()){

                    if (playerShip.getCoordinates().equals(touchedHexagon.get().getCubeCoordinate())){
                        //Log.d("xxx", "\nSortir");
                        return false;  //no activa el propi hexagon
                    }

                Optional<HexagonSatelliteData> dataTouched = touchedHexagon.get().getSatelliteData();
                Log.d("xxx", "\nHexagon: " + touchedHexagon.get().getCubeCoordinate() + " visible?: "+dataTouched.get().isVisible() + " moveable?: "+dataTouched.get().isMoveable() + " element: " + dataTouched.get().getElement());
                Log.d("xxx", "\nPortal is in: " + level.getPortal().getXCoord() + " <= X/Z => " + level.getPortal().getZCoord());

                    if (dataTouched.get().isMoveable()){ //ens movem a la casella

                        //coordinades cúbiques origen i destí
                        int origenX = playerShip.getShipX();                    int origenZ = playerShip.getShipZ();
                        int destiX = touchedHexagon.get().getGridX();                    int destiZ = touchedHexagon.get().getGridZ();

                        //orientem
                        if (origenX == destiX){    if (origenZ > destiZ)   {    playerShip.setOrientacio(4);     }   if (origenZ < destiZ) {    playerShip.setOrientacio(1);  } }// Log.d("xxx", "\nHexagon:1") ;
                        if (origenX > destiX){     if (origenZ == destiZ)  {    playerShip.setOrientacio(3);     }   else {    playerShip.setOrientacio(2);  } }// Log.d("xxx", "\nHexagon:2") ;    }
                        if (origenX < destiX){     if (origenZ > destiZ)   {    playerShip.setOrientacio(5);     }   else {    playerShip.setOrientacio(6);  }  }// Log.d("xxx", "\nHexagon:3") ;   }
                        if ( (origenX == destiX) &&  (origenZ == destiZ) )   {    playerShip.setOrientacio(playerShip.getOrientacio()+1);     }

                        playerShip.setShipX(destiX);                        playerShip.setShipZ(destiZ);
                    }

                    //portal
                    if (dataTouched.get().getElement() instanceof Portal && dataTouched.get().isVisible()){
                        level = new Level (min(level.getLevel()+1,15)); //pujem de nivell

                        gameState.setLevel(level);

                        playerShip.setShipX(gameState.getLevel().getStartingX());   //recol.loquem la nau
                        playerShip.setShipZ(gameState.getLevel().getStartingZ());
                        mainActivity.updateBackground(gameState.getLevel().getBackground());
                        cleanBoard();

                        Log.d("xxx", "\nEnter portal");
                        Log.d("xxx", "\nBackground: "+ gameState.getLevel().getBackground());
                    }

                }
                break;
        }
        return true; //rebem array de punts de contacte i si tenen esdeveniment down, move, up
    }

    public void rotate(Bitmap paramBitmap, float angle, Canvas canvas, int x, int y)    //mètode per encarar la nau segons on es mou
    {
        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(angle);
        canvas.translate(-paramBitmap.getWidth() / 2, -paramBitmap.getHeight() / 2);
        canvas.drawBitmap(paramBitmap, 0, 0, new Paint());
        canvas.restore();

    }

    public void setGameState(GameState gameState){        this.gameState = gameState;    }  //mètode per updatejar el gameState

    public void placePortal(List <Hexagon> lista){      //mètode per col.locar el portal

        //triem un hexagon a l'atzar
        Random r = new Random();
        int pos = r.nextInt(lista.size() - 1) + 1;
        Hexagon hexa = lista.get(pos);
        HexagonSatelliteData data = (HexagonSatelliteData) hexa.getSatelliteData().get();

        while (data.isVisible()){   //reroll de hexàgon si resulta que era visible
            r = new Random();
            pos = r.nextInt(lista.size() - 1) + 1;
            hexa = lista.get(pos);
            data = (HexagonSatelliteData) hexa.getSatelliteData().get();
        }

        Portal portal = new Portal();
        portal.setXCoord(hexa.getGridX());
        portal.setZCoord(hexa.getGridZ());
        //col.loquem el portal com a element del hexagon, i guardem les coordenades en el nivell
        data.setElement(portal);
        hexa.setSatelliteData(data);
        level.setPortal(portal);
        Log.d("xxx", "\nPlaced portal in: " + hexa.getCubeCoordinate());
    }

    public void cleanBoard(){   //neteja tots els objectes
        hexas.clear();
        built = false;
    }

    public MainActivity getMainActivity() {        return mainActivity;    }
    public void setMainActivity(MainActivity mainActivity) {        this.mainActivity = mainActivity;    }
}
