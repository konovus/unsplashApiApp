package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;

@Entity(tableName = "users")
public class User implements Serializable {

    private String id;
    private String username;
    private String name;
    private ProfileImage profileImage;

    public User(String id, String username, String name, ProfileImage profileImage) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.profileImage = profileImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }
}
