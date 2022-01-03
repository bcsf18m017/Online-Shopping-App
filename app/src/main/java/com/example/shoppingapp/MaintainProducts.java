package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MaintainProducts extends AppCompatActivity {

    EditText name,price,description;
    Button edit;
    ImageView imageView;
    String receivedName,receivedPrice,receivedDescription,receivedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_products);

        name=findViewById(R.id.editProductName);
        imageView=findViewById(R.id.editProductImage);
        description=findViewById(R.id.editProductDescription);
        price=findViewById(R.id.editProductPrice);
        edit=findViewById(R.id.updateProductButton);

        receivedName=getIntent().getStringExtra("name");
        receivedDescription=getIntent().getStringExtra("description");
        receivedImage=getIntent().getStringExtra("image");
        receivedPrice=getIntent().getStringExtra("price");



        name.setText(receivedName);
        description.setText(receivedDescription);
        price.setText(receivedPrice);

        Picasso.get().load(receivedImage).placeholder(R.drawable.profile).into(imageView);

    }
}