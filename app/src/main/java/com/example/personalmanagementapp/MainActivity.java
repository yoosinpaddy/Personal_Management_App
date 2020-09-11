package com.example.personalmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper dbHelper;
    private static final String TAG = "MainActivity";

    private LinearLayout itemGallery, itemTodo, itemEvents, itemFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLiteHelper(this);

        itemFriends = findViewById(R.id.itemFriends);
        itemTodo = findViewById(R.id.itemTodo);
        itemEvents = findViewById(R.id.itemEvents);
        itemGallery = findViewById(R.id.itemGallery);

        itemFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllFriendsActivity.class));
            }
        });
        itemTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllToDOActivity.class));
            }
        });
        itemEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AllEventsActivity.class));
            }
        });
        itemGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

    }

}