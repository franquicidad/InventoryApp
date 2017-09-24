package com.example.mac.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.inventoryapp.Data.StoreContract;

/**
 * Created by mac on 18/09/17.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Global variables for views on the Layout*/
    private EditText mNameEdit,mShipto;
    private Spinner mProdSpinner;
    private ImageView mProdImages;
    private TextView mPriceText;
    private TextView mQuantity;
    private Button mQuantityPlus;
    private Button mQuantityMinus;
    /** Identifier for the pet data loader */
    private static final int EDITOR_LOADER = 0;

    /** Content URI for the existing pet (null if it's a new pet) */

    private Uri mCurrentInvUri;

    private boolean mInvHasChanged =false;
    /** If the user doesnt select nothing then we cant have a null product
     * so by default the headphone will be selected.
     */
    private int mProduct=0;

    int quantityCountVariable=0;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_add_layout);

        Intent intent=getIntent();
        mCurrentInvUri=intent.getData();

        /**if the intent DOES NOT contain an URI, then we are creating a new product*/
        if (mCurrentInvUri == null) {
            /**As this is a new product then change the title bar to display create product*/
            setTitle("Create a Product");
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            supportInvalidateOptionsMenu();

        } else {

            setTitle("Edit product");
            getSupportLoaderManager().initLoader(EDITOR_LOADER, null, this);
        }

        mNameEdit=(EditText)findViewById(R.id.enterName);
        mShipto=(EditText)findViewById(R.id.EditAddress);
        mProdSpinner=(Spinner)findViewById(R.id.SpinnerProduct);
        mProdImages=(ImageView)findViewById(R.id.imageProductType);
        mPriceText=(TextView)findViewById(R.id.price);
        mQuantity=(TextView)findViewById(R.id.QuantityUnitsText);
        mQuantityPlus=(Button)findViewById(R.id.PlusButton);
        mQuantityMinus=(Button)findViewById(R.id.MinusButton);

        /** Set listeners for the buttons to change the mQuantity variable*/

        mQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment(mQuantity);
            }
        });

        mQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement(mQuantity);
            }
        });

        mNameEdit.setOnTouchListener(mTouchlistener);
        mShipto.setOnTouchListener(mTouchlistener);
        mProdSpinner.setOnTouchListener(mTouchlistener);
        mProdImages.setOnTouchListener(mTouchlistener);
        mPriceText.setOnTouchListener(mTouchlistener);
        mQuantity.setOnTouchListener(mTouchlistener);

        setupSpinner();
    }
    /** Method to increment the units available*/

    public void increment(View view){
        if(quantityCountVariable==50){
            Toast.makeText(this, "You cannot order more than 50 units",Toast.LENGTH_LONG).show();
            return;
        }
        quantityCountVariable++;

        mQuantity.setText(" "+quantityCountVariable);
    }
    /** Method to reduce the inventory units available*/

    public void decrement(View view){
        if(quantityCountVariable<=0){
            Toast.makeText(this, "You cant less than 0 of a product!",Toast.LENGTH_LONG).show();
            return;
        }
        quantityCountVariable--;
        mQuantity.setText(""+quantityCountVariable);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentInvUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_inv);
            menuItem.setVisible(false);
        }return true;
    }

    private View.OnTouchListener mTouchlistener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mInvHasChanged=true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press.
        if(!mInvHasChanged){
        super.onBackPressed();
        return;
    }

    DialogInterface.OnClickListener discardButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User clicked "Discard" button, close the current activity.
                    finish();
                }
            };

    // Show dialog that there are unsaved changes
    showUnsavedChangesDialog(discardButtonClickListener);
}

    /** Now setup the spinner that allows the user to choose the product
     *
     */

    private void setupSpinner(){
        ArrayAdapter productSpinAdapter= ArrayAdapter.createFromResource(
                this,R.array.product_options,android.R.layout.simple_spinner_item);

        productSpinAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mProdSpinner.setAdapter(productSpinAdapter);


        mProdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection=(String)parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)){
                    if(selection.equals("Headphones")) {
                        mProduct = StoreContract.StoreEntry.ITEM_HEADPHONES;
                        mProdImages.setImageResource(R.drawable.audifonos);
                        mPriceText.setText("$10.00");
                    }else if(selection.equals("Guitar jackson")){
                        mProduct= StoreContract.StoreEntry.ITEM_GUITAR_JACKSON;
                        mProdImages.setImageResource(R.drawable.jacksonguitar);
                        mPriceText.setText("$1000");
                    }else if(selection.equals("Guitar Esp")){
                        mProduct= StoreContract.StoreEntry.ITEM_GUITAR_ESP;
                        mProdImages.setImageResource(R.drawable.espguitar);
                    }else if (selection.equals("Guitar Fender")){
                        mProduct= StoreContract.StoreEntry.ITEM_GUITAR_FENDER;
                        mProdImages.setImageResource(R.drawable.fendergui);
                    }else if (selection.equals("Mapex Drums")){
                        mProduct= StoreContract.StoreEntry.ITEM_DRUMKIT_MAPEX;
                        mProdImages.setImageResource(R.drawable.mapexdrum);
                    }else if (selection.equals("Tama Drums")){
                        mProduct= StoreContract.StoreEntry.ITEM_DRUMKIT_TAMA;
                        mProdImages.setImageResource(R.drawable.tamadrum);
                    }else{
                        mProduct=0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mProduct=0;
            }
        });

    }

    private void saveProduct(){
        String custName= mNameEdit.getText().toString().trim();
        String Price=mPriceText.getText().toString().trim();
        String Quantity=mQuantity.getText().toString().trim();
        String product= mProdSpinner.getOnItemSelectedListener().toString();
        String address=mShipto.getText().toString().trim();
        String productPicture=getResources().getInteger(mProdImages);

        if(mCurrentInvUri == null &&
                TextUtils.isEmpty(custName) &&
                TextUtils.isEmpty(Price)&&
                TextUtils.isEmpty(Quantity)&&
                TextUtils.isEmpty(product)&&
                TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter all the fields of the product",Toast.LENGTH_LONG).show();

            return;
        };

        ContentValues values=new ContentValues();

        values.put(StoreContract.StoreEntry.COLUMN_CUST_NAME,custName);
        values.put(StoreContract.StoreEntry.COLUMN_INV_ITEM,product);
        values.put(StoreContract.StoreEntry.COLUMN_AVAILABLE_UNITS,Quantity);
        values.put(StoreContract.StoreEntry.COLUMN_PRICE,Price);
        values.put(StoreContract.StoreEntry.COLUMN_SHIP_TO_ADDRESS,address);


    }









    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
