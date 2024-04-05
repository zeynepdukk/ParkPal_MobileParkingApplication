package com.example.parkpal1;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking.db";
    private static final int DATABASE_VERSION = 2;

    // Kullanıcı tablosu ve sütunları
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";


    private static final String TABLE_PARKING = "parking_status";
    private static final String COLUMN_ZONE = "zone";
    private static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kullanıcı tablosu oluşturuluyor
        String SQL_CREATE_USERS =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT," +
                        COLUMN_EMAIL + " TEXT," +
                        COLUMN_PASSWORD + " TEXT," +
                        COLUMN_ROLE + " TEXT" +
                        ")";
        db.execSQL(SQL_CREATE_USERS);

        // Park yeri tablosu oluşturuluyor
        String SQL_CREATE_PARKING_TABLE =
                "CREATE TABLE " + TABLE_PARKING + " (" +
                        COLUMN_ZONE + " TEXT PRIMARY KEY," +
                        COLUMN_STATUS + " INTEGER DEFAULT 0" +
                        ")";
        db.execSQL(SQL_CREATE_PARKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        onCreate(db);
    }

    // Kullanıcı kaydı
    public boolean addUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Kullanıcı girişi
    public boolean checkUser(String username, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?";
        String[] selectionArgs = {username, password, role};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_ROLE)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }
    // Park yeri durumunu güncelle
    public boolean updateStatus(String zone, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ZONE, zone);
        values.put(COLUMN_STATUS, status);
        long result = db.replace(TABLE_PARKING, null, values); // Eğer zone zaten varsa güncelleme yapacak
        return result != -1;
    }

    // Park yeri durumunu al
    public int getStatus(String zone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARKING, new String[]{COLUMN_STATUS}, COLUMN_ZONE + "=?", new String[]{zone}, null, null, null);
        int status = -1;
        if (cursor != null && cursor.moveToFirst()) {
            int statusColumnIndex = cursor.getColumnIndex(COLUMN_STATUS);
            if (statusColumnIndex != -1) {
                status = cursor.getInt(statusColumnIndex);
            }
            cursor.close();
        }
        return status;
    }
}
