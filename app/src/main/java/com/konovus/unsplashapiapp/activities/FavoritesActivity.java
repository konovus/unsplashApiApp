package com.konovus.unsplashapiapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.databinding.ActivityFavoritesBinding;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        doInitialization();

    }

    private void doInitialization(){

    }
}
