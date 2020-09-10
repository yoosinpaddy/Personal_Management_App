package com.example.personalmanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "personalmanagement.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /*Create tables*/
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists friends " +
                "(id integer primary key AUTOINCREMENT , fname text, lname text, gender text, age text, address text)"
        );
        db.execSQL("create table if not exists todos " +
                "(id integer primary key AUTOINCREMENT , name text, location text, status text)"
        );
        db.execSQL("create table if not exists events " +
                "(id integer primary key AUTOINCREMENT , name text, date text, time text, location text)"
        );
        db.execSQL("create table if not exists photos " +
                "(id integer primary key AUTOINCREMENT , name text)"
        );
        db.execSQL("create table if not exists friends_photos " +
                "(id integer primary key AUTOINCREMENT , friend_id integer REFERENCES \"+friends+\"(\"+id+\"), photo_id integer REFERENCES \"+photos+\"(\"+id+\"))"
        );
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    /*Add new friend*/
    public boolean addFriend(String fname, String lname, String gender, String age, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fname", fname);
        contentValues.put("lname", lname);
        contentValues.put("gender", gender);
        contentValues.put("age", age);
        contentValues.put("address", address);
        db.insert("friends", null, contentValues);
        return true;
    }

    /*Update friend*/
    public boolean updateFriend(int id, String fname, String lname, String gender, String age, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fname", fname);
        contentValues.put("lname", lname);
        contentValues.put("gender", gender);
        contentValues.put("age", age);
        contentValues.put("address", address);
        db.update("friends", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    /*Delete friend*/
    public Integer deleteFriend(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("friends",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Get all friends*/
    public ArrayList<Friend> getAllFriends() {
        ArrayList<Friend> array_list = new ArrayList();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from friends", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Friend f = new Friend();
            f.setId(res.getInt(res.getColumnIndex("id")));
            f.setFname(res.getString(res.getColumnIndex("fname")));
            f.setLname(res.getString(res.getColumnIndex("lname")));
            f.setGender(res.getString(res.getColumnIndex("gender")));
            f.setAge(res.getString(res.getColumnIndex("age")));
            f.setAddress(res.getString(res.getColumnIndex("address")));
            array_list.add(f);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public Friend getFriendById(int friend_id) {
        Friend f = new Friend();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from friends where id = " + friend_id + " limit 1", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            f.setId(res.getInt(res.getColumnIndex("id")));
            f.setFname(res.getString(res.getColumnIndex("fname")));
            f.setLname(res.getString(res.getColumnIndex("lname")));
            f.setGender(res.getString(res.getColumnIndex("gender")));
            f.setAge(res.getString(res.getColumnIndex("age")));
            f.setAddress(res.getString(res.getColumnIndex("address")));
            res.moveToNext();
        }
        res.close();
        return f;
    }
}
