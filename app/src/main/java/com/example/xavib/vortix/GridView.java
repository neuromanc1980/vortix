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
import android.widget.TextView;

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
    HexagonalGridCalculator hexCalc;

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

        super.draw(canvas);
        if (gameState == null) return;
        this.postInvalidateDelayed(150); //taxa de refresc

        //gameState = mainActivity.getGameState();    //carreguem gameState

        if (!built) {
            built = true;       //només construirem una vegada


            Log.d("xxx", "\nBuilding grid level: "   + gameState.getLevel().getLevel()         );

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
            hexCalc = builder.buildCalculatorFor(grid);


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

            mainActivity.refreshStats();

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
                if (data.getElement() instanceof Portal && data.isVisible()){
                    Bitmap portalBm = BitmapFactory.decodeResource(getResources(), R.drawable.portal);
                    canvas.drawBitmap(portalBm, (float) hexagon.getCenterX() - portalBm.getHeight()/2, (float) hexagon.getCenterY() - portalBm.getWidth()/2, new Paint() );
                }

                //station
                if (data.getElement() instanceof Station && data.isVisible()){
                    Bitmap stationBm = BitmapFactory.decodeResource(getResources(), R.drawable.station);
                    canvas.drawBitmap(stationBm, (float) hexagon.getCenterX() - stationBm.getHeight()/2, (float) hexagon.getCenterY() - stationBm.getWidth()/2, new Paint() );
                }

                //asteroides
                if (data.getElement() instanceof Asteroid && data.isVisible()){
                    Bitmap asteroidBm = BitmapFactory.decodeResource(getResources(), R.raw.asteroides1bueno); //default
                    if (((Asteroid) data.getElement()).getDensity() <= 5) { asteroidBm = BitmapFactory.decodeResource(getResources(), R.raw.asteroides1bueno); }
                    if ((((Asteroid) data.getElement()).getDensity() > 5) && (((Asteroid) data.getElement()).getDensity() <= 25)) { asteroidBm = BitmapFactory.decodeResource(getResources(), R.raw.asteroides2bueno); }
                    if (((Asteroid) data.getElement()).getDensity() > 25) { asteroidBm = BitmapFactory.decodeResource(getResources(), R.raw.asteroides3bueno); }

                    canvas.drawBitmap(asteroidBm, (float) hexagon.getCenterX() - asteroidBm.getHeight()/2, (float) hexagon.getCenterY() - asteroidBm.getWidth()/2, new Paint() );
                }

                //minerals
                if (data.getElement() instanceof Mineral && data.isVisible()){
                    Bitmap mineralBm = BitmapFactory.decodeResource(getResources(), R.drawable.minerals1); //default
                    if (((Mineral) data.getElement()).getValue() <= 20) { mineralBm = BitmapFactory.decodeResource(getResources(), R.drawable.minerals1x); }
                    if (((Mineral) data.getElement()).getValue() > 20) { mineralBm = BitmapFactory.decodeResource(getResources(), R.drawable.minerals2x); }
                    if (((Mineral) data.getElement()).isExhausted()) { mineralBm = BitmapFactory.decodeResource(getResources(), R.drawable.planet); }
                    canvas.drawBitmap(mineralBm, (float) hexagon.getCenterX() - mineralBm.getHeight()/2, (float) hexagon.getCenterY() - mineralBm.getWidth()/2, new Paint() );
                }

                //nebula
                if (data.getElement() instanceof Nebula && data.isVisible()){
                    Bitmap nebulaBm = BitmapFactory.decodeResource(getResources(), R.drawable.nebula); //default
                    if (((Nebula) data.getElement()).getDensity() == 2) { nebulaBm = BitmapFactory.decodeResource(getResources(), R.drawable.nebula); }
                    if (((Nebula) data.getElement()).getDensity() == 3) { nebulaBm = BitmapFactory.decodeResource(getResources(), R.drawable.nebula2); }
                    canvas.drawBitmap(nebulaBm, (float) hexagon.getCenterX() - nebulaBm.getHeight()/2, (float) hexagon.getCenterY() - nebulaBm.getWidth()/2, new Paint() );
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
                if (playerShip.getShields() > 0){   //dibuixem escuts
                    Bitmap shieldsBM = BitmapFactory.decodeResource(getResources(), R.drawable.shields2);
                    canvas.drawBitmap(shieldsBM, (float) hexagon.getCenterX() - shieldsBM.getHeight()/2, (float) hexagon.getCenterY() - shieldsBM.getWidth()/2, new Paint() );
                }
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

        if (!level.isStationPlaced()){
            placeStation(hexas);
            level.setStationPlaced(true);
        }

        if (!level.isPortalPlaced()){
            Log.d("xxx", "\nPortal is not placed yet, placing it.....: ");
            placePortal(hexas);
            level.setPortalPlaced(true);
            placeElements(hexas);
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

                touchedHexagon =  grid.getByPixelCoordinate(x,y);

                if (touchedHexagon.isPresent()){

                    if (hexCalc.calculateDistanceBetween(touchedHexagon.get(), shipHexagon(playerShip).get())>1) { return false; }  //no considerem el touch de hexàgons llunyans
                    if (playerShip.getCoordinates().equals(touchedHexagon.get().getCubeCoordinate())){            return false; } //no activa el propi hexagon

                Optional<HexagonSatelliteData> dataTouched = touchedHexagon.get().getSatelliteData();
                Log.d("xxx", "\nHexagon: " + touchedHexagon.get().getCubeCoordinate() + " visible?: "+dataTouched.get().isVisible() + " moveable?: "+dataTouched.get().isMoveable() + " element: " + dataTouched.get().getElement());
                //Log.d("xxx", "\nPortal is in: " + level.getPortal().getXCoord() + " <= X/Z => " + level.getPortal().getZCoord());

                    if (dataTouched.get().isMoveable()){ //ens movem a la casella


                        mainActivity.updateFuel();
                        mainActivity.infoBox("");   //netejem text box
                        //gameState.getPlayerShip().setEnergy(gameState.getPlayerShip().getEnergy()-5+gameState.getPlayerShip().getEngine());

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
                    if (dataTouched.get().getElement() instanceof Portal && dataTouched.get().isVisible() ){
                        level = new Level (min(level.getLevel()+1,15)); //pujem de nivell

                        gameState.setLevel(level);

                        //play sound portal
                        mainActivity.playSound(R.raw.portal);

                        playerShip.setShipX(gameState.getLevel().getStartingX());   //recol.loquem la nau
                        playerShip.setShipZ(gameState.getLevel().getStartingZ());
                        mainActivity.updateBackground(gameState.getLevel().getBackground());
                        mainActivity.song.stop();
                        mainActivity.playSong(level.getLevel());
                        cleanBoard();
                        //canviar nivell label
                        mainActivity.mensaje.setTextColor(Color.YELLOW);
                        mainActivity.infoBox("You found an ancient portal to a new sector!");

                        Log.d("xxx", "\nEnter portal");
                        Log.d("xxx", "\nBackground: "+ gameState.getLevel().getBackground());
                    }

                    //asteroides
                    if (dataTouched.get().getElement() instanceof Asteroid && dataTouched.get().isVisible()){
                        mainActivity.playSound(R.raw.asteroid);
                        int damage = ((Asteroid) dataTouched.get().getElement()).getDensity();
                        mainActivity.updateHPShield(damage);
                        mainActivity.mensaje.setTextColor(Color.CYAN);
                        mainActivity.infoBox("You crossed an asteroid field, and suffered "+damage+" damage!");
                    }

                    //minerals - exhausted
                    if (dataTouched.get().getElement() instanceof Mineral && dataTouched.get().isVisible() && ((Mineral) dataTouched.get().getElement()).isExhausted()){
                        mainActivity.mensaje.setTextColor(Color.RED);
                        mainActivity.infoBox("Planet does not have any more useful resources!");
                    }

                    //minerals
                    if (dataTouched.get().getElement() instanceof Mineral && dataTouched.get().isVisible() && ((Mineral) dataTouched.get().getElement()).isExhausted() == false){
                        int minerals = ((Mineral) dataTouched.get().getElement()).getValue();
                        playerShip.setCredits(playerShip.getCredits()+minerals);
                        ((Mineral) dataTouched.get().getElement()).setExhausted(true);
                        mainActivity.playSound(R.raw.powerup);
                        mainActivity.mensaje.setTextColor(Color.YELLOW);
                        mainActivity.infoBox("You collected "+minerals+" credits in minerals!");
                    }

                    //nebula
                    if (dataTouched.get().getElement() instanceof Nebula && dataTouched.get().isVisible()){
                        int fuel = ((Nebula) dataTouched.get().getElement()).getDensity();
                        playerShip.setEnergy(playerShip.getEnergy()-((Nebula) dataTouched.get().getElement()).getDensity());
                        mainActivity.playSound(R.raw.pulse);
                        mainActivity.mensaje.setTextColor(Color.WHITE);
                        mainActivity.infoBox("Nebula area drained "+fuel+" fuel!");
                    }

                    //station
                    if (dataTouched.get().getElement() instanceof Station && dataTouched.get().isVisible()){

                        if (playerShip.getCredits() >= 25){
                            playerShip.setEnergy(playerShip.getMaxenergy());
                            playerShip.setCredits(playerShip.getCredits()-25);
                            mainActivity.playSound(R.raw.pulse);
                            mainActivity.mensaje.setTextColor(Color.BLUE);
                            mainActivity.infoBox("Filled fuel tanks at a cost of 25 credits.");
                        }   else {
                            mainActivity.mensaje.setTextColor(Color.RED);
                            mainActivity.infoBox("You need at least 25 credits to refuel.");
                        }
                    }

                }
                break;
        }
        mainActivity.refreshStats();
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

    public void placeStation(List <Hexagon> lista){      //mètode per col.locar la estació

        //triem un hexagon a l'atzar
        Random r = new Random();
        int pos = r.nextInt(lista.size() - 1) + 1;
        Hexagon hexa = lista.get(pos);
        HexagonSatelliteData data = (HexagonSatelliteData) hexa.getSatelliteData().get();

        while (data.isVisible() && data.getElement() == null){   //reroll de hexàgon si resulta que era visible i no te portal
            r = new Random();
            pos = r.nextInt(lista.size() - 1) + 1;
            hexa = lista.get(pos);
            data = (HexagonSatelliteData) hexa.getSatelliteData().get();
        }

        Station station = new Station();
        station.setXCoord(hexa.getGridX());
        station.setZCoord(hexa.getGridZ());
        //col.loquem la estació com a element del hexagon, i guardem les coordenades en el nivell
        data.setElement(station);
        hexa.setSatelliteData(data);
        level.setStation(station);
        Log.d("xxx", "\nPlaced station in: " + hexa.getCubeCoordinate());
    }

    public void cleanBoard(){   //neteja tots els objectes
        hexas.clear();
        built = false;
    }

    public void placeElements(List <Hexagon> lista){

        //-----------------------------------------------   ASTEROIDES   ------------------------------------------------
        Random r = new Random();
        int asteroidsNumber = r.nextInt(gameState.getLevel().getLevel()/2 +2) + 2;

        Log.d("xxx", "\nPlacing "+asteroidsNumber+" asteroids " );

        for (int i = 0; i <= asteroidsNumber ; i++){        //posem asteroidsNumber asteroids

            //triem un hexagon a l'atzar
            Random r2 = new Random();
            int pos = r2.nextInt(lista.size() - 1) + 1;
            Hexagon hexa = lista.get(pos);
            HexagonSatelliteData data = (HexagonSatelliteData) hexa.getSatelliteData().get();
                if (data.getElement() != null){           i++;        } //ja està ocupat
                else{
                    Asteroid asteroid = new Asteroid();
                    asteroid.setXCoord(hexa.getGridX());
                    asteroid.setZCoord(hexa.getGridZ());

                    //densitat
                    Random r3 = new Random();
                    int daño = r3.nextInt(gameState.getLevel().getLevel()*2 ) + 1;
                    int densitat = r2.nextInt(gameState.getLevel().getLevel()*10 ) + 1;
                        if (densitat < 25)  { asteroid.setDensity(daño);  }
                        if (densitat < 50)  { asteroid.setDensity(daño*2);  }
                        if (densitat >= 50)  { asteroid.setDensity(daño*3);  }
                    Log.d("xxx", "\nDensitat "+asteroid.getDensity()+" a l'asteroide coordinades: X: "+hexa.getGridX()+" Z:"+hexa.getGridZ() );
                    data.setElement(asteroid);
                    hexa.setSatelliteData(data);
                }
            }

        //-----------------------------------------------   MINERALS   ------------------------------------------------
        int mineralsNumber = r.nextInt(gameState.getLevel().getLevel()/3 +1) + 1;

        Log.d("xxx", "\nPlacing "+mineralsNumber+" minerals " );

        for (int i = 0; i <= mineralsNumber ; i++){        //posem asteroidsNumber asteroids

            //triem un hexagon a l'atzar
            Random r2 = new Random();
            int pos = r2.nextInt(lista.size() - 1) + 1;
            Hexagon hexa = lista.get(pos);
            HexagonSatelliteData data = (HexagonSatelliteData) hexa.getSatelliteData().get();
            if (data.getElement() != null){           i++;        } //ja està ocupat
            else{
                Mineral mineral = new Mineral();
                mineral.setXCoord(hexa.getGridX());
                mineral.setZCoord(hexa.getGridZ());
                //valor del mineral
                Random r3 = new Random();
                int value = r2.nextInt(gameState.getLevel().getLevel()*4 ) + 10 + gameState.getLevel().getLevel()*2;
                if (value < 20)  { mineral.setValue(value);  }
                if (value >= 20)  { mineral.setValue(value);  }
                Log.d("xxx", "\nValor "+mineral.getValue()+" del mineral a coordinades: X: "+hexa.getGridX()+" Z:"+hexa.getGridZ() );
                data.setElement(mineral);
                hexa.setSatelliteData(data);
            }
        }

        //-----------------------------------------------   NEBULA   ------------------------------------------------
        int nebulaNumber = r.nextInt(gameState.getLevel().getLevel()/3 +1) + 1;

        Log.d("xxx", "\nPlacing "+nebulaNumber+" minerals " );

        for (int i = 0; i <= nebulaNumber ; i++){        //posem asteroidsNumber asteroids

            //triem un hexagon a l'atzar
            Random r2 = new Random();
            int pos = r2.nextInt(lista.size() - 1) + 1;
            Hexagon hexa = lista.get(pos);
            HexagonSatelliteData data = (HexagonSatelliteData) hexa.getSatelliteData().get();
            if (data.getElement() != null){           i++;        } //ja està ocupat
            else{
                Nebula nebula = new Nebula();
                nebula.setXCoord(hexa.getGridX());
                nebula.setZCoord(hexa.getGridZ());
                //valor de la nebula
                Random r3 = new Random();
                int value = r2.nextInt(gameState.getLevel().getLevel()*5 ) + 1;
                if (value < 20)  { nebula.setDensity(2);  }
                if (value >= 20)  { nebula.setDensity(3);  }

                Log.d("xxx", "\nValor "+nebula.getDensity()+" de la nebula a coordinades: X: "+hexa.getGridX()+" Z:"+hexa.getGridZ() );
                data.setElement(nebula);
                hexa.setSatelliteData(data);
            }
        }
    }

    public MainActivity getMainActivity() {        return mainActivity;    }
    public void setMainActivity(MainActivity mainActivity) {        this.mainActivity = mainActivity;    }
    public Optional<Hexagon> shipHexagon(Ship ship) {    Optional<Hexagon> position = grid.getByCubeCoordinate(ship.getCoordinates());  return position;               }

}
