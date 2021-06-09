package com.ekanek.ekanekwallpaper.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.ekanek.ekanekwallpaper.R;
import com.ekanek.ekanekwallpaper.data.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EachRowPhotosAdapter extends RecyclerView.Adapter<EachRowPhotosAdapter.PhotoViewHolder> {
    private List<Photo> mPhotoList;

    public EachRowPhotosAdapter() {
        super();
        setHasStableIds(true);
    }

    public void setPhotoList(List<Photo> photoList) {
        mPhotoList = photoList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(mPhotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotoList != null ? mPhotoList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mPhotoList.get(position).getId());
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPhotoImage;
        private Context mContext;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoImage = itemView.findViewById(R.id.image_photo);
            mContext = itemView.getContext();
        }

        public void bind(Photo photo) {
             Picasso.with(mContext)
                    .load(photo.getUrl())
                    .into(mPhotoImage);


        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

