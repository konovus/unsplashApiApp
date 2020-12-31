package com.konovus.unsplashapiapp.viewmodels;

import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.repositories.MainPhotosRepository;
import com.konovus.unsplashapiapp.responses.PhotoResponse;
import com.konovus.unsplashapiapp.responses.SearchPhotoResponse;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainPhotosViewModel extends ViewModel {

    private MainPhotosRepository mainPhotosRepository;

    public MainPhotosViewModel(){
        mainPhotosRepository = new MainPhotosRepository();
    }

    public LiveData<List<Photo>> getMainPhotos(int page, int per_page, String order_by, String client_id){
        return mainPhotosRepository.getMainPhotos(page, per_page, order_by, client_id);
    }

    public LiveData<SearchPhotoResponse> searchPhotos(int page, int per_page, String query, String client_id){
        return mainPhotosRepository.searchPhotos(page, per_page, query, client_id);
    }
}
