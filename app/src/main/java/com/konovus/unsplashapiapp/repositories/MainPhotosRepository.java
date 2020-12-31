package com.konovus.unsplashapiapp.repositories;

import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.network.ApiClient;
import com.konovus.unsplashapiapp.network.ApiService;
import com.konovus.unsplashapiapp.responses.SearchPhotoResponse;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPhotosRepository {

    private ApiService apiService;

    public MainPhotosRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<List<Photo>> getMainPhotos(int page, int per_page, String order_by, String client_id){
        MutableLiveData<List<Photo>> photos = new MutableLiveData<>();
        apiService.getPhotos(order_by, page, per_page, client_id).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                photos.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                photos.setValue(null);
            }
        });
        return photos;
    }

    public LiveData<SearchPhotoResponse> searchPhotos(int page, int per_page,String query, String client_id){
        MutableLiveData<SearchPhotoResponse> photosResponse = new MutableLiveData<>();
        apiService.searchPhotos(query, page, per_page, client_id).enqueue(new Callback<SearchPhotoResponse>() {
            @Override
            public void onResponse(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response) {
                photosResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SearchPhotoResponse> call, Throwable t) {
                photosResponse.setValue(null);
            }
        });
        return photosResponse;
    }
}
