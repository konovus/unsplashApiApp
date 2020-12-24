package com.konovus.unsplashapiapp.type_converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.konovus.unsplashapiapp.models.Urls;
import com.konovus.unsplashapiapp.models.User;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class UrlsConverters {

    @TypeConverter
    public String fromUrls(Urls urls){
        if(urls == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<Urls>() {}.getType();
        String json = gson.toJson(urls, type);
        return json;
    }
    @TypeConverter
    public Urls toUrls(String urls) {
        if (urls == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<Urls>() {}.getType();
        return gson.fromJson(urls, type);
    }

}
