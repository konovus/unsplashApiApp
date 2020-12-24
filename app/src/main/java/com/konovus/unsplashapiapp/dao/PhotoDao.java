package com.konovus.unsplashapiapp.dao;

import com.konovus.unsplashapiapp.models.Photo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos")
    Flowable<List<Photo>> getFavList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToFavList(Photo photo);

    @Delete
    Completable deleteFromFavList(Photo photo);

    @Query("SELECT * FROM photos WHERE id= :photoId")
    Flowable<Photo> getPhotoFromFavList(String photoId);
}
