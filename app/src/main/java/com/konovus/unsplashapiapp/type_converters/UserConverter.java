package com.konovus.unsplashapiapp.type_converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.konovus.unsplashapiapp.models.Urls;
import com.konovus.unsplashapiapp.models.User;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class UserConverter {
    @TypeConverter
    public String fromUser(User user){
        if(user == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        String json = gson.toJson(user, type);
        return json;
    }
    @TypeConverter
    public User toUser(String user) {
        if (user == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        return gson.fromJson(user, type);
    }
}
