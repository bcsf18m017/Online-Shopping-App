package com.example.shoppingapp.Admin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Buyer.MainActivity;
import com.example.shoppingapp.Model.Orders;
import com.example.shoppingapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class AdminNewOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderRef;
    BottomNavigationView navigation;
    int count=0;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerView=findViewById(R.id.order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        navigation=findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.order_nav_option);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.order_nav_option:
                       return true;
                    case R.id.add_nav_option:
                        Intent intent1=new Intent(getApplicationContext(), AdminNewProduct.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.products_nav_option:
                        Intent intent2=new Intent(getApplicationContext(), AdminProductsDisplay.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.logout_nav_option:
                        Paper.book().destroy();
                        Intent intent=new Intent(AdminNewOrder.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

//    protected void  register()
//    {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
//        {
//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        }
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//        {
//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        }
//
//    }
//
//    protected  void unregister()
//    {
//        try {
//            unregisterReceiver(broadcastReceiver);
//        }
//        catch (Exception e)
//        {
//
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Orders>options=new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef,Orders.class).build();

        FirebaseRecyclerAdapter<Orders,OrdersViewHolder> adapter=new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Orders model) {

                count++;
                holder.username.setText(model.getName());
                holder.phone.setText(model.getPhone());
                holder.total.setText(model.getTotalAmount());
                holder.dateTime.setText(model.getDate()+" "+model.getTime());
                holder.address.setText(model.getAddress()+","+model.getCity());

                holder.orderDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AdminNewOrder.this, UserProducts.class);
                        intent.putExtra("OID",model.getDate()+model.getTime());
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{"Yes","No"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrder.this);
                        builder.setTitle("Have You Shipped this order?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    try {
                                        String uid=model.getDate()+model.getTime();
                                        removeOrder(uid);
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                }
                                else
                                {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
                return new OrdersViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uid) {

        orderRef.child(uid).removeValue();
        FirebaseDatabase.getInstance().getReference().child("OrderedProducts").child(uid).removeValue();

    }

    public static class OrdersViewHolder extends  RecyclerView.ViewHolder
    {

        public TextView username,phone,total,address,dateTime;
        public Button orderDetails;
        public OrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username=itemView.findViewById(R.id.order_user_name);
            phone=itemView.findViewById(R.id.order_phone_number);
            total=itemView.findViewById(R.id.order_price);
            address=itemView.findViewById(R.id.order_address);
            dateTime=itemView.findViewById(R.id.order_date_time);
            orderDetails=itemView.findViewById(R.id.order_details_button);

        }
    }

}