package com.example.personalmanagementapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class AddFriendActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteHelper dbHelper;
    private TextInputEditText edtFname, edtLname, edtAge, edtAddress;
    private String fname, lname, age, address, gender;
    private RadioGroup rgGender;
    private RadioButton radioGenderButton;
    private Button btnSaveFriend;
    private boolean isUpdateIntent;
    private int friendID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        dbHelper = new SQLiteHelper(this);

        edtFname = findViewById(R.id.edtFname);
        edtLname = findViewById(R.id.edtLname);
        edtAge = findViewById(R.id.edtAge);
        edtAddress = findViewById(R.id.edtAddress);
        rgGender = findViewById(R.id.rgGender);
        btnSaveFriend = findViewById(R.id.btnSaveFriend);

        initToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isUpdateIntent = true;
            getSupportActionBar().setTitle("Update Friend");
            Friend toEdit = (Friend) extras.getSerializable("friend");
            friendID = toEdit.getId();
            edtFname.setText(toEdit.getFname());
            edtLname.setText(toEdit.getLname());
            edtAge.setText(toEdit.getAge());
            edtAddress.setText(toEdit.getAddress());

            if (toEdit.getGender().contentEquals("Male")) {
                radioGenderButton = findViewById(R.id.radioMale);
            } else {
                radioGenderButton = findViewById(R.id.radioFemale);
            }
            radioGenderButton.setChecked(true);
        }

        btnSaveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = edtFname.getText().toString().trim();
                lname = edtLname.getText().toString().trim();
                age = edtAge.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                int selectedGender = rgGender.getCheckedRadioButtonId();
                radioGenderButton = findViewById(selectedGender);
                gender = (String) radioGenderButton.getText();

                saveFriendData();

            }
        });

    }

    private void saveFriendData() {
        if (TextUtils.isEmpty(fname)) {
            edtFname.setError("First name is required");
        } else if (TextUtils.isEmpty(lname)) {
            edtLname.setError("Last name is required");
        } else if (TextUtils.isEmpty(age)) {
            edtAge.setError("Age is required");
        } else if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Address is required");
        } else if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else {
            if (isUpdateIntent) {
                dbHelper.updateFriend(friendID, fname, lname, gender, age, address);
                Toast.makeText(this, "Friend updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                dbHelper.addFriend(fname, lname, gender, age, address);
                Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show();
            }

            clearTexts();
        }
    }

    private void clearTexts() {
        edtFname.setText("");
        edtLname.setText("");
        edtAge.setText("");
        edtAddress.setText("");
        rgGender.clearCheck();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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