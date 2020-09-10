package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TaskDetailedActvity extends AppCompatActivity {

    private static final String TAG ="Task Detailed" ;
    Task task;
    int task_id = -1;
    TextView name, location;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detailed_actvity);
        dbHelper = new SQLiteHelper(this);

        name = findViewById(R.id.tv_task_name);
        location = findViewById(R.id.tv_location);

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            task_id = extras.getInt("task_id");
            Log.e(TAG, "onCreate: taskid1 "+ task_id );
            task = dbHelper.getTaskById(task_id);
            Log.e(TAG, "onCreate: taskname "+ task.getName() );
            name.setText(task.getName());
            location.setText(task.getLocation());
        }
    }
}