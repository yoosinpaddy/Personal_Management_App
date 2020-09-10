package com.example.personalmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FriendDetailActivity extends AppCompatActivity {

    Friend friend;
    int friend_id = -1;
    TextView name, age, gender, address;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        initToolbar();

        dbHelper = new SQLiteHelper(this);

        name = findViewById(R.id.tv_friend_name);
        age = findViewById(R.id.tv_age);
        gender = findViewById(R.id.tv_gender);
        address = findViewById(R.id.tv_address);

        /*Get friend data from intent*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //friend = (Friend) extras.getSerializable("friend");
            friend_id = extras.getInt("friend_id");

            friend = dbHelper.getFriendById(friend_id);

            name.setText(friend.getFname() + " " + friend.getLname());
            age.setText(friend.getAge());
            gender.setText(friend.getGender());
            address.setText(friend.getAddress());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (friend_id != -1) {
            friend = dbHelper.getFriendById(friend_id);
            name.setText(friend.getFname() + " " + friend.getLname());
            age.setText(friend.getAge());
            gender.setText(friend.getGender());
            address.setText(friend.getAddress());
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friend Details");
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
            Intent intent = new Intent(FriendDetailActivity.this, AddFriendActivity.class);
            intent.putExtra("friend", friend);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_delete_friend) {
            if (dbHelper.deleteFriend(friend_id) > -1) {
                Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Friend already removed", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}