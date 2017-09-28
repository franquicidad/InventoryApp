package com.example.mac.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mac.inventoryapp.Data.StoreContract;
import com.facebook.stetho.Stetho;

/**
 * Created by mac on 18/09/17.
 */

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int STORE_LOADER=0;

    private StoreCursorAdapter mCursorAdapter;

    private ListView listView;

    public Uri InventoryCurrentStoreUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_inventory);

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.AgregarInventario);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEdit=new Intent(getBaseContext(),EditorActivity.class);
                startActivity(intEdit);
            }
        });

        listView=(ListView)findViewById(R.id.inventoryListView);

        View emptyview= findViewById(R.id.empty_view);
        listView.setEmptyView(emptyview);

        mCursorAdapter=new StoreCursorAdapter(this,null);

        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent catalogIntent=new Intent(InventoryActivity.this,EditorActivity.class);
                InventoryCurrentStoreUri=ContentUris.withAppendedId(StoreContract.StoreEntry.CONTENT_URI,id);
                catalogIntent.setData(InventoryCurrentStoreUri);
                startActivity(catalogIntent);
            }
        });

        getSupportLoaderManager().initLoader(STORE_LOADER,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_inv:
                deleteInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBuyClick(long id, int quantity) {
        Uri currentProductUri = ContentUris.withAppendedId(StoreContract.StoreEntry.CONTENT_URI, id);
        Log.v("CatalogActivity", "Uri: " + currentProductUri);
        quantity--;
        ContentValues values = new ContentValues();
        values.put(StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS, quantity);
        int rowsEffected = getContentResolver().update(currentProductUri, values, null, null);
        Log.v("TAG","------------>"+rowsEffected);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                StoreContract.StoreEntry._ID,
                StoreContract.StoreEntry.COLUMN_PRODUCT_IMAGES,
                StoreContract.StoreEntry.COLUMN_INV_ITEM,
                StoreContract.StoreEntry.COLUMN_PRICE,
                StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS
        };
        return new CursorLoader(this,
                StoreContract.StoreEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    private void deleteInventory(){
        int rowsDeleted=getContentResolver().delete(StoreContract.StoreEntry.CONTENT_URI,null,null);
        Log.v("InventoryActivity",rowsDeleted+"Rows deleted form data base");
    }



}
