package com.konovus.unsplashapiapp.models;

import com.konovus.unsplashapiapp.type_converters.UrlsConverters;
import com.konovus.unsplashapiapp.type_converters.UserConverter;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "photos")
public class Photo implements Serializable {

    @NonNull
    @PrimaryKey
    private String id;
    private int likes;
    @TypeConverters(UrlsConverters.class)
    private Urls urls;
    @TypeConverters(UserConverter.class)
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
