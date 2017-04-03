package com.example.xavib.vortix;

//classe de nau

import org.codetome.hexameter.core.api.CubeCoordinate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.backport.Optional;

public class Ship {

    private int hp, scanner = 1, energy, shields, engine;
    private String model;
    private Weapon weapon1, weapon2, weapon3;
    private int shipX, shipZ, imatge;
    private int orientacio = 1;

    //nau de dues armes
    public Ship(int hp, int scanner, int energy, int shields, String model, Weapon weapon1, Weapon weapon2, int engine) {
        this.hp = hp;                this.scanner = scanner;
        this.energy = energy;        this.shields = shields;
        this.model = model;          this.weapon1 = weapon1;
        this.weapon2 = weapon2;      this.engine = engine;
        this.orientacio = 1;
    }

    public int getImatge() {        return imatge;    }
    public void setImatge(int imatge) {        this.imatge = imatge;    }

    public Ship (){

    }

    public Ship (int x, int z){
        this.shipX = x;
        this.shipZ = z;
    }

    public CubeCoordinate getCoordinates(){
        CubeCoordinate coordinate = CubeCoordinate.fromCoordinates(this.getShipX(), this.getShipZ());
        return coordinate;
    }

    public int getOrientacio() {        return orientacio;    }
    public void setOrientacio(int orientacio) {        this.orientacio = orientacio;    }

    public int getShipX() {        return shipX;    }
    public void setShipX(int shipX) {        this.shipX = shipX;    }

    public int getShipZ() {        return shipZ;    }
    public void setShipZ(int shipZ) {        this.shipZ = shipZ;    }

    //nau de tres armes
    public Ship(int hp, int scanner, int energy, int shields, String model, Weapon weapon1, Weapon weapon2, Weapon weapon3, int engine) {
        this.hp = hp;               this.scanner = scanner;
        this.energy = energy;       this.shields = shields;
        this.model = model;         this.weapon1 = weapon1;
        this.weapon2 = weapon2;     this.weapon3 = weapon3;

        this.engine = engine;
    }

    //nau d'una armes
    public Ship(int hp, int scanner, int energy, int shields, String model, Weapon weapon1, int engine) {
        this.hp = hp;              this.scanner = scanner;
        this.energy = energy;      this.shields = shields;
        this.model = model;        this.weapon1 = weapon1;
        this.engine = engine;
    }


    public int getHp() {        return hp;    }
    public void setHp(int hp) {        this.hp = hp;    }

    public int getScanner() {        return scanner;    }
    public void setScanner(int scanner) {        this.scanner = scanner;    }

    public int getEnergy() {        return energy;    }
    public void setEnergy(int energy) {        this.energy = energy;    }

    public int getShields() {        return shields;    }
    public void setShields(int shields) {        this.shields = shields;    }

    public String getModel() {        return model;    }
    public void setModel(String model) {        this.model = model;    }

    public Weapon getWeapon1() {        return weapon1;    }
    public void setWeapon1(Weapon weapon1) {        this.weapon1 = weapon1;    }

    public Weapon getWeapon2() {        return weapon2;    }
    public void setWeapon2(Weapon weapon2) {        this.weapon2 = weapon2;    }

    public Weapon getWeapon3() {        return weapon3;    }
    public void setWeapon3(Weapon weapon3) {        this.weapon3 = weapon3;    }

    public int getEngine() {        return engine;    }
    public void setEngine(int engine) {        this.engine = engine;    }
}
