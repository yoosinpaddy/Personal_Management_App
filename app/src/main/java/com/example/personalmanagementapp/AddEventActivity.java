package com.example.personalmanagementapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private TextInputEditText edtEventName, edtEventLocation, edtEventDate, edtEventTime;
    private String strEventName, strEventLocation, strEventDate, strEventTime;
    private Button btnSaveEvent;
    private SQLiteHelper dbHelper;
    private boolean isUpdateIntent;
    private int eventID = -1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        dbHelper = new SQLiteHelper(this);

        edtEventName = findViewById(R.id.edtEventName);
        edtEventLocation = findViewById(R.id.edtEventLocation);
        edtEventDate = findViewById(R.id.edtEventDate);
        edtEventTime = findViewById(R.id.edtEventTime);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);

        initToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isUpdateIntent = true;
            getSupportActionBar().setTitle("Update Event");
            Event toEdit = (Event) extras.getSerializable("event");
            eventID = toEdit.getId();
            edtEventName.setText(toEdit.getName());
            edtEventLocation.setText(toEdit.getLocation());
            edtEventDate.setText(toEdit.getDate());
            edtEventTime.setText(toEdit.getTime());
        }

        edtEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                view.setMinDate(System.currentTimeMillis() + 86400000);
                                edtEventDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                edtEventTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAndSave();
            }
        });
    }

    private void ValidateAndSave() {
        strEventName = edtEventName.getText().toString().trim();
        strEventLocation = edtEventLocation.getText().toString().trim();
        strEventDate = edtEventDate.getText().toString().trim();
        strEventTime = edtEventTime.getText().toString().trim();

        if (strEventName.isEmpty()) {
            edtEventName.requestFocus();
            edtEventName.setError("Name is required");
        } else if (strEventLocation.isEmpty()) {
            edtEventLocation.requestFocus();
            edtEventLocation.setError("Location is required");
        } else if (strEventDate.isEmpty()) {
            edtEventDate.setError("Date is required");
        } else if (strEventTime.isEmpty()) {
            edtEventTime.setError("Time is required");
        } else {

            if (isUpdateIntent) {
                dbHelper.updateEvent(eventID, strEventName, strEventDate, strEventTime, strEventLocation);
                Toast.makeText(AddEventActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addEvent(strEventName, strEventDate, strEventTime, strEventLocation);
                Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
            }

            edtEventName.setText("");
            edtEventLocation.setText("");
            edtEventDate.setText("");
            edtEventTime.setText("");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        if (isUpdateIntent) {
            getSupportActionBar().setTitle("Update Friend");
        } else {
            getSupportActionBar().setTitle("Add New Friend");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        }
        return super.onOptionsItemSelected(item);
    }
}