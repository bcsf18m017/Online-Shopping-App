package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shoppingapp.Model.Product;
import com.example.shoppingapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProduct extends AppCompatActivity {

    EditText searchText;
    Button searchButton;
    RecyclerView recyclerView;
    String searchInput,user,phoneReceived,imageReceived,addressReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        searchButton=findViewById(R.id.searchProductButton);
        searchText=findViewById(R.id.searchProductName);
        recyclerView=findViewById(R.id.searchList);

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchProduct.this));

        user=getIntent().getExtras().get("username").toString();
        phoneReceived=getIntent().getExtras().get("phone").toString();
        imageReceived=getIntent().getExtras().get("image").toString();
        addressReceived=getIntent().getExtras().get("address").toString();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput=searchText.getText().toString();
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Product>options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(ref.orderByChild("Name").startAt(searchInput).endAt(searchInput),Product.class).build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.name.setText(model.getName());
                holder.price.setText(model.getPrice()+"$");
                Picasso.get().load(model.getImage()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchProduct.this,ProductDetails.class);
                        intent.putExtra("pid",model.getDate()+model.getTime());
                        intent.putExtra("phone",phoneReceived);
                        intent.putExtra("image",model.getImage());
                        intent.putExtra("address",addressReceived);
                        intent.putExtra("username",user);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}