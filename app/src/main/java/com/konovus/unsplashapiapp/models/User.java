package com.konovus.unsplashapiapp.models;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Serializable {

    @PrimaryKey
    private String id;
    private String username;
    private String name;

    public User(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
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



    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

}
