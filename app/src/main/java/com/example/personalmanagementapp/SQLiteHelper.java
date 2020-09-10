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

    /*Db operations for to-do module*/


    /*Add new todos*/
    public boolean addTask(String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("location", location);
        contentValues.put("status", "incomplete");
        db.insert("todos", null, contentValues);
        return true;
    }

    /*Update todos*/
    public boolean updateTask(int id, String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("location", location);
        db.update("todos", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    /*Delete todos*/
    public Integer deleteTask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("todos",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Get all todos*/
    public ArrayList<Task> getAllTodos() {
        ArrayList<Task> array_list = new ArrayList();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from todos", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Task t = new Task();
            t.setId(res.getInt(res.getColumnIndex("id")));
            t.setName(res.getString(res.getColumnIndex("name")));
            t.setLocation(res.getString(res.getColumnIndex("location")));
            t.setStatus(res.getString(res.getColumnIndex("status")));
            array_list.add(t);
            res.moveToNext();
        }
        return array_list;
    }



    /*Db operations for event module*/


    /*Add new event*/
    public boolean addEvent(String name, String date, String time,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("location", location);
        db.insert("events", null, contentValues);
        return true;
    }

    /*Update events*/
    public boolean updateEvent(int id,String name, String date, String time,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("location", location);
        db.update("events", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    /*Delete events*/
    public Integer deleteEvent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Get all events*/
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> array_list = new ArrayList();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from events", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Event t = new Event();
            t.setId(res.getInt(res.getColumnIndex("id")));
            t.setName(res.getString(res.getColumnIndex("name")));
            t.setDate(res.getString(res.getColumnIndex("date")));
            t.setTime(res.getString(res.getColumnIndex("time")));
            t.setLocation(res.getString(res.getColumnIndex("location")));
            array_list.add(t);
            res.moveToNext();
        }
        return array_list;
    }





    /*Db operations for gallery photos*/


    /*Add new photo*/
    public boolean addPhoto(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.insert("photos", null, contentValues);
        return true;
    }



    /*Delete photo*/
    public Integer deletephoto(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("photos",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Get all photos*/
    public ArrayList<Photo> getAllPhotos() {
        ArrayList<Photo> array_list = new ArrayList();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from photos", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Photo t = new Photo();
            t.setId(res.getInt(res.getColumnIndex("id")));
            t.setName(res.getString(res.getColumnIndex("name")));
            array_list.add(t);
            res.moveToNext();
        }
        return array_list;
    }



    /*Db operations for assigned photos for friends*/


    /*assign new friend photo*/
    public boolean addFriendPhoto(Integer friend_id,Integer photo_id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("friend_id", friend_id);
        contentValues.put("photo_id", photo_id);
        db.insert("friends_photos", null, contentValues);
        return true;
    }



    /*Delete assigned friend photo*/
    public Integer deleteFriendPhoto(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("friends_photos",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Get all photos for a particular friend*/
    public ArrayList<FriendPhoto> getAllFriendPhotos(int friend_id) {
        ArrayList<FriendPhoto> array_list = new ArrayList();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from photos where friend_id = ?", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            FriendPhoto t = new FriendPhoto();
            t.setId(res.getInt(res.getColumnIndex("id")));
            t.setFriend_id(res.getInt(res.getColumnIndex("friend_id")));
            t.setFriend_id(res.getInt(res.getColumnIndex("photo_id")));
            array_list.add(t);
            res.moveToNext();
        }
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



    public Boolean toggleStatusTask(int task_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("todos", contentValues, "id = ? ", new String[]{Integer.toString(task_id)});
        return true;
    }

    public Task getTaskById(int task_id) {
        Task t = new Task();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from todos where id = " + task_id + " limit 1", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            t.setId(res.getInt(res.getColumnIndex("id")));
            t.setName(res.getString(res.getColumnIndex("name")));
            t.setLocation(res.getString(res.getColumnIndex("location")));
            t.setStatus(res.getString(res.getColumnIndex("status")));
            res.moveToNext();
        }
        res.close();
        return t;
    }

    public Event getEventById(int event_id) {
        //TODO
        return null;
    }
}
