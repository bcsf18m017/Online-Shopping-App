package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetails extends AppCompatActivity {

    FloatingActionButton cart;
    ImageView productImage;
    ElegantNumberButton counter;
    TextView name,price,description;
    private String productID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        counter=findViewById(R.id.counter);
        name=findViewById(R.id.product_detail_name);
        productImage=findViewById(R.id.product_detail_image);
        description=findViewById(R.id.product_detail_description);
        price=findViewById(R.id.product_detail_price);
        cart=findViewById(R.id.cart_button);

        productID=getIntent().getStringExtra("pid"); 

    }
}