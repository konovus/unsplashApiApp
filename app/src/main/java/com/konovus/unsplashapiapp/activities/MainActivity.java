package com.konovus.unsplashapiapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.adapters.PhotoAdapter;
import com.konovus.unsplashapiapp.databinding.ActivityMainBinding;
import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.responses.SearchPhotoResponse;
import com.konovus.unsplashapiapp.viewmodels.MainPhotosViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements PhotoAdapter.PhotoListener {

    private ActivityMainBinding activityMainBinding;
    private MainPhotosViewModel mainPhotosViewModel;
    private List<Photo> photos = new ArrayList<>();
    private List<Photo> search_photos = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private int currentPage = 1;
    private int totalPages = 10000;
    private Timer timer;
    private boolean isSearch;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        mainPhotosViewModel = new ViewModelProvider(this).get(MainPhotosViewModel.class);
        photoAdapter = new PhotoAdapter(photos, this, this);
        activityMainBinding.recyclerView.setItemViewCacheSize(200);
        activityMainBinding.recyclerView.setHasFixedSize(true);
        activityMainBinding.recyclerView.setAdapter(photoAdapter);
        activityMainBinding.recyclerView.setLayoutManager(new WrapStaggeredLayout(2, StaggeredGridLayoutManager.VERTICAL));
//        activityMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityMainBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    if(!isSearch)
                        getMainPhotos();
                    else
                        searchPhotos(query);
                }
                if(recyclerView.canScrollVertically(1))
                    if(activityMainBinding.getIsLoadingMore() || activityMainBinding.getIsLoading()) {
                        activityMainBinding.setIsLoadingMore(false);
                        activityMainBinding.setIsLoading(false);
                    }

            }
        });
        activityMainBinding.favouritesImg.setOnClickListener(v -> startActivity(new Intent(this, FavoritesActivity.class)));
        activityMainBinding.searchImg.setOnClickListener(v -> {
            if(activityMainBinding.searchImg.getTag() == null || activityMainBinding.searchImg.getTag().toString().isEmpty()) {
                activityMainBinding.searchImg.setTag("Close");
                activityMainBinding.searchImg.setImageResource(R.drawable.ic_close);
                activityMainBinding.searchEt.setVisibility(View.VISIBLE);
                activityMainBinding.headerText.setVisibility(View.GONE);
                activityMainBinding.searchEt.requestFocus();
                toggleKeyboard(this);
            } else {
                activityMainBinding.searchImg.setTag("");
                activityMainBinding.searchImg.setImageResource(R.drawable.ic_search);
                activityMainBinding.searchEt.setVisibility(View.GONE);
                activityMainBinding.headerText.setVisibility(View.VISIBLE);
                hideKeyboardFrom(this, v);
            }


        });
        activityMainBinding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();

                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage = 1;
                                    totalPages = 1;
                                    search_photos.clear();
                                    searchPhotos(query);
                                }
                            });
                        }
                    }, 800);
                } else if(isSearch && s.toString().isEmpty()){
                    isSearch = false;
                    currentPage = 1;
                    photos.clear();
                    search_photos.clear();
                    getMainPhotos();
                }
            }
        });
        getMainPhotos();
    }
    private void searchPhotos(String query) {
        isSearch = true;
        toggleLoading();
        mainPhotosViewModel.searchPhotos(currentPage, 20, query, "CYn6YcuwIT4PsQPnKT656mLrfDBQCR_37tZk8JTry5k")
                .observe(this, new Observer<SearchPhotoResponse>() {
                @Override
            public void onChanged(SearchPhotoResponse searchPhotoResponse) {
                toggleLoading();
                if (searchPhotoResponse != null) {
                    totalPages = searchPhotoResponse.getTotal_pages();
                    int oldCount = search_photos.size();
                    search_photos.addAll(searchPhotoResponse.getResults());
                    if(oldCount == 0) {
                        photoAdapter.setPhotos(search_photos, null);
                        activityMainBinding.recyclerView.smoothScrollToPosition(0);
                    } else
                        photoAdapter.notifyItemRangeInserted(oldCount, search_photos.size());
                } else Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getMainPhotos(){
        toggleLoading();
        mainPhotosViewModel.getMainPhotos(currentPage, 20, "latest", "CYn6YcuwIT4PsQPnKT656mLrfDBQCR_37tZk8JTry5k")
                .observe(this, photosResponse ->{
            toggleLoading();
            if(photosResponse != null && !photosResponse.isEmpty()){
                int oldCount = photos.size();
                photos.addAll(photosResponse);
                if(oldCount == 0) {
                    photoAdapter.setPhotos(photos, null);
                    activityMainBinding.recyclerView.smoothScrollToPosition(0);
                } else
                    photoAdapter.notifyItemRangeInserted(oldCount, oldCount+=20);
            } else Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_LONG).show();
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
    public void onPhotoClicked(Photo photo, View view, int position) {
        Intent intent = new Intent(MainActivity.this, PhotoDetailsActivity.class);
        intent.putExtra("photo", photo);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this, view, ViewCompat.getTransitionName(view));
        startActivity(intent, optionsCompat.toBundle());
    }

    private void toggleKeyboard(Context context){
        InputMethodManager inputMgr = (InputMethodManager)context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.RESULT_HIDDEN);
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class WrapStaggeredLayout extends StaggeredGridLayoutManager{


        public WrapStaggeredLayout(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "IndexOutOfBoundsException in RecyclerView");
            }
        }
    }
}

