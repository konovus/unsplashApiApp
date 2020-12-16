package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;

@Entity(tableName = "profile_images")
public class ProfileImage implements Serializable {

    private String small;
    private String medium;
    private String large;

    public ProfileImage(String small, String medium, String large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getLarge() {
        return large;
    }
}
