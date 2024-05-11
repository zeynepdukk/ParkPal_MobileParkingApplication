package com.example.parkpal1;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    private static final String TABLE_PARKING = "parking_status";
    private static final String COLUMN_ZONE = "zone";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CODE = "code";

    private static final String TABLE_PARKING_STATUS = "parking_status2";
    private static final String COLUMN_AVAILABLE_SPACE = "available_space";
    private static final String COLUMN_FULL_SPACE = "full_space";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USERS =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT," +
                        COLUMN_EMAIL + " TEXT," +
                        COLUMN_PASSWORD + " TEXT," +
                        COLUMN_ROLE + " TEXT" +
                        ")";
        db.execSQL(SQL_CREATE_USERS);

        String SQL_CREATE_PARKING_TABLE =
                "CREATE TABLE " + TABLE_PARKING + " (" +
                        COLUMN_ZONE + " TEXT PRIMARY KEY," +
                        COLUMN_STATUS + " INTEGER DEFAULT 0," +
                        COLUMN_CODE + " TEXT" + // Yeni sütun ekleme
                        ")";
        db.execSQL(SQL_CREATE_PARKING_TABLE);

        String SQL_CREATE_PARKING_STATUS_TABLE =
                "CREATE TABLE " + TABLE_PARKING_STATUS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_AVAILABLE_SPACE + " INTEGER DEFAULT 0," +
                        COLUMN_FULL_SPACE + " INTEGER DEFAULT 0" +
                        ")";
        db.execSQL(SQL_CREATE_PARKING_STATUS_TABLE);

        initParkingSpaces();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING_STATUS);
        onCreate(db);
    }
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
    public boolean updateStatus(String zone, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ZONE, zone);
        values.put(COLUMN_STATUS, status);
        long result = db.replace(TABLE_PARKING, null, values);
        return result != -1;
    }

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
    String generateRandomCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }
    public boolean addParkingZone(String zone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ZONE, zone);
        values.put(COLUMN_STATUS, 0);
        values.put(COLUMN_CODE, generateRandomCode());
        long result = db.insert(TABLE_PARKING, null, values);
        return result != -1;
    }
    public String getCode(String zone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARKING, new String[]{COLUMN_CODE}, COLUMN_ZONE + "=?", new String[]{zone}, null, null, null);
        String code = null;
        if (cursor != null && cursor.moveToFirst()) {
            int codeColumnIndex = cursor.getColumnIndex(COLUMN_CODE);
            if (codeColumnIndex != -1) {
                code = cursor.getString(codeColumnIndex);
            }
            cursor.close();
        }
        return code;
    }
    public boolean updateCode(String zone, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, code);
        String selection = COLUMN_ZONE + "=?";
        String[] selectionArgs = { zone };
        int result = db.update(TABLE_PARKING, values, selection, selectionArgs);
        return result > 0;
    }
    public void updateSpaces(int availableSpace, int fullSpace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVAILABLE_SPACE, availableSpace);
        values.put(COLUMN_FULL_SPACE, fullSpace);
        db.update(TABLE_PARKING_STATUS, values, null, null);
    }

    public int getAvailableSpace() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_AVAILABLE_SPACE + " FROM " + TABLE_PARKING_STATUS;
        Cursor cursor = db.rawQuery(query, null);
        int availableSpace = 0;
        if (cursor.moveToFirst()) {
            availableSpace = cursor.getInt(0);
        }
        cursor.close();
        return availableSpace;
    }

    public int getFullSpace() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_FULL_SPACE + " FROM " + TABLE_PARKING_STATUS;
        Cursor cursor = db.rawQuery(query, null);
        int fullSpace = 0;
        if (cursor.moveToFirst()) {
            fullSpace = cursor.getInt(0);
        }
        cursor.close();
        return fullSpace;
    }
    public void initParkingSpaces() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PARKING_STATUS, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            if (count == 0) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_AVAILABLE_SPACE, 25);
                values.put(COLUMN_FULL_SPACE, 25);
                db.insert(TABLE_PARKING_STATUS, null, values);
            }
        }
    }
    public boolean reserveParkingSpace(String zone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ZONE, zone);
        values.put(COLUMN_STATUS, 1); // Park yeri dolu olarak işaretle
        long result = db.replace(TABLE_PARKING, null, values);
        return result != -1;
    }
    public boolean cancelParkingReservation(String zone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ZONE, zone);
        values.put(COLUMN_STATUS, 0); // Park yeri boş olarak işaretle
        long result = db.replace(TABLE_PARKING, null, values);
        return result != -1;
    }

}