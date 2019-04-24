package com.example.poker101.date;

import java.io.Serializable;

public class Invite implements Serializable {
    public String from_id_facebook;
    public String for_id_facebook;

    public Invite(String from_id_facebook, String for_id_facebook) {
        this.from_id_facebook = from_id_facebook;
        this.for_id_facebook = for_id_facebook;
    }
}
