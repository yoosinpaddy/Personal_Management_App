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
import java.util.Calendar;
import java.util.List;

public class AllEventsActivity extends AppCompatActivity {

    private View parent_view;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private SQLiteHelper dbHelper;
    private RecyclerView recyclerView, pastRecyclerView;
    private EventAdapter mAdapter, pastAdapter;
    private FloatingActionButton fabAddEvent;
    private TextView tvNoCurrentEvents, tvNoPastEvents;
    private ArrayList<Event> currentEventsList = new ArrayList<>();
    private ArrayList<Event> pastEventArrayList = new ArrayList<>();
    private ArrayList<Event> allEventsList = new ArrayList<>();
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.event_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        pastRecyclerView = (RecyclerView) findViewById(R.id.past_events_recyclerView);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastRecyclerView.setHasFixedSize(true);

        tvNoCurrentEvents = findViewById(R.id.tvNoCurrentEvents);
        tvNoPastEvents = findViewById(R.id.tvNoPastEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        allEventsList.clear();
        currentEventsList.clear();
        pastEventArrayList.clear();
        allEventsList = dbHelper.getAllEvents();

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = mDay + "/" + (mMonth + 1) + "/" + mYear;
        Log.e(TAG, "initComponent: currentDate " + currentDate);

        for (Event t : allEventsList) {
            String[] dateParts = t.getDate().split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            Log.e(TAG, "initComponent: event[" + t.getId() + "] date: " + t.getDate());
            Log.e(TAG, "comparing: " + t.getDate() + " with: " + currentDate);

            if (year >= mYear) {
                Log.e(TAG, "initComponent: Event[" + t.getId() + "] year is greater or equal");
                if (month >= mMonth) {
                    Log.e(TAG, "initComponent: Event[" + t.getId() + "] month is greater or equal");
                    if (day >= mDay) {
                        Log.e(TAG, "initComponent: Event[" + t.getId() + "] day is greater or equal");
                        Log.e(TAG, "initComponent: Event[" + t.getId() + "] is current. Add to current list");
                        currentEventsList.add(t);
                    } else {
                        Log.e(TAG, "initComponent: Event[" + t.getId() + "] day is less");
                        Log.e(TAG, "initComponent: Event[" + t.getId() + "] is past. Add to past list");
                        pastEventArrayList.add(t);
                    }
                } else {
                    Log.e(TAG, "initComponent: Event[" + t.getId() + "] month is less");
                    Log.e(TAG, "initComponent: Event[" + t.getId() + "] is past. Add to past list");
                    pastEventArrayList.add(t);
                }
            } else {
                Log.e(TAG, "initComponent: Event[" + t.getId() + "] year is less");
                Log.e(TAG, "initComponent: Event[" + t.getId() + "] is past. Add to past list");
                pastEventArrayList.add(t);
            }

        }

        //set data and list adapter
        mAdapter = new EventAdapter(this, currentEventsList);
        pastAdapter = new EventAdapter(this, pastEventArrayList);
        recyclerView.setAdapter(mAdapter);
        pastRecyclerView.setAdapter(pastAdapter);
        actionModeCallback = new ActionModeCallback();

        checkSizes();

        // on item list clicked
        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Event event, int pos) {
                Intent intent = new Intent(AllEventsActivity.this, EventDetailActivity.class);
                intent.putExtra("event_id", event.getId());
                startActivity(intent);
            }

            @Override
            public void onCheckBoxCheckedListener(boolean isChecked, Event event, int pos) {
                if (isChecked) {
                    eventsToDelete.add(event);
                    enableActionMode(pos, true);
                    Log.e(TAG, "onCheckBoxCheckedListener: checked event: " + pos);
                } else {
                    eventsToDelete.remove(event);
                    enableActionMode(pos, true);
                    Log.e(TAG, "onCheckBoxCheckedListener: unchecked event: " + pos);
                }
            }

        });

        // on item list clicked
        pastAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Event event, int pos) {
                Intent intent = new Intent(AllEventsActivity.this, EventDetailActivity.class);
                intent.putExtra("event_id", event.getId());
                startActivity(intent);
            }

            @Override
            public void onCheckBoxCheckedListener(boolean isChecked, Event event, int pos) {
                if (isChecked) {
                    eventsToDelete.add(event);
                    enableActionMode(pos, false);
                    Log.e(TAG, "onCheckBoxCheckedListener: checked event: " + pos);
                } else {
                    eventsToDelete.remove(event);
                    enableActionMode(pos, false);
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

    private void checkSizes() {
        tvNoCurrentEvents.setVisibility(View.GONE);
        tvNoPastEvents.setVisibility(View.GONE);

        if (currentEventsList.size() == 0) {
            tvNoCurrentEvents.setVisibility(View.VISIBLE);
        }
        if (pastEventArrayList.size() == 0) {
            tvNoPastEvents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void enableActionMode(int position, boolean isCurrent) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position, isCurrent);
    }

    private void toggleSelection(int position, boolean isCurrent) {
        int count = -1;
        if (isCurrent) {
            mAdapter.toggleSelection(position);
            count = mAdapter.getSelectedItemCount();
        } else {
            pastAdapter.toggleSelection(position);
            count = pastAdapter.getSelectedItemCount();
        }

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(count + " selected");
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
            List<Integer> selectedItemPositions2 = pastAdapter.getSelectedItems();
            try {
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    mAdapter.removeData(selectedItemPositions.get(i));
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                for (int i = selectedItemPositions2.size() - 1; i >= 0; i--) {
                    pastAdapter.removeData(selectedItemPositions2.get(i));
                    pastAdapter.notifyDataSetChanged();
                }
            }

            checkSizes();

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
            pastAdapter.clearSelections();
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