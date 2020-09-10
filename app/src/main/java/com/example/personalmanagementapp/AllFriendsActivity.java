package com.example.personalmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AllFriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterListFriend mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private SQLiteHelper dbHelper;
    private ArrayList<Friend> friendArrayList = new ArrayList<>();
    private ArrayList<Friend> friendsToDelete = new ArrayList<>();
    private static final String TAG = "AllFriendsActivity";
    private TextView tvNoFriends;
    private FloatingActionButton fabAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_friends);

        dbHelper = new SQLiteHelper(this);

        initToolbar();
        initComponent();
        Toast.makeText(this, "Long press to select multiple", Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All My Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        tvNoFriends = findViewById(R.id.tvNoFriends);
        fabAddFriend = findViewById(R.id.fabAddFriend);

        friendArrayList.addAll(dbHelper.getAllFriends());

        if (friendArrayList.size() == 0) {
            tvNoFriends.setVisibility(View.VISIBLE);
        } else {
            tvNoFriends.setVisibility(View.GONE);
        }

        //set data and list adapter
        mAdapter = new AdapterListFriend(this, friendArrayList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new AdapterListFriend.OnClickListener() {
            @Override
            public void onItemClick(View view, Friend obj, int pos) {
                if (mAdapter.getSelectedItemCount() > 0) {
                    friendsToDelete.add(obj);
                    enableActionMode(pos);
                } else {
                    Friend friend = mAdapter.getItem(pos);
                    Intent intent = new Intent(AllFriendsActivity.this, FriendDetailActivity.class);
                    intent.putExtra("friend", friend);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, Friend obj, int pos) {
                friendsToDelete.add(obj);
                enableActionMode(pos);
            }
        });

        actionModeCallback = new ActionModeCallback();

        fabAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllFriendsActivity.this, AddFriendActivity.class));
            }
        });

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteFriends();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
        }
    }

    private void deleteFriends() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
        Log.e(TAG, "deleteFriends: Attempting to delete " + friendsToDelete.size() + " items");
        for (Friend f : friendsToDelete) {
            if (dbHelper.deleteContact(f.getId()) > 0) {
                Log.e(TAG, "deleteFriends: Deleted: " + f);
            } else {
                Log.e(TAG, "deleteFriends: Failed to delete: " + f);
            }
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