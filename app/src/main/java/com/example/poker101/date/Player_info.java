package com.example.poker101.date;

import java.io.Serializable;

public class Player_info implements Serializable {
    public String id_facebook;
    public Carte carte1;
    public Carte carte2;
    public int bani_totali;
    public int bani_pariati;
    public String last_action = "";

    public Player_info(String id_facebook,Carte carte1, Carte carte2, int bani_totali) {
        this.id_facebook = id_facebook;
        this.carte1 = carte1;
        this.carte2 = carte2;

        this.bani_totali = bani_totali;
    }
}
