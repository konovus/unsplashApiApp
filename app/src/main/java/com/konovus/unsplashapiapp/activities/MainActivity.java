package com.konovus.unsplashapiapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.adapters.PhotoAdapter;
import com.konovus.unsplashapiapp.databinding.ActivityMainBinding;
import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.viewmodels.MainPhotosViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoAdapter.PhotoListener {

    private ActivityMainBinding activityMainBinding;
    private MainPhotosViewModel mainPhotosViewModel;
    private List<Photo> photos = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private int currentPage = 1;
    private int totalPages = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        doInitialization();
    }

    private void doInitialization(){
        mainPhotosViewModel = new ViewModelProvider(this).get(MainPhotosViewModel.class);
        photoAdapter = new PhotoAdapter(photos, this, this);
        activityMainBinding.recyclerView.setItemViewCacheSize(1000);
        activityMainBinding.recyclerView.setHasFixedSize(true);
        activityMainBinding.recyclerView.setAdapter(photoAdapter);
        activityMainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        activityMainBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    getMainPhotos();
                }
            }
        });
        getMainPhotos();
    }

    private void getMainPhotos(){
        toggleLoading();
        mainPhotosViewModel.getMainPhotos(currentPage, "popular", "CYn6YcuwIT4PsQPnKT656mLrfDBQCR_37tZk8JTry5k")
                .observe(this, photosResponse ->{
            toggleLoading();
            if(photosResponse != null && !photosResponse.isEmpty()){
                int oldCount = photos.size();
                photos.addAll(photosResponse);
                photoAdapter.notifyItemRangeInserted(oldCount, oldCount+=10);
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading())
                activityMainBinding.setIsLoading(false);
            else
                activityMainBinding.setIsLoading(true);
        } else {
            if (activityMainBinding.getIsLoadingMore())
                activityMainBinding.setIsLoadingMore(false);
            else
                activityMainBinding.setIsLoadingMore(true);
        }
    }

    @Override
    public void onFavoriteClicked(Photo photo) {

        Toast.makeText(this, "Clicked on Favorite " + photo.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadClicked(Photo photo) {

    }




}
