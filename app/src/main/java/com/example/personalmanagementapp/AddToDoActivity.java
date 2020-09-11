package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddToDoActivity extends AppCompatActivity {

    private TextInputEditText editname;
    private TextInputEditText editlocation;
    private Button submit;
    private SQLiteHelper dbHelper;
    private Integer taskID;
    private Boolean isNew = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        initToolbar();
        dbHelper = new SQLiteHelper(this);
        editname = findViewById(R.id.todo_name);
        editlocation = findViewById(R.id.todo_location);
        submit = findViewById(R.id.submit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isNew = false;
            getSupportActionBar().setTitle("Update Task");
            Task task = (Task) extras.getSerializable("task");
            taskID = task.getId();
            editname.setText(task.getName());
            editlocation.setText(task.getLocation());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAndSave();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ValidateAndSave(){
        String name = editname.getText().toString().trim();
        String location = editlocation.getText().toString().trim();

        if (name.isEmpty()){
            editname.requestFocus();
            editname.setError("Name is required");
        }else if (location.isEmpty()){
            editlocation.requestFocus();
            editlocation.setError("Location is required");
        }else{
            if (isNew){
                dbHelper.addTask(name,location);
                Toast.makeText(AddToDoActivity.this,"To do added successfully",Toast.LENGTH_SHORT).show();
            }else {
                dbHelper.updateTask(taskID,name,location);
                Toast.makeText(AddToDoActivity.this,"To do updated successfully",Toast.LENGTH_SHORT).show();
            }

            editname.setText("");
            editlocation.setText("");
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