package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapp.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
        Toast.makeText(ProductDetails.this, productID, Toast.LENGTH_SHORT).show();

        getProductDetails(productID);

    }

    private void getProductDetails(String productID) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Products");

        ref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Product product=snapshot.getValue(Product.class);
                    name.setText(product.getName());
                    price.setText(product.getPrice());
                    description.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}