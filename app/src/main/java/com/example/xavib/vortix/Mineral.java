package com.example.xavib.vortix;

/**
 * Created by xaviB on 26/4/17.
 */

public class Mineral extends Obstacle{

    private int value;    //mal que fa al creuar
    private boolean exhausted;

    public Mineral(int value) {
        this.value = value;
        this.exhausted = false;
    }
    public Mineral(){            }

    public int getValue() {        return value;    }
    public void setValue(int value) {        this.value = value;    }

    public boolean isExhausted() {        return exhausted;    }
    public void setExhausted(boolean exhausted) {        this.exhausted = exhausted;    }
}