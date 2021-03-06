package com.example.shoppingapp.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapp.Model.Product;
import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    ImageView productImage;
    ElegantNumberButton counter;
    TextView name,price,description,category;
    Button addToCart;
    ProgressDialog loadingBar;
    private String productID="",receivedPhone,receivedAddress,receivedImage,receivedName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        counter=findViewById(R.id.counter);
        name=findViewById(R.id.product_detail_name);
        productImage=findViewById(R.id.product_detail_image);
        description=findViewById(R.id.product_detail_description);
        price=findViewById(R.id.product_detail_price);
        addToCart=findViewById(R.id.add_to_cart);
        category=findViewById(R.id.product_detail_category);

        loadingBar=new ProgressDialog(this);

        productID=getIntent().getStringExtra("pid");
        receivedPhone=getIntent().getStringExtra("phone");
        receivedImage=getIntent().getStringExtra("image");
        receivedAddress=getIntent().getStringExtra("address");
        receivedName=getIntent().getStringExtra("username");

        getProductDetails(productID);


        try {
            counter.setNumber(getIntent().getStringExtra("counter"));
        }
        catch (Exception e)
        {
            counter.setNumber("1");
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

    }

    private void addingToCartList() {

        String date,time;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("MM dd, yyyy");
        date=format.format(calendar.getTime());

        format=new SimpleDateFormat("HH:mm:ss a");
        time=format.format(calendar.getTime());

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String ,Object>map=new HashMap<>();
        map.put("ProductID",productID);
        map.put("Name",name.getText().toString());
        map.put("Description",description.getText().toString());
        map.put("Date",date);
        map.put("Time",time);
        map.put("Price",price.getText().toString());
        map.put("Quantity",counter.getNumber());
        map.put("Image",receivedImage);
        map.put("Discount","");

        ref.child(receivedPhone).child("Products").child(productID)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(ProductDetails.this,Cart.class);
                            intent.putExtra("username",receivedName);
                            intent.putExtra("phone",receivedPhone);
                            intent.putExtra("image",receivedImage);
                            intent.putExtra("address",receivedAddress);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
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
                    category.setText(product.getCategory());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}