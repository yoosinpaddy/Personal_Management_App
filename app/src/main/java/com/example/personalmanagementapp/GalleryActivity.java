package com.example.personalmanagementapp;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterGridAlbums mAdapter;
    public static final int REQUEST_CODE = 330;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 430;
    private ArrayList<Photo> photos = new ArrayList<>();
    private ArrayList<Photo> multiplePhotos = new ArrayList<>();
    private static final String TAG = "GalleryActivity";
    private SQLiteHelper dbHelper;
    private int selected_photo_id = -1;
    private TextView tvNoImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        dbHelper = new SQLiteHelper(this);
        parent_view = findViewById(android.R.id.content);
        tvNoImages = findViewById(R.id.tvNoImages);

        initToolbar();
        initComponent();

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Gallery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new AdapterGridAlbums(this, photos);
        recyclerView.setAdapter(mAdapter);

        refreshData();


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridAlbums.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Photo photo, int position) {
                Log.e(TAG, "onItemClick: selected id " + photo.getId());
                selected_photo_id = photo.getId();
                showPopup(view);
            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (selected_photo_id > -1) {
                    if (dbHelper.deletephoto(selected_photo_id) > -1) {
                        Log.e(TAG, "onMenuItemClick: Deleted " + selected_photo_id);
                    } else {
                        Log.e(TAG, "onMenuItemClick: Failed to delete " + selected_photo_id);
                    }
                    refreshData();
                } else {
                    Log.e(TAG, "onMenuItemClick: invalid id");
                }
                return true;
            default:
                return false;
        }
    }

    private void refreshData() {
        photos.clear();
        photos.addAll(dbHelper.getAllPhotos());
        mAdapter.notifyDataSetChanged();

        if (photos.size() == 0) {
            tvNoImages.setVisibility(View.VISIBLE);
        } else {
            tvNoImages.setVisibility(View.GONE);
        }
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
                    if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Log.e(TAG, "onActivityResult: selectedImageUri: " + selectedImageUri);

                        // Get the path from the Uri
                        final String path = getPathFromURI(selectedImageUri);

                        if (path != null) {
                            dbHelper.addPhoto(path);
                            refreshData();
                            Log.e(TAG, "onActivityResult: Image paths: " + photos);
                        }
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                final String path = getPathFromURI(uri);
                                dbHelper.addPhoto(path);
                                refreshData();
                            }
                        }
                    }

                }
            }
        } catch (
                Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }

    }

    public String getPathFromURI(Uri contentUri) {
        Cursor cur = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cur = getContentResolver().query(contentUri, proj, null, null, null);
            assert cur != null;
            int column_index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cur.moveToFirst();
            return cur.getString(column_index);
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
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