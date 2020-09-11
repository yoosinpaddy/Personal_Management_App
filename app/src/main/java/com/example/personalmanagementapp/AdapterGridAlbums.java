package com.example.personalmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AdapterGridAlbums extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Photo> photos = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Photo obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterGridAlbums(Context context, ArrayList<Photo> photos) {
        this.photos = photos;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView counter;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            counter = (TextView) v.findViewById(R.id.counter);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Photo obj = photos.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.name.setText(obj.getPath());
//            if (obj.counter == null) {
//                view.counter.setVisibility(View.GONE);
//            } else {
//                view.counter.setText(obj.counter.toString());
//                view.counter.setVisibility(View.VISIBLE);
//            }

            try {
                Glide.with(ctx).load(obj.getPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view.image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, photos.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void updateData(ArrayList<Photo> imageList) {
        this.photos.addAll(imageList);
        notifyDataSetChanged();
    }

}