package com.example.xavib.vortix;

/**
 * Created by xaviB on 29/3/17.
 */

public class Asteroid extends Obstacle{

    private int density;    //mal que fa al creuar

    public Asteroid(int density) {
        this.density = density;
    }

    public Asteroid(){            }

    public int getDensity() {        return density;    }
    public void setDensity(int density) {        this.density = density;    }
}
