package com.example.personalmanagementapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private TextInputEditText edtEventName, edtEventLocation, edtEventDate, edtEventTime;
    private String strEventName, strEventLocation, strEventDate, strEventTime;
    private Button btnSaveEvent;
    private SQLiteHelper dbHelper;

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
            dbHelper.addEvent(strEventName, strEventDate, strEventTime, strEventLocation);

            Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();

            edtEventName.setText("");
            edtEventLocation.setText("");
            edtEventDate.setText("");
            edtEventTime.setText("");
        }
    }
}