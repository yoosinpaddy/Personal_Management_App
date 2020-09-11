package com.example.personalmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FriendDetailActivity extends AppCompatActivity {

    private static final String TAG ="FriendActivity" ;
    Friend friend;
    int friend_id = -1;
    TextView name, age, gender, address,noPhotos;
    private SQLiteHelper dbHelper;
    private RecyclerView recyclerViewPhotos;
    private ArrayList<Photo> photos = new ArrayList<>();
    private AdapterGridAlbums mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        initToolbar();

        dbHelper = new SQLiteHelper(this);

        initComponents();

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

    private void initComponents(){
        name = findViewById(R.id.tv_friend_name);
        age = findViewById(R.id.tv_age);
        gender = findViewById(R.id.tv_gender);
        address = findViewById(R.id.tv_address);
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);
        noPhotos = findViewById(R.id.noPhotos);

        recyclerViewPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewPhotos.setHasFixedSize(true);


        photos.addAll(dbHelper.getAllPhotosForFriend(friend_id));
        if (photos.size() == 0) {
            noPhotos.setVisibility(View.VISIBLE);
        } else {
            noPhotos.setVisibility(View.GONE);
        }
        Log.e(TAG, "initComponents: " +friend_id );
        //set data and list adapter
        mAdapter = new AdapterGridAlbums(this, photos);
        recyclerViewPhotos.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridAlbums.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Photo obj, int position) {
//                Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        noPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendDetailActivity.this,GalleryActivity.class);
                i.putExtra("mBool", true);
                startActivityForResult(i,2);
            }
        });
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
        } else if (item.getItemId() == R.id.action_delete) {
            if (dbHelper.deleteFriend(friend_id) > -1) {
                Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Friend already removed", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Integer photo_id =data.getIntExtra("photoId",0);
                dbHelper.addFriendPhoto(friend_id,photo_id,"test");
                photos.clear();
                photos.addAll(dbHelper.getAllPhotosForFriend(friend_id));
                mAdapter.updateData(photos);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}