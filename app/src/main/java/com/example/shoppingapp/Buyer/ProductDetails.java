package com.example.shoppingapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    FloatingActionButton cart;
    ImageView productImage;
    ElegantNumberButton counter;
    TextView name,price,description;
    Button addToCart;
    private String productID="",receivedPhone,receivedAddress,receivedImage,receivedName,state="normal";
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
        addToCart=findViewById(R.id.add_to_cart);

        productID=getIntent().getStringExtra("pid");
        receivedPhone=getIntent().getStringExtra("phone");
        receivedImage=getIntent().getStringExtra("image");
        receivedAddress=getIntent().getStringExtra("address");
        receivedName=getIntent().getStringExtra("username");
        Toast.makeText(ProductDetails.this, productID, Toast.LENGTH_SHORT).show();

        getProductDetails(productID);

       // checkOrderStat();
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetails.this, "You Can Purchase More Products Once You Will Receive Your Order"
                            , Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }

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

        ref.child("User View").child(receivedPhone).child("Products").child(productID)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            ref.child("Admin View").child(receivedPhone).child("Products").child(productID)
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetails.this, "Added to Cart List Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ProductDetails.this,Home.class);
                                                intent.putExtra("Username",receivedName);
                                                intent.putExtra("phone",receivedPhone);
                                                intent.putExtra("image",receivedImage);
                                                intent.putExtra("address",receivedAddress);
                                                startActivity(intent);
                                            }
                                        }
                                    });
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
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkOrderStat()
    {
        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(receivedPhone);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String state=snapshot.child("State").getValue().toString();
                    if(state.equals("Not Shipped"))
                    {
                        state="Order Placed";
                    }
                    else
                    {
                        state="Order Shipped";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStat();
    }
}