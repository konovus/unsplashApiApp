package com.konovus.unsplashapiapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konovus.unsplashapiapp.R;
import com.konovus.unsplashapiapp.databinding.PhotoItemBinding;
import com.konovus.unsplashapiapp.models.Photo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
            photoItemBinding.photo.setOnClickListener(v -> photoListener.onPhotoClicked(photo, v));


        }
    }

    public interface PhotoListener{
        void onPhotoClicked(Photo photo, View view);
    }

    public void setPhotos(List<Photo> photos){
        this.photos = photos;
        notifyDataSetChanged();
    }



    private class MyAsyncTask extends AsyncTask<String, Integer, Long> {

        private PhotoItemBinding photoItemBinding;
        private Context context;
        private Photo photo;

        public MyAsyncTask(PhotoItemBinding photoItemBinding, Context context, Photo photo){
            this.photoItemBinding = photoItemBinding;
            this.context = context;
            this.photo = photo;
        }
        protected void onPreExecute() {
            photoItemBinding.progressText.setVisibility(View.VISIBLE);
            photoItemBinding.progressDownload.setVisibility(View.VISIBLE);
            photoItemBinding.progressDownload.setIndeterminate(false);
            photoItemBinding.progressDownload.setMax(100);

// update the UI before background processes start
        }

        protected Long doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // Detect the file lenghth
                int fileLength = connection.getContentLength();
                // Locate storage location
                String filepath = context.getExternalFilesDir("/").getAbsolutePath()
                        + File.separator;
                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());
                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath
                        + photo.getId() + ".jpg");
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                // Error Log
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog

            photoItemBinding.progressDownload.setProgress(progress[0]);
            photoItemBinding.progressText.setText(progress[0].toString());

            // receive progress updates from doInBackground
        }

        protected void onPostExecute(Long result) {
//          update the UI after background processes completes

            photoItemBinding.progressText.setText("");
            photoItemBinding.progressDownload.setProgress(0);
            photoItemBinding.progressDownload.setVisibility(View.GONE);
            photoItemBinding.progressText.setVisibility(View.GONE);
        }
    }
}
