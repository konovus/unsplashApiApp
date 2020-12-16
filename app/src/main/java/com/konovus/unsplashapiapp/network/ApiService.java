package com.konovus.unsplashapiapp.network;

import com.konovus.unsplashapiapp.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("order_by") String order_by,
                                @Query("page") int page,
                                @Query("client_id") String client_id);
}
