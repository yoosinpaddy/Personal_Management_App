package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddToDoActivity extends AppCompatActivity {

    private TextInputEditText editname;
    private TextInputEditText editlocation;
    private Button submit;
    private SQLiteHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        dbHelper = new SQLiteHelper(this);
        editname = findViewById(R.id.todo_name);
        editlocation = findViewById(R.id.todo_location);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAndSave();
            }
        });
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
            dbHelper.addTask(name,location,"incomplete");
            Toast.makeText(AddToDoActivity.this,"To do added successfully",Toast.LENGTH_SHORT).show();
            editname.setText("");
            editlocation.setText("");
        }
    }
}