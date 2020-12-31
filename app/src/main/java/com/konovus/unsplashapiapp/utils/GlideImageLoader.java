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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;


public class GlideImageLoader {

    private ImageView mImageView;
    private ImageView mImageViewFull;
    private ProgressBar mProgressBar;
    private static Bitmap bitmap;
    private ContentResolver cr;

    public GlideImageLoader(ImageView imageView,ImageView mImageViewFull, ProgressBar progressBar, ContentResolver cr) {
        mImageView = imageView;
        this.mImageViewFull = mImageViewFull;
        mProgressBar = progressBar;
        this.cr = cr;
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
        Glide.with(mImageViewFull.getContext())
                .load(url)
                .apply(options.diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        bitmap = ((BitmapDrawable) resource).getBitmap();
                        mImageViewFull.animate().alpha(1f).setDuration(300).setStartDelay(300).withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        mImageView.setVisibility(View.INVISIBLE);
                                        mImageViewFull.setVisibility(View.VISIBLE);
                                        mImageViewFull.setClipToOutline(true);
                                    }
                                }
                        ).start();
                        ProgressAppGlideModule.forget(url);
//                        onFinished();
                        return false;
                    }
                })
                .into(mImageViewFull);
    }


    private void onConnecting() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    private void onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.setVisibility(View.GONE);
            mImageViewFull.setVisibility(View.VISIBLE);
        }
    }
}
