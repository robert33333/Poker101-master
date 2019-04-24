package com.example.poker101.date;

import java.io.Serializable;
import java.util.ArrayList;

public class Tura implements Serializable {
    public String opponent_action;
    public int opponent_bani;
    public ArrayList<Carte> carti = new ArrayList<>(5);
    public Carte opponent_carte1;
    public Carte opponent_carte2;
    public String winner;
    public int tura_curenta;
}
