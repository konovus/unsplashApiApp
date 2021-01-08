package com.konovus.unsplashapiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import com.bumptech.glide.Glide;
import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.activities.MainActivity;
import com.konovus.unsplashapiapp.activities.PhotoDetailsActivity;
import com.konovus.unsplashapiapp.databinding.PagerItemBinding;
import com.konovus.unsplashapiapp.models.Photo;
import com.konovus.unsplashapiapp.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class Pager extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Photo> photos;
    private int currentPos;
    private PagerItemBinding binding;


    public Pager(Context context, ArrayList<Photo> feedItemList, int currentPos) {

        this.mContext = context;
        this.photos = feedItemList;
        this.currentPos = currentPos;
//        if(mLayoutInflater == null)
//            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return (photos != null ? photos.size() : 0);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.pager_item, container, false);

//        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        if(currentPos < photos.size()){
            position = currentPos ;

            Photo photo = photos.get(position);
//        PhotoView photoView = (PhotoView) itemView.findViewById(R.id.image);
//        Glide
//            .with(this.mContext)
//            .load(photo.getUrls().getFull())
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .thumbnail(Glide.with(this.mContext)
//                        .load(photo.getUrls().getRegular())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL))
//            .into(binding.photo);

            binding.setPhoto(photo);
            binding.flipView.setFlipDuration(500);


            new GlideImageLoader(binding.photo, binding.photoFull,
                    binding.progressBar, mContext.getContentResolver()).load(photo.getUrls().getFull()
                    , new RequestOptions().priority(Priority.HIGH));


            binding.likeBtn.setAlpha(0f);
            binding.downloadBtn.setAlpha(0f);
            binding.likeBtn.animate().alpha(1f).setStartDelay(400).setDuration(300).start();
            binding.downloadBtn.animate().alpha(1f).setStartDelay(400).setDuration(300).start();

            container.addView(binding.getRoot());
            currentPos++;

        } else {

        }
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
