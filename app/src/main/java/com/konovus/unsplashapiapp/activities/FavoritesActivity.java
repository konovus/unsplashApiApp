package com.konovus.unsplashapiapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.adapters.PhotoAdapter;
import com.konovus.unsplashapiapp.databinding.ActivityFavoritesBinding;
import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.viewmodels.FavListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.konovus.unsplashapiapp.activities.PhotoDetailsActivity.isFavListUpdated;

public class FavoritesActivity extends AppCompatActivity implements PhotoAdapter.PhotoListener {

    private ActivityFavoritesBinding binding;
    private FavListViewModel viewModel;
    private List<Photo> favlist;
    private PhotoAdapter adapter;
    private int currentPage = 1;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        doInitialization();

    }

    private void doInitialization(){
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(FavListViewModel.class);
        binding.backImg.setOnClickListener(v -> onBackPressed());
        favlist = new ArrayList<>();
        loadFavList();
    }

    private void loadFavList(){
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getFavList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    binding.setIsLoading(false);
                    if(!favlist.isEmpty())
                        favlist.clear();
                    Collections.reverse(photos);
                    favlist.addAll(photos);
                    adapter = new PhotoAdapter(favlist, this, this);
                    binding.recyclerView.setItemViewCacheSize(1000);
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFavListUpdated && position >= 0){
            favlist.remove(position);
            adapter.setPhotos(favlist, position);
            isFavListUpdated = false;
        }
    }

    @Override
    public void onPhotoClicked(Photo photo, View view, int position) {
        this.position = position;
        Intent intent = new Intent(this, PhotoDetailsActivity.class);
        intent.putExtra("photo", photo);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivity(intent, optionsCompat.toBundle());
    }
}
