package com.example.shoppingapp.Buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Model.CartItem;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button next;
    private TextView total,message;
    private String receivedPhone,receivedAddress,receivedImage,receivedName;
    int totalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        message=findViewById(R.id.order_message);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        next=findViewById(R.id.nextButton);
        total=findViewById(R.id.total_price);


        receivedPhone=getIntent().getStringExtra("phone");
        receivedImage=getIntent().getStringExtra("image");
        receivedAddress=getIntent().getStringExtra("address");
        receivedName=getIntent().getStringExtra("username");
        loadCartData();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cart.this,ConfirmOrder.class);
                intent.putExtra("Total",String.valueOf(totalPrice));
                intent.putExtra("username",receivedName);
                intent.putExtra("phone",receivedPhone);
                intent.putExtra("image",receivedImage);
                intent.putExtra("address",receivedAddress);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
//        FirebaseRecyclerOptions<CartItem>options=new FirebaseRecyclerOptions.Builder<CartItem>()
//                .setQuery(cartListRef.child(receivedPhone).child("Products"),CartItem.class).build();
//        FirebaseRecyclerAdapter<CartItem, CartViewHolder>adapter=new FirebaseRecyclerAdapter<CartItem, CartViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItem model) {
//
//                holder.itemName.setText(model.getName());
//                holder.itemPrice.setText(model.getPrice());
//                holder.itemQuantity.setText(model.getQuantity());
//                Picasso.get().load(model.getImage()).into(holder.itemImage);
//                int tempPrice=(Integer.parseInt(model.getPrice()))*(Integer.parseInt(model.getQuantity()));
//
//                totalPrice=totalPrice+tempPrice;
//                total.setText("Total Price ="+String.valueOf(totalPrice)+"$");
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CharSequence choice[]=new CharSequence[]{"Edit","Remove"};
//                        AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
//                         builder.setTitle("Choose an option:");
//                         builder.setItems(choice, new DialogInterface.OnClickListener() {
//                             @Override
//                             public void onClick(DialogInterface dialog, int which) {
//                                    if(which==0)
//                                    {
//                                        Intent intent=new Intent(Cart.this,ProductDetails.class);
//                                        intent.putExtra("username",model.getName());
//                                        intent.putExtra("phone",receivedPhone);
//                                        intent.putExtra("image",model.getImage());
//                                        intent.putExtra("address",receivedAddress);
//                                        intent.putExtra("pid",model.getProductID());
//                                        intent.putExtra("counter",model.getQuantity());
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    else if(which==1)
//                                    {
//                                        cartListRef.child(receivedPhone).child("Products").child(model.getProductID())
//                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful())
//                                                    {
//                                                        Toast.makeText(Cart.this, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
//                                                        Intent intent=new Intent(Cart.this,Home.class);
//                                                        intent.putExtra("Username",receivedName);
//                                                        intent.putExtra("phone",receivedPhone);
//                                                        intent.putExtra("image",receivedImage);
//                                                        intent.putExtra("address",receivedAddress);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                            }
//                                        });
//                                    }
//                             }
//                         });
//                         builder.show();
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent, false);
//                CartViewHolder holder=new CartViewHolder(view);
//                return holder;
//            }
//        };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }


    private  void  loadCartData()
    {
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<CartItem>options=new FirebaseRecyclerOptions.Builder<CartItem>()
                .setQuery(cartListRef.child(receivedPhone).child("Products"),CartItem.class).build();
        FirebaseRecyclerAdapter<CartItem, CartViewHolder>adapter=new FirebaseRecyclerAdapter<CartItem, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItem model) {

                holder.itemName.setText(model.getName());
                holder.itemPrice.setText(model.getPrice());
                holder.itemQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getImage()).into(holder.itemImage);
                int tempPrice=(Integer.parseInt(model.getPrice()))*(Integer.parseInt(model.getQuantity()));

                totalPrice=totalPrice+tempPrice;
                total.setText("Total Price ="+String.valueOf(totalPrice)+"$");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence choice[]=new CharSequence[]{"Edit","Remove"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
                        builder.setTitle("Choose an option:");
                        builder.setItems(choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent intent=new Intent(Cart.this,ProductDetails.class);
                                    intent.putExtra("username",model.getName());
                                    intent.putExtra("phone",receivedPhone);
                                    intent.putExtra("image",model.getImage());
                                    intent.putExtra("address",receivedAddress);
                                    intent.putExtra("pid",model.getProductID());
                                    intent.putExtra("counter",model.getQuantity());
                                    startActivity(intent);
                                    finish();
                                }
                                else if(which==1)
                                {
                                    cartListRef.child(receivedPhone).child("Products").child(model.getProductID())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(Cart.this, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(Cart.this,Home.class);
                                                intent.putExtra("Username",receivedName);
                                                intent.putExtra("phone",receivedPhone);
                                                intent.putExtra("image",receivedImage);
                                                intent.putExtra("address",receivedAddress);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
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