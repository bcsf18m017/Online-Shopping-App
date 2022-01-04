package com.example.shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingapp.Model.CartItem;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UserProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference productRef;
    RecyclerView.LayoutManager layoutManager;
    String receivedPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products);

        receivedPhone=getIntent().getStringExtra("phone");
        productRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                .child(receivedPhone).child("Products");

        recyclerView=findViewById(R.id.user_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CartItem>options=new FirebaseRecyclerOptions.Builder<CartItem>()
                .setQuery(productRef,CartItem.class).build();
        FirebaseRecyclerAdapter<CartItem, CartViewHolder>adapter=new FirebaseRecyclerAdapter<CartItem, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItem model) {


                holder.itemName.setText(model.getName());
                holder.itemPrice.setText(model.getPrice());
                holder.itemQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.itemImage);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent, false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}