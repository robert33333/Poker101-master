package com.example.poker101.date;

import java.io.Serializable;

public class Carte implements Serializable {
    public static final String romb="romb";
    public static final String trefla="trefla";
    public static final String inima_neagra="inima_neagra";
    public static final String inima_rosie="inima_rosie";

    int numar;
    String tip;

    public Carte(int numar, String tip) {
        this.numar = numar;
        this.tip = tip;
    }

    public int getNumar() {
        return numar;
    }

    public String getTip() {
        return tip;
    }
}
