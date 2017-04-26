package com.example.xavib.vortix;

/**
 * Created by xaviB on 26/4/17.
 */

public class Mineral extends Obstacle{

    private int value;    //mal que fa al creuar

    public Mineral(int value) {
        this.value = value;
    }
    public Mineral(){            }

    public int getValue() {        return value;    }
    public void setValue(int value) {        this.value = value;    }
}