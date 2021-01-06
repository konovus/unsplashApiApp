package com.konovus.unsplashapiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import com.bumptech.glide.Glide;
import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.models.Photo;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class Pager extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Photo> photos;
    private int currentPos;

    public Pager(Context context, ArrayList<Photo> feedItemList, int currentPos) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photos = feedItemList;
        this.currentPos = currentPos;
    }

    @Override
    public int getCount() {
        return (photos != null ? photos.size() : 0);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        position = currentPos ;

        Photo photo = photos.get(position);
        PhotoView photoView = (PhotoView) itemView.findViewById(R.id.image);
        Glide
                .with(this.mContext)
                .load(photo.getUrls().getFull())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(Glide.with(this.mContext)
                            .load(photo.getUrls().getRegular())
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(photoView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
