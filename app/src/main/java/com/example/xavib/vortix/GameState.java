package com.example.xavib.vortix;

/**
 classe d'inicialització i persistència
 */

public class GameState {

    private Ship PlayerShip;
    private Level level;
    private int credits, energy, shields, engine, imatge;

    public GameState() {
        this.level = new Level(1);      //starting level
    }

    public int getCredits() {        return credits;    }
    public void setCredits(int credits) {        this.credits = credits;    }

    public Ship getPlayerShip() {        return PlayerShip;    }
    public void setPlayerShip(Ship playerShip) {        PlayerShip = playerShip;    }

    public int getEnergy() {        return energy;    }
    public void setEnergy(int energy) {       this.energy = energy;    }

    public int getShields() {        return shields;    }
    public void setShields(int shields) {        this.shields = shields;    }

    public int getEngine() {        return engine;    }
    public void setEngine(int engine) {        this.engine = engine;    }

    public int getImatge() {        return imatge;    }
    public void setImatge(int imatge) {        this.imatge = imatge;    }

    public Level getLevel() {        return level;    }
    public void setLevel(Level level) {        this.level = level;    }

    public void updateShip(Ship ship){
        this.PlayerShip = ship;
    }
}
