package com.example.personalmanagementapp;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.OriginalViewHolder> {

    private List<Event> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, Event obj, int pos);

        void onCheckBoxCheckedListener(boolean isChecked, Event event, int pos);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public EventAdapter(Context context, List<Event> items) {
        this.items = items;
        this.context = context;
        selected_items = new SparseBooleanArray();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, time, location;
        public View lyt_parent;
        private CheckBox checkEvent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.event_name);
            date = (TextView) v.findViewById(R.id.event_date);
            time = (TextView) v.findViewById(R.id.event_time);
            location = (TextView) v.findViewById(R.id.event_location);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            checkEvent = (CheckBox) v.findViewById(R.id.checkEvent);
        }
    }

    @Override
    public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new OriginalViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(OriginalViewHolder holder, final int position) {
        final Event event = items.get(position);
        holder.name.setText(event.getName());
        holder.date.setText(event.getDate());
        holder.time.setText(event.getTime());
        holder.location.setText(event.getLocation());
        holder.checkEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mOnItemClickListener.onCheckBoxCheckedListener(b, event, position);
            }
        });

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });

        toggleCheckedIcon(holder, position);

    }

    private void toggleCheckedIcon(OriginalViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.checkEvent.setChecked(true);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.checkEvent.setChecked(false);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public Event getItem(int position) {
        return items.get(position);
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        //notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        items.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void insertItem(int index, Event Event) {
        items.add(index, Event);
        notifyItemInserted(index);
    }

}