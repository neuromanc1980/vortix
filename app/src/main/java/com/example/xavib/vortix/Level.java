package com.example.xavib.vortix;

//els objectes level contenen tots els elements intrínsecs de cada nivell


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {

    private int level, startingX, startingZ;
    private int background;
    private int gridH, gridW;
    private double mod;
    private Paint linea = new Paint();
    private String center;
    private int obstacles;


    //constructor
    public Level(int level) {

        this.level = level;

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
            case 3:                this.background = (R.drawable.lvl2);                 break;
            case 4:                this.background = (R.drawable.lvl2);                 break;
            case 5:                this.background = (R.drawable.lvl2);                 break;
            case 6:                this.background = (R.drawable.lvl2);                 break;
            case 7:                this.background = (R.drawable.lvl2);                 break;
            case 8:                this.background = (R.drawable.lvl2);                 break;
            case 9:                this.background = (R.drawable.lvl2);                 break;
            case 10:                this.background = (R.drawable.lvl2);                break;
            case 11:                this.background = (R.drawable.lvl2);                break;
            case 12:                this.background = (R.drawable.lvl2);                break;
            case 13:                this.background = (R.drawable.lvl2);                break;
            case 14:                this.background = (R.drawable.lvl2);                break;
            case 15:                this.background = (R.drawable.lvl2);                break;
        }

        Random random = new Random();   int min = this.level;   int max = this.level*2 + 1;
        this.obstacles = random.nextInt(max-min) + min;

    }

    public int getStartingX() {        return startingX;    }
    public void setStartingX(int startingX) {        this.startingX = startingX;    }

    public int getStartingZ() {        return startingZ;    }
    public void setStartingZ(int startingZ) {        this.startingZ = startingZ;    }

    public int getLevel() {        return level;    }
    public void setLevel(int level) {        this.level = level;    }

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
}
