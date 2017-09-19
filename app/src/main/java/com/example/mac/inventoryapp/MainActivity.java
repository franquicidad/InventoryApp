package com.example.mac.inventoryapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonInv;

    private FloatingActionButton floatingActionButtonShip;

    private FloatingActionButton floatingActionButtonPurchase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Cast floating buttons and initialize them to other activities
         *
         */


        floatingActionButtonPurchase=(FloatingActionButton)findViewById(R.id.PurchaseButton);

        floatingActionButtonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent purchaseInt=new Intent(getBaseContext(),PurchasesActivity.class);
                startActivity(purchaseInt);
            }
        });

        floatingActionButtonShip=(FloatingActionButton)findViewById(R.id.ShippingButton);
        floatingActionButtonShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shipInt=new Intent(getBaseContext(),ShippingActivity.class);
                startActivity(shipInt);
            }
        });
        floatingActionButtonInv=(FloatingActionButton)findViewById(R.id.inventoryButton);
        floatingActionButtonInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invInt=new Intent(getBaseContext(),InventoryActivity.class);
                startActivity(invInt);
            }
        });



    }
}
