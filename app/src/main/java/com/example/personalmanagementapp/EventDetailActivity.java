package com.example.personalmanagementapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //friend = (Friend) extras.getSerializable("friend");
            event_id = extras.getInt("event_id");

            event = dbHelper.getEventById(event_id);

        }

    }
}