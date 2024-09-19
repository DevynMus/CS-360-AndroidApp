package com.example.appdevynmustard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class inventory  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ItemInventory";
    private static final int VERSION = 3;
    private TextView dataTable;
    public static final class inventoryTable {
        public static final String TABLE = "items";
        public static final String COL_ID = "_id";
        public static final String COL_NAME = "name";
        public static final String COL_PRICE = "price";
        public static final String COL_COUNT = "count";
    }
    public inventory(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Add item to database
    public void addItem(String name, float price, int count ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(inventoryTable.COL_NAME, name);
        values.put(inventoryTable.COL_PRICE, price);
        values.put(inventoryTable.COL_COUNT, count);
        db.insert(inventoryTable.TABLE, null, values);
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
            db.execSQL("CREATE TABLE " + inventoryTable.TABLE + " (" +
                inventoryTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                inventoryTable.COL_NAME + " TEXT, " +
                inventoryTable.COL_PRICE + " FLOAT, " +
                inventoryTable.COL_COUNT + " INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + inventoryTable.TABLE);
        onCreate(db);
    }

    // Update the database item
    public void update_item(InventoryItem item, int idNew) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(inventoryTable.COL_NAME, item.getName());
        values.put(inventoryTable.COL_PRICE, item.getPrice());
        values.put(inventoryTable.COL_COUNT, item.getCount());

        // Update the row with the matching item ID
        db.update("items", values, "_id = " + idNew, null);
        db.close();
    }

    public void delete_item(InventoryItem item, int idNew) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "_id = ?", new String[]{String.valueOf(idNew)});
        db.close();
    }

    public String databaseToString() {
        String dbString = "";
        ArrayList<String> userList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + inventoryTable.TABLE + " WHERE 1";


        // Iterate through database entries via cursor function
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(inventoryTable.COL_NAME) +1) != null) {
                dbString += cursor.getString(cursor.getColumnIndex(inventoryTable.COL_NAME) +1);
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