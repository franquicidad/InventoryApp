package com.example.mac.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mac.inventoryapp.Data.StoreContract;

/**
 * Created by mac on 21/09/17.
 */

public class StoreCursorAdapter extends CursorAdapter {

    ImageView imageProduct;
    TextView product,price,quantity;

    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_layout_design,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        imageProduct=(ImageView)view.findViewById(R.id.imageProductType);
        product=(TextView)view.findViewById(R.id.ListProductName);
        price=(TextView)view.findViewById(R.id.ListPrice);
        quantity=(TextView)view.findViewById(R.id.ListQuantity);

        int imageColumnIndex=cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_PRODUCT_IMAGES);
        int productColumnIndex=cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_INV_ITEM);
        int priceColumnIndex=cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_PRICE);
        int quantityColumnIndex=cursor.getColumnIndexOrThrow(StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS);

        String imageProduct2 =cursor.getString(imageColumnIndex);
        String productName=cursor.getString(productColumnIndex);
        String priceProduct=cursor.getString(priceColumnIndex);
        String quantityAvailable=cursor.getString(quantityColumnIndex);

        imageProduct.setImageResource(Integer.parseInt(imageProduct2));
        product.setText(productName);
        price.setText(priceProduct);
        quantity.setText(quantityAvailable);
    }
}
