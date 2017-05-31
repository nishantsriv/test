package com.self.clothchooser.database;

/**
 * Created by nishant on 14-05-2017.
 */

public class ClothPair {
    int id;
    byte[] shirt, pant;

    public ClothPair(int id, byte[] shirt, byte[] pant) {
        this.id = id;
        this.shirt = shirt;
        this.pant = pant;
    }

    public int getId() {
        return id;
    }

    public byte[] getShirt() {
        return shirt;
    }

    public byte[] getPant() {
        return pant;
    }
}
