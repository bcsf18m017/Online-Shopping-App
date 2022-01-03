package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingapp.Model.Product;
import com.example.shoppingapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminProductsDisplay extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products_display);

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productsRef,Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Product model) {

                        holder.name.setText(model.getName());
                        holder.price.setText(model.getPrice()+"$");

                        Picasso.get().load(model.getImage()).into(holder.image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence choice[]=new CharSequence[]{"Edit","Remove"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminProductsDisplay.this);
                                builder.setTitle("Choose an option:");
                                builder.setItems(choice, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            Intent intent=new Intent(AdminProductsDisplay.this,MaintainProducts.class);
                                            intent.putExtra("pid",model.getDate()+model.getTime());
                                            startActivity(intent);
                                        }
                                        else if(which==1)
                                        {
                                                String pid=getRef(position).getKey();
                                                productsRef.child(pid).removeValue();
                                        }

                                    }
                                });
                                builder.show();
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