package com.example.appdevynmustard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class user  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserTable";
    private static final int VERSION = 2;
    private TextView dataTable;
    public static final class userTable {
        public static final String TABLE = "users";
        public static final String COL_ID = "_id";
        public static final String COL_NAME = "name";
        public static final String COL_PASSWORD = "password";
    }
    public user(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Add user to database
    public void addUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(userTable.COL_NAME, name);
        values.put(userTable.COL_PASSWORD, password);
        db.insert(userTable.TABLE, null, values);
        db.close();

    }

    // Get ID from SQLITE, or return -1
    public int GetId(String searchedName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getNameId = db.rawQuery("SELECT _id FROM items WHERE name = '" + searchedName + "'", null);
        if (getNameId != null && getNameId.moveToFirst()) {
            return getNameId.getInt(0);
        } else {
            return -1;
        }
    }

    // Return all database records
    public String printDatabase() {
        String dbOut = databaseToString();
        return (dbOut);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + userTable.TABLE + " (" +
                userTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                userTable.COL_NAME + " TEXT, " +
                userTable.COL_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + userTable.TABLE);
        onCreate(db);
    }

    public boolean verifyLogin(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {userTable.COL_ID};
        String selection = userTable.COL_NAME + " = ? AND " + userTable.COL_PASSWORD + " = ?";
        String[] selectionArgs = {name, password};
        Cursor cursor = db.query(userTable.TABLE, columns, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        return userExists;
    }
    public String databaseToString() {
        String dbString = "";
        ArrayList<String> userList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + userTable.TABLE + " WHERE 1";


        // Iterate through database entries via cursor function
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(userTable.COL_NAME) +1) != null) {
                dbString += cursor.getString(cursor.getColumnIndex(userTable.COL_NAME) +1);
                dbString += "\n";
            }
            userList.add(cursor.toString());
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return dbString;
    }
}