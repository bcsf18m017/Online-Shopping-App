package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity {

    Button add;
    EditText name,price,description;
    ImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        add=findViewById(R.id.addProduct_Button);
        name=findViewById(R.id.product_name);
        description=findViewById(R.id.product_description);
        price=findViewById(R.id.product_price);

        productImage=findViewById(R.id.product_image);
    }
}