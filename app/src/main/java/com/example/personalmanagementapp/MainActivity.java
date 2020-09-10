package com.example.personalmanagementapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper dbHelper;
    private ArrayList<Friend> friendArrayList = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLiteHelper(this);

        dbHelper.addFriend("Jose", "Noblepal", "male", "32", "12");

        getAllFriends();


    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFriends() {
        friendArrayList.addAll(dbHelper.getAllFriends());
        Log.e(TAG, "onPostExecute: Friends -> " + friendArrayList.toString());
        /*new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


            }
        };*/
    }
}