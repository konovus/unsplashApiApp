package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;

@Entity(tableName = "photos")
public class Photo implements Serializable {

    private String id;
    private int likes;
    private Urls urls;
    private User user;

    public Urls getUrls() {
        return urls;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public User getUser() {
        return user;
    }
}
