package com.konovus.unsplashapiapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.databinding.ActivityPhotoDetailsBinding;
import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.utils.CapturePhotoUtils;
import com.konovus.unsplashapiapp.utils.GlideImageLoader;
import com.konovus.unsplashapiapp.viewmodels.PhotoDetailsViewModel;

public class PhotoDetailsActivity extends AppCompatActivity {

    private ActivityPhotoDetailsBinding binding;
    private Photo photo;
    private PhotoDetailsViewModel viewModel;
    private boolean isLiked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_details);

        doInitialization();
    }

    private void doInitialization(){
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(PhotoDetailsViewModel.class);

        setupLayout();
        checkFavInFavList();

        binding.wrapperLayout.setOnClickListener(v -> onBackPressed());

        binding.likeBtn.setOnClickListener(v -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            if(isLiked){
                compositeDisposable.add(viewModel.removeFromFavList(photo)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isLiked = false;
                            toggleFav((ImageView) v);
                            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }));
            } else {
                compositeDisposable.add(viewModel.addToFavList(photo)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            toggleFav((ImageView) v);
                            compositeDisposable.dispose();
                        }));
            }
        });

        binding.downloadBtn.setOnClickListener(v -> {
            CapturePhotoUtils.insertImage(getContentResolver(),
                    GlideImageLoader.getBitmap(), photo.getId(), photo.getId()+photo.getUser().getName());
            Toast.makeText(this, "Saved in Gallery", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupLayout(){
        if(getIntent().hasExtra("photo"))
            photo = (Photo) getIntent().getSerializableExtra("photo");
        binding.setPhoto(photo);
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH);

        new GlideImageLoader(binding.photo, binding.photoFull,
                binding.progressBar, getContentResolver()).load(photo.getUrls().getFull(),options);

        binding.likeBtn.postDelayed(() -> {
            binding.likeBtn.setVisibility(View.VISIBLE);
            binding.downloadBtn.setVisibility(View.VISIBLE);
        }, 300);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.likeBtn.setVisibility(View.INVISIBLE);
        binding.downloadBtn.setVisibility(View.INVISIBLE);
    }

    private void checkFavInFavList(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getPhotoFromFavList(photo.getId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photo -> {
                    isLiked = true;

                    binding.likeBtn.setBackgroundResource(R.drawable.icons_fav_bg_shape);
//                Changing color for the icon (vector drawable)
                    binding.likeBtn.setColorFilter(ContextCompat.getColor(binding.likeBtn.getContext(), R.color.colorIconsBG), PorterDuff.Mode.SRC_IN);
                    binding.likeBtn.setTag("Fav");

                    compositeDisposable.dispose();
                }));

    }

    private void toggleFav(ImageView view){
        if(view.getTag() == null || view.getTag().toString().isEmpty()){
            view.setBackgroundResource(R.drawable.icons_fav_bg_shape);
//                Changing color for the icon (vector drawable)
            view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorIconsBG), PorterDuff.Mode.SRC_IN);
            view.setTag("Fav");
        } else {
            view.setBackgroundResource(R.drawable.icons_bg_shape);
//                Changing color for the icon (vector drawable)
            view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorIcons), PorterDuff.Mode.SRC_IN);
            view.setTag("");

        }
    }
    private void transitionSetup(){
        Fade fade = new Fade();
        View decor =  getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(decor.findViewById(android.R.id.statusBarBackground), true);
        fade.excludeTarget(decor.findViewById(android.R.id.navigationBarBackground), true);
        fade.excludeTarget(decor.findViewById(android.R.id.background), true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }
}
