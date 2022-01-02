package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmOrder extends AppCompatActivity {

    EditText name,address,phone,city;
    Button confirmButton;String total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        name=findViewById(R.id.shipment_name);
        address=findViewById(R.id.shipment_address);
        city=findViewById(R.id.shipment_city);
        phone=findViewById(R.id.shipment_phone);

        confirmButton=findViewById(R.id.shipment_confirm_button);
        total=getIntent().getStringExtra("Total");
        Toast.makeText(ConfirmOrder.this, total, Toast.LENGTH_SHORT).show();


    }
}