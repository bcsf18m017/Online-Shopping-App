package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MaintainProducts extends AppCompatActivity {

    EditText name,price,description;
    Button edit;
    ImageView imageView;
    String receivedName,receivedPrice,receivedDescription,receivedImage,productID;
    private DatabaseReference ref;
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
        productID=getIntent().getStringExtra("pid");

        ref= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);



        name.setText(receivedName);
        description.setText(receivedDescription);
        price.setText(receivedPrice);

        Picasso.get().load(receivedImage).placeholder(R.drawable.profile).into(imageView);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct();
            }
        });

    }

    private void editProduct() {
            
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(MaintainProducts.this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price.getText().toString()))
        {
            Toast.makeText(MaintainProducts.this, "Enter Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description.getText().toString()))
        {
            Toast.makeText(MaintainProducts.this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> map=new HashMap<>();
            map.put("ProductID",productID);
            map.put("Description",description.getText().toString());
            //map.put("Image",downloadImageUrl);
            map.put("Price",price.getText().toString());
            map.put("Name",name.getText().toString());

            ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(MaintainProducts.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(MaintainProducts.this,AdminProductsDisplay.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }
}