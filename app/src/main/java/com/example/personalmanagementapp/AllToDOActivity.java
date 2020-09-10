package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AllToDOActivity extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private TodoAdapter mAdapter;
    private FloatingActionButton fabAddTask;
    private TextView tvNoTasks;
    private ArrayList<Task> taskArrayList = new ArrayList<>();

    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_to_d_o);
        dbHelper =new SQLiteHelper(this);

        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Todo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.todo_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        tvNoTasks = findViewById(R.id.tvNoTasks);
        fabAddTask = findViewById(R.id.fabAddTask);

        taskArrayList.addAll(dbHelper.getAllTodos());

        if (taskArrayList.size() == 0) {
            tvNoTasks.setVisibility(View.VISIBLE);
        } else {
            tvNoTasks.setVisibility(View.GONE);
        }
        

        //set data and list adapter
        mAdapter = new TodoAdapter(this, taskArrayList);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Task obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.getName() + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllToDOActivity.this, AddToDoActivity.class));
            }
        });

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