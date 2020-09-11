package com.example.personalmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EventDetailActivity extends AppCompatActivity {

    int event_id = -1;
    Event event;
    TextView name, date, time, location;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        dbHelper = new SQLiteHelper(this);

        name = findViewById(R.id.tv_event_name);
        date = findViewById(R.id.tv_date);
        time = findViewById(R.id.tv_time);
        location = findViewById(R.id.tv_location);

        initToolbar();

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //friend = (Friend) extras.getSerializable("friend");
            event_id = extras.getInt("event_id");

            event = dbHelper.getEventById(event_id);

            name.setText(event.getName());
            date.setText(event.getDate());
            time.setText(event.getTime());
            location.setText(event.getLocation());

        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(EventDetailActivity.this, AddEventActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_delete) {
            if (dbHelper.deleteEvent(event_id) > -1) {
                Toast.makeText(this, "Event removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Event already removed", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (event_id != -1) {
            event = dbHelper.getEventById(event_id);

            name.setText(event.getName());
            date.setText(event.getDate());
            time.setText(event.getTime());
            location.setText(event.getLocation());
        }
    }
}