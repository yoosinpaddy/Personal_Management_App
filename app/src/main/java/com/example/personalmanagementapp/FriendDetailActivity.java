package com.example.personalmanagementapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FriendDetailActivity extends AppCompatActivity {

    Friend friend;
    TextView name, age, gender, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        initToolbar();

        name = findViewById(R.id.tv_friend_name);
        age = findViewById(R.id.tv_age);
        gender = findViewById(R.id.tv_gender);
        address = findViewById(R.id.tv_address);

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            friend = (Friend) extras.getSerializable("friend");

            name.setText(friend.getFname() + " " + friend.getLname());
            age.setText(friend.getAge());
            gender.setText(friend.getGender());
            address.setText(friend.getAddress());
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
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
            //TODO Edit
        } else if (item.getItemId() == R.id.action_delete_friend) {
            //TODO Delete
        }
        return super.onOptionsItemSelected(item);
    }
}