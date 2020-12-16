package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;

public class Urls {

    private String regular;
    private String thumb;
    private String small;
    private String full;

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public String getThumb() {
        return thumb;
    }

    public String getSmall() {
        return small;
    }
}
