package com.example.xavib.vortix;

//els objectes level contenen tots els elements intrínsecs de cada nivell


import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Level {

    private int level;
    private int startingX, startingZ;
    private int background;
    private int gridH, gridW;
    private double mod;
    private Paint linea = new Paint();
    private String center;

    public boolean isPortalPlaced() {        return portalPlaced;    }
    public void setPortalPlaced(boolean portalPlaced) {        this.portalPlaced = portalPlaced;    }

    private int obstacles;
    private Portal portal;
    private Station station;
    private boolean portalPlaced;
    private boolean stationPlaced;


    //constructor
    public Level(int level) {

        this.level = level;
        this.portalPlaced = false;
        this.stationPlaced = false;

        //stats segons el nivell
        if (this.level < 6){
            this.gridH = 5;            this.gridW = 5;      this.mod = 8.1;
            this.linea.setColor(Color.rgb(153, 255, 204));  center = "1,2";
            this.linea.setStrokeWidth(4);        this.linea.setAlpha(60);        this.linea.setStyle(Paint.Style.FILL);
            this.startingX=1;     this.startingZ=2;
        }

        if ( (this.level > 5) && (this.level < 10) ) {
            this.gridH = 7;            this.gridW = 7;      this.mod = 11;
            this.linea.setColor(Color.rgb(255, 204, 153));  center = "2,3";
            this.linea.setStrokeWidth(4);        this.linea.setAlpha(60);        this.linea.setStyle(Paint.Style.FILL);
            this.startingX=2;     this.startingZ=3;
        }

        if (this.level >9 ){
            this.gridH = 9;            this.gridW = 9;      this.mod = 14;
            this.linea.setColor(Color.rgb(204, 153, 255));  center = "2,4";
            this.linea.setStrokeWidth(4);        this.linea.setAlpha(60);        this.linea.setStyle(Paint.Style.FILL);
            this.startingX=2;     this.startingZ=4;
        }


        switch (level) {
            case 1:                this.background = (R.drawable.lvl1);                 break;
            case 2:                this.background = (R.drawable.lvl1);                 break;
            case 3:                this.background = (R.drawable.lvl1);                 break;
            case 4:                this.background = (R.drawable.lvl2);                 break;
            case 5:                this.background = (R.drawable.lvl2);                 break;
            case 6:                this.background = (R.drawable.lvl3);                 break;
            case 7:                this.background = (R.drawable.lvl3);                 break;
            case 8:                this.background = (R.drawable.lvl4);                 break;
            case 9:                this.background = (R.drawable.lvl4);                 break;
            case 10:                this.background = (R.drawable.lvl5);                break;
            case 11:                this.background = (R.drawable.lvl6);                break;
            case 12:                this.background = (R.drawable.lvl7);                break;
            case 13:                this.background = (R.drawable.lvl8);                break;
            case 14:                this.background = (R.drawable.lvl9);                break;
            case 15:                this.background = (R.drawable.lvl10);               break;
            default:                this.background = (R.drawable.lvl10);               break;
        }

        Random random = new Random();   int min = this.level;   int max = this.level*2 + 1;
        this.obstacles = random.nextInt(max-min) + min;

        this.portal = new Portal();
        this.station = new Station();


    }

    public int getStartingX() {        return startingX;    }
    public void setStartingX(int startingX) {        this.startingX = startingX;    }

    public int getStartingZ() {        return startingZ;    }
    public void setStartingZ(int startingZ) {        this.startingZ = startingZ;    }

    public int getLevel() {        return level;    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getBackground() {        return background;    }
    public void setBackground(int background) {        this.background = background;    }

    public int getGridH() {        return gridH;    }
    public void setGridH(int gridH) {        this.gridH = gridH;    }

    public int getGridW() {        return gridW;    }
    public void setGridW(int gridW) {        this.gridW = gridW;    }

    public double getMod() {        return mod;    }
    public void setMod(float mod) {        this.mod = mod;    }

    public Paint getLinea() {        return linea;    }
    public void setLinea(Paint linea) {        this.linea = linea;    }

    public String getCenter() {        return center;    }
    public void setCenter(String center) {        this.center = center;    }

    public Portal getPortal() {        return portal;    }
    public void setPortal(Portal portal) {        this.portal = portal;    }

    public boolean isStationPlaced() {        return stationPlaced;    }
    public void setStationPlaced(boolean stationPlaced) {        this.stationPlaced = stationPlaced;    }

    public Station getStation() {        return station;    }
    public void setStation(Station station) {        this.station = station;    }
}
