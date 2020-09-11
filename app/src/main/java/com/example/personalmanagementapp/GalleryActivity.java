package com.example.personalmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterGridAlbums mAdapter;
    public static final int REQUEST_CODE = 330;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 430;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<Photo> photos = new ArrayList<>();
    private static final String TAG = "GalleryActivity";
    private SQLiteHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        dbHelper =new SQLiteHelper(this);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();

        /*final Uri data = FileProvider.getUriForFile(context, "myprovider", new File(file_path));
        context.grantUriPermission(context.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final Intent intent = new Intent(Intent.ACTION_VIEW)
              .setDataAndType(data, "video/*")
              .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);*/

        /**/
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Albums");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        //TODO: Get images from external files

        photos.addAll(dbHelper.getAllPhotos());
        //set data and list adapter
        mAdapter = new AdapterGridAlbums(this, photos);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridAlbums.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Photo obj, int position) {
                Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_add_images) {
            if (checkPermissions()) {
                startImagePicker();
            } else {
                requestPerms();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CODE) {
                    Uri selectedImageUri = data.getData();
                    Log.e(TAG, "onActivityResult: selectedImageUri: " + selectedImageUri);
                    //selectedImageUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID+".provider",file1);
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                        imagePaths.add(path);
                        dbHelper.addPhoto(path);
                        photos.clear();
                        photos.addAll(dbHelper.getAllPhotos());

                    }
                    mAdapter.updateData(photos);
                    Log.e(TAG, "onActivityResult: Image path: " + imagePaths);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPerms() {
        ActivityCompat.requestPermissions(GalleryActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            startImagePicker();
        }
    }
}