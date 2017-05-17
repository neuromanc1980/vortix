package com.example.xavib.vortix;

/**
 * Created by xaviB on 9/5/17.
 */

public class Nebula extends Obstacle{

    private int density;    //fuel que drena

    public Nebula(int density) {
        this.density = density;
    }

    public Nebula(){            }

    public int getDensity() {        return density;    }
    public void setDensity(int density) {        this.density = density;    }
}
