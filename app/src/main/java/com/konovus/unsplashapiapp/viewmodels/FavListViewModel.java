package com.konovus.unsplashapiapp.viewmodels;

import android.app.Application;

import com.konovus.unsplashapiapp.database.PhotosDatabase;
import com.konovus.unsplashapiapp.models.Photo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class FavListViewModel extends AndroidViewModel {

    private PhotosDatabase photosDatabase;

    public FavListViewModel(@NonNull Application application) {
        super(application);
        photosDatabase = PhotosDatabase.getPhotosDatabase(application);
    }

    public Flowable<List<Photo>> getFavList(){
        return photosDatabase.photoDao().getFavList();
    };

    public Completable removeFromFavList(Photo photo){
        return photosDatabase.photoDao().deleteFromFavList(photo);
    }
}
