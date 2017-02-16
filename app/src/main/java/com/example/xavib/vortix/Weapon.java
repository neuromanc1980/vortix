package com.example.xavib.vortix;

//clase d'armament

public class Weapon {

    private String name, type;
    private int cooldown, damage;

    public Weapon(String name, String type, int cooldown, int damage) {
        this.name = name;           this.type = type;
        this.cooldown = cooldown;   this.damage = damage;
    }

    public String getName() {        return name;    }
    public void setName(String name) {        this.name = name;    }

    public String getType() {        return type;    }
    public void setType(String type) {        this.type = type;    }

    public int getCooldown() {        return cooldown;    }
    public void setCooldown(int cooldown) {        this.cooldown = cooldown;    }

    public int getDamage() {        return damage;    }
    public void setDamage(int damage) {        this.damage = damage;    }

    @Override
    public String toString() {
        return name + '\'' +
                ", type='" + type + '\'' +
                ", cooldown=" + cooldown +
                ", damage=" + damage;
    }

    public void randomWeapon(int level){

    }

}
