package com.example.mac.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mac.inventoryapp.Data.StoreContract.StoreEntry;

/**
 * Created by mac on 18/09/17.
 */

public class StoreDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = StoreDbHelper.class.getSimpleName();

    /**
     * Name of the data base file
     */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema,you must increment the version.
     */

    private static final int DATABASE_VERSION = 1;


    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE" + " "+StoreEntry.TABLE_NAME + " ("
                + StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StoreEntry.COLUMN_PRODUCT_IMAGES + " TEXT NOT NULL, "
                + StoreEntry.COLUMN_CUST_NAME + " TEXT NOT NULL, "
                + StoreEntry.COLUMN_INV_ITEM + " INTEGER NOT NULL, "
                + StoreEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + StoreEntry.COLUMN_AVAILABLE_UNITS + " INTEGER NOT NULL DEFAULT 0, "
                + StoreEntry.COLUMN_SHIP_TO_ADDRESS + " TEXT NOT NULL);";



    Log.d( "StoreDbHelper", "++++++++++++++++++++ " + SQL_CREATE_INVENTORY_TABLE);
        /**
         * Execute the SQL Statement.
         */
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
