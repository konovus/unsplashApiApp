package com.konovus.unsplashapiapp.database;

import android.content.Context;

import com.konovus.unsplashapiapp.dao.PhotoDao;
import com.konovus.unsplashapiapp.models.Photo;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = Photo.class, version = 1, exportSchema = false)
public abstract class PhotosDatabase extends RoomDatabase  {

    private static PhotosDatabase photosDatabase;

    public static synchronized PhotosDatabase getPhotosDatabase(Context context){
        if(photosDatabase == null)
            photosDatabase = Room.databaseBuilder(context,
                    PhotosDatabase.class, "photos_db").build();
        return photosDatabase;
    }

    public abstract PhotoDao photoDao();

}
