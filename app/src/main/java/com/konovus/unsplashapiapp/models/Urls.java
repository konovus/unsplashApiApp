package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "urls")
public class Urls implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String regular;
    private String thumb;
    private String small;
    private String full;

    public Urls(int id, String regular, String thumb, String small, String full) {
        this.id = id;
        this.regular = regular;
        this.thumb = thumb;
        this.small = small;
        this.full = full;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
