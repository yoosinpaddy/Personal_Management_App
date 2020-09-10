package com.example.personalmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

private final int VIEW_ITEM=1;
private final int VIEW_SECTION=0;

private List<Task> items=new ArrayList<>();
private Context ctx;
private OnItemClickListener mOnItemClickListener;

public interface OnItemClickListener {
    void onItemClick(View view, Task obj, int position);
}

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public TodoAdapter(Context context, List<Task> items) {
        this.items = items;
        ctx = context;
    }

public class OriginalViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView name;
    public View lyt_parent;

    public OriginalViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.name);
        lyt_parent = (View) v.findViewById(R.id.lyt_parent);
    }
}

public static class SectionViewHolder extends RecyclerView.ViewHolder {
    public TextView title_section;

    public SectionViewHolder(View v) {
        super(v);
        title_section = (TextView) v.findViewById(R.id.title_section);
    }

}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Task p = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.name.setText(p.getName());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            view.title_section.setText(p.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).section ? VIEW_SECTION : VIEW_ITEM;
    }

    public void insertItem(int index, Task task) {
        items.add(index, task);
        notifyItemInserted(index);
    }

    public Task getItem(int position) {
        return items.get(position);
    }

}