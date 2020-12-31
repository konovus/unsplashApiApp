package com.konovus.unsplashapiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.databinding.PhotoItemBinding;
import com.konovus.unsplashapiapp.models.Photo;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private List<Photo> photos;
    private LayoutInflater layoutInflater;
    private PhotoListener photoListener;
    Context context;

    public PhotoAdapter(List<Photo> photos, PhotoListener photoListener, Context context) {
        this.photos = photos;
        this.photoListener = photoListener;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        PhotoItemBinding photoItemBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.photo_item, parent, false
        );
        return new PhotoViewHolder(photoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bindPhoto(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        private PhotoItemBinding photoItemBinding;

        public PhotoViewHolder(PhotoItemBinding photoItemBinding) {
            super(photoItemBinding.getRoot());
            this.photoItemBinding = photoItemBinding;
        }

        public void bindPhoto(Photo photo){
            photoItemBinding.setPhoto(photo);
            photoItemBinding.executePendingBindings();
            photoItemBinding.photo.setClipToOutline(true);
            photoItemBinding.photo.setOnClickListener(v -> photoListener.onPhotoClicked(photo, v, getAdapterPosition()));
        }
    }

    public interface PhotoListener{
        void onPhotoClicked(Photo photo, View view, int position);
    }

    public void setPhotos(List<Photo> photos, Integer pos){
        int currentSize = this.photos.size();
        this.photos.clear();
        this.photos.addAll(photos);
        if(pos != null){
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, getItemCount());
        } else {
            //tell the recycler view that all the old items are gone
            notifyItemRangeRemoved(0, currentSize);
            //tell the recycler view how many new items we added
            notifyItemRangeInserted(0, photos.size());
        }
    }

}
