package com.konovus.unsplashapiapp.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.konovus.unsplashapiapp.R;

import androidx.annotation.Nullable;


public class GlideImageLoader {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private static Bitmap bitmap;
    private ContentResolver cr;
    private String thumb;

    public GlideImageLoader(ImageView imageView, ProgressBar progressBar, ContentResolver cr, String thumb) {
        mImageView = imageView;
        mProgressBar = progressBar;
        this.cr = cr;
        this.thumb = thumb;
    }

    public static Bitmap getBitmap(){
        return bitmap;
    }

    public void load(final String url, RequestOptions options) {
        if (url == null || options == null) return;

        onConnecting();

        //set Listener & start
        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                if (mProgressBar != null) {
                    mProgressBar.setProgress((int) (100 * bytesRead / expectedLength));
                }
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });
        //Get Image
        Glide.with(mImageView.getContext())
                .load(url)
                .apply(options.diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(Glide.with(mImageView.getContext())
                                .load(thumb)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mImageView.setTag("isReady");
                        bitmap = ((BitmapDrawable) resource).getBitmap();
                        mImageView.setClipToOutline(true);
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        return false;
                    }
                })
                .placeholder(R.color.colorIcons)
                .into(mImageView);
    }


    private void onConnecting() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    private void onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
    }
}
