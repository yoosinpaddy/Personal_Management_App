package com.example.personalmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends AppCompatActivity {

    private View parent_view;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private SQLiteHelper dbHelper;
    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    private FloatingActionButton fabAddEvent;
    private TextView tvNoEvents;
    private ArrayList<Event> eventArrayList = new ArrayList<>();
    private ArrayList<Event> eventsToDelete = new ArrayList<>();
    private static final String TAG = "AllEventsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        dbHelper = new SQLiteHelper(this);

        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.event_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        eventArrayList.addAll(dbHelper.getAllEvents());

        if (eventArrayList.size() == 0) {
            tvNoEvents.setVisibility(View.VISIBLE);
        } else {
            tvNoEvents.setVisibility(View.GONE);
        }


        //set data and list adapter
        mAdapter = new EventAdapter(this, eventArrayList);
        recyclerView.setAdapter(mAdapter);
        actionModeCallback = new ActionModeCallback();

        // on item list clicked
        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Event obj, int pos) {
                Event friend = mAdapter.getItem(pos);
                Intent intent = new Intent(AllEventsActivity.this, EventDetailActivity.class);
                intent.putExtra("event_id", friend.getId());
                startActivity(intent);
            }

            @Override
            public void onCheckBoxCheckedListener(boolean isChecked, Event event, int pos) {
                if (isChecked) {
                    eventsToDelete.add(event);
                    enableActionMode(pos);
                    Log.e(TAG, "onCheckBoxCheckedListener: checked event: " + pos);
                } else {
                    eventsToDelete.remove(event);
                    enableActionMode(pos);
                    Log.e(TAG, "onCheckBoxCheckedListener: unchecked event: " + pos);
                }
            }

        });



        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllEventsActivity.this, AddEventActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        eventArrayList.clear();
        eventArrayList.addAll(dbHelper.getAllEvents());

        if (eventArrayList.size() == 0) {
            tvNoEvents.setVisibility(View.VISIBLE);
        } else {
            tvNoEvents.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        eventArrayList.clear();
        eventArrayList.addAll(dbHelper.getAllEvents());

        if (eventArrayList.size() == 0) {
            tvNoEvents.setVisibility(View.VISIBLE);
        } else {
            tvNoEvents.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteEvents();
                mode.finish();
                return true;
            }
            return false;
        }

        private void deleteEvents() {
            List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
            for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                mAdapter.removeData(selectedItemPositions.get(i));
            }
            mAdapter.notifyDataSetChanged();
            if (eventArrayList.size() == 0) {
                tvNoEvents.setVisibility(View.VISIBLE);
            } else {
                tvNoEvents.setVisibility(View.GONE);
            }
            Log.e(TAG, "deleteEvents: Attempting to delete " + eventsToDelete.size() + " items");
            for (Event f : eventsToDelete) {
                if (dbHelper.deleteEvent(f.getId()) > 0) {
                    Log.e(TAG, "deleteEvents: Deleted: " + f);
                } else {
                    Log.e(TAG, "deleteEvents: Failed to delete: " + f);
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}