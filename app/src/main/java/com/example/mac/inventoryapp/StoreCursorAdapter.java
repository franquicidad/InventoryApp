package com.example.mac.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.inventoryapp.Data.StoreContract;
import com.example.mac.inventoryapp.Data.StoreDbHelper;

/**
 * Created by mac on 21/09/17.
 */

public class StoreCursorAdapter extends CursorAdapter {

    ImageView imageProduct;
    TextView product, price;

    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_layout_design, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final TextView quantity=(TextView)view.findViewById(R.id.ListQuantity);



        imageProduct = (ImageView) view.findViewById(R.id.imageProductType);
        product = (TextView) view.findViewById(R.id.ListProductName);
        price = (TextView) view.findViewById(R.id.ListPrice);

        int imageColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_IMAGES);
        int productColumnIndex = cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_INV_ITEM);
        int priceColumnIndex = cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS);
        int rowIndex = cursor.getColumnIndex(StoreContract.StoreEntry._ID);

        final int rowId = cursor.getInt(rowIndex);


        String imageProduct2 = cursor.getString(imageColumnIndex);
        Uri imageUri = Uri.parse(imageProduct2);
        String productName = cursor.getString(productColumnIndex);

        int productInt=Integer.parseInt(productName);
        String ProductName;
        if(productInt==0){
            ProductName="Headphones";
        }else if(productInt==1){
            ProductName="Guitar Jackson";
        }else if(productInt==2){
            ProductName="Guitar Esp";
        }else if(productInt==3){
            ProductName="Guitar Fender";
        }else if(productInt== 4){
            ProductName="Mapex Drums";
        }else{
            ProductName="Tama Drums";
        }
        String priceProduct = cursor.getString(priceColumnIndex);
        String quantityAvailable = cursor.getString(quantityColumnIndex);

        product.setText(ProductName);
        Log.v("TAG", "The product name is----------->>>>>>>>>>>>>>>>"+productName);
        price.setText(priceProduct);
        imageProduct.setImageURI(imageUri);
        quantity.setText(quantityAvailable);

        Button saleButton = (Button) view.findViewById(R.id.purchaseListView);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreDbHelper dbHelper = new StoreDbHelper(context);
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                int items = Integer.parseInt(quantity.getText().toString());
                if (items > 0) {
                    int mQuantitySold = items - 1;

                    Log.v("TAG", " The current ITEMSSS VALUE IS:--------------------------------->" + mQuantitySold);

                    ContentValues values = new ContentValues();

                    values.put(StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS, mQuantitySold);

                    String selection = StoreContract.StoreEntry._ID + "=?";

                    String[] selectionArgs = new String[]{String.valueOf(rowId)};
                    Log.v("my_tag", "row id is:-------------------------------------------> " + rowId);

                    int rowsAffected = database.update(StoreContract.StoreEntry.TABLE_NAME, values, selection, selectionArgs);

                    if (rowsAffected != -1) {
                        quantity.setText(String.valueOf(mQuantitySold));
                    }
                } else {
                    Toast.makeText(context, "No stock left ", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}
