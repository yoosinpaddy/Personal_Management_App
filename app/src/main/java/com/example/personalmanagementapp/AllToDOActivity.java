package com.example.personalmanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AllToDOActivity extends AppCompatActivity {

    private static final String TAG = "AllTaskActivity" ;
    private View parent_view;

    private RecyclerView incompleterecyclerView;
    private RecyclerView completerecyclerView;
    private TodoAdapter inCompletemAdapter;
    private TodoAdapter CompletemAdapter;
    private FloatingActionButton fabAddTask;
    private TextView tvNoIncompleteTasks;
    private TextView tvNoCompleteTasks;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private ArrayList<Task> incompleteTaskArrayList = new ArrayList<>();
    private ArrayList<Task> CompleteTaskArrayList = new ArrayList<>();

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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Todo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        incompleterecyclerView = (RecyclerView) findViewById(R.id.incomlete_todo_recyclerView);
        incompleterecyclerView.setLayoutManager(new LinearLayoutManager(this));
        incompleterecyclerView.setHasFixedSize(true);
        tvNoIncompleteTasks = findViewById(R.id.tvNoIncompleteTasks);

        completerecyclerView = (RecyclerView) findViewById(R.id.complete_todo_recyclerView);
        completerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        completerecyclerView.setHasFixedSize(true);
        tvNoCompleteTasks = findViewById(R.id.tvNoCompleteTasks);

        fabAddTask = findViewById(R.id.fabAddTask);

        taskArrayList.addAll(dbHelper.getAllTodos());

        if (taskArrayList.size() == 0) {
            tvNoIncompleteTasks.setVisibility(View.VISIBLE);
            tvNoCompleteTasks.setVisibility(View.VISIBLE);
        } else {
            for (Task t : taskArrayList) {

                if (t.getStatus().equals("complete")) {
                    CompleteTaskArrayList.add(t);
                } else {
                    incompleteTaskArrayList.add(t);
                }
            }

            if (CompleteTaskArrayList.size() == 0){
                tvNoCompleteTasks.setVisibility(View.VISIBLE);
            }else {
                tvNoCompleteTasks.setVisibility(View.GONE);
            }
            if (incompleteTaskArrayList.size() == 0){
                tvNoIncompleteTasks.setVisibility(View.VISIBLE);
            }else {
                tvNoIncompleteTasks.setVisibility(View.GONE);
            }

        }
        

        //set data and list adapter
        inCompletemAdapter = new TodoAdapter(this, incompleteTaskArrayList);
        incompleterecyclerView.setAdapter(inCompletemAdapter);

        CompletemAdapter = new TodoAdapter(this, CompleteTaskArrayList);
        completerecyclerView.setAdapter(CompletemAdapter);

        // on item list clicked
        inCompletemAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Task obj, int position) {
                Task task = inCompletemAdapter.getItem(position);
                Log.e(TAG, "onItemClick: taskId "+ task.getId());
                Intent intent = new Intent(AllToDOActivity.this, TaskDetailedActvity.class);
                intent.putExtra("task_id", task.getId());
                startActivity(intent);
            }
        });

        CompletemAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Task obj, int position) {
                Task task = CompletemAdapter.getItem(position);
                Intent intent = new Intent(AllToDOActivity.this, TaskDetailedActvity.class);
                intent.putExtra("task_id", task.getId());
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        CompleteTaskArrayList.clear();
        incompleteTaskArrayList.clear();
        taskArrayList.clear();
        initComponent();
    }
}