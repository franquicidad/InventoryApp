package com.example.mac.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mac.inventoryapp.Data.StoreContract.StoreEntry;

/**
 * Created by mac on 19/09/17.
 */

public class StoreProvider extends ContentProvider {

    private StoreDbHelper mDbHelper;

    private static final int INVENTORY = 100;

    private static final int INVENTORY_ID = 101;

    public static final String LOG_TAG = StoreProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(StoreEntry.CONTENT_AUTHORITY, StoreEntry.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(StoreEntry.CONTENT_AUTHORITY, StoreEntry.PATH_INVENTORY + "/#", INVENTORY_ID);
    }


    @Override
    public boolean onCreate() {

        mDbHelper = new StoreDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return StoreEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertInv(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
    }

    private Uri insertInv(Uri uri, ContentValues contentValues) {

        Integer productName = contentValues.getAsInteger(StoreEntry.COLUMN_INV_ITEM);
        if (productName == null || !StoreEntry.isValidItem(productName)) {
            throw new IllegalArgumentException("Product requires a name");
        }
        Integer productPrice = contentValues.getAsInteger(StoreEntry.COLUMN_PRICE);
        if (productPrice != null || productPrice < 0) {
            throw new IllegalArgumentException("Product requires a Price and cant be less or equal to 0");
        }
        Integer productQuantity = contentValues.getAsInteger(StoreEntry.COLUMN_AVAILABLE_UNITS);
        if (productQuantity < 0) {
            throw new IllegalArgumentException("Product requires Quantity and cant be less than 0");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Long id = database.insert(StoreEntry.TABLE_NAME, null, contentValues);

        if ((id == -1)) {
            Log.e(LOG_TAG, "Failed to insert row");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs) {
        SQLiteDatabase database=mDbHelper.getWritableDatabase();

        final int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                return database.delete(StoreEntry.TABLE_NAME,selection,selectionArgs);
            case INVENTORY_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return database.delete(StoreEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateInv(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateInv(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateInv(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        Integer productName = contentValues.getAsInteger(StoreEntry.COLUMN_INV_ITEM);
        if (productName == null || !StoreEntry.isValidItem(productName)) {
            throw new IllegalArgumentException("Product requires a name");
        }
        Integer productPrice = contentValues.getAsInteger(StoreEntry.COLUMN_PRICE);
        if (productPrice != null || productPrice < 0) {
            throw new IllegalArgumentException("Product requires a Price and cant be less or equal to 0");
        }
        Integer productQuantity = contentValues.getAsInteger(StoreEntry.COLUMN_AVAILABLE_UNITS);
        if (productQuantity < 0) {
            throw new IllegalArgumentException("Product requires Quantity and cant be less than 0");
        }
        if (contentValues.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(StoreEntry.TABLE_NAME, contentValues, selection, selectionArgs);


    }
}


