package com.example.xavib.vortix;

/**
 * Created by xaviB on29/3/17.
 */

public class Obstacle {

    private String type;
    private int XCoord, ZCoord, imatge;
    private boolean passable;

    public String getType() {        return type;    }
    public void setType(String type) {
        this.type = type;
    }

    public int getXCoord() {        return XCoord;    }
    public void setXCoord(int XCoord) {
        this.XCoord = XCoord;
    }

    public int getZCoord() {        return ZCoord;    }
    public void setZCoord(int ZCoord) {        this.ZCoord = ZCoord;    }

    public int getImatge() {        return imatge;    }
    public void setImatge(int imatge) {        this.imatge = imatge;    }

    public boolean isPassable() {        return passable;    }
    public void setPassable(boolean passable) {        this.passable = passable;    }
}
