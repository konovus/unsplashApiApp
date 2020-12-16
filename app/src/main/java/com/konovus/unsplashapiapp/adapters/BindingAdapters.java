package com.konovus.unsplashapiapp.adapters;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.konovus.unsplashapiapp.R;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String URL){
        imageView.setAlpha(0f);
        if(URL != null && !URL.isEmpty())
            Glide.with(imageView.getContext()).load(URL).addListener(
                    new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageView.animate().setDuration(300).alpha(1f).start();
                            return false;
                        }
                    }
            ).placeholder(R.color.colorIcons).into(imageView);
    }
}
