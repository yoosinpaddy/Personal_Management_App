package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailedActvity extends AppCompatActivity {

    private static final String TAG = "Task Detailed";
    Task task;
    int task_id = -1;
    private TextView name, location;
    private CheckBox checkTask;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detailed_actvity);
        dbHelper = new SQLiteHelper(this);
        initToolbar();
        name = findViewById(R.id.tv_task_name);
        location = findViewById(R.id.tv_location);
        checkTask = findViewById(R.id.checkTask);

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            task_id = extras.getInt("task_id");
            Log.e(TAG, "onCreate: taskid1 " + task_id);
            task = dbHelper.getTaskById(task_id);
            Log.e(TAG, "onCreate: taskname " + task.getName());
            name.setText(task.getName());
            location.setText(task.getLocation());
            if (task.getStatus().equals("complete")) {
                checkTask.setChecked(true);
                Log.e(TAG, "status:  "+ task.getStatus() );
            }

        }

        checkTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                   dbHelper.toggleStatusTask(task_id,"complete");
                    Log.e(TAG, "onClick: id "+ task_id );
                    Toast.makeText(TaskDetailedActvity.this,"Checked",Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.toggleStatusTask(task_id,"incomplete");
                    Toast.makeText(TaskDetailedActvity.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo Detail");
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
            Intent intent = new Intent(TaskDetailedActvity.this, AddToDoActivity.class);
            intent.putExtra("task", task);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.action_delete_friend) {
            if (dbHelper.deleteTask(task_id) > -1) {
                Toast.makeText(this, "Task removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task already removed", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}