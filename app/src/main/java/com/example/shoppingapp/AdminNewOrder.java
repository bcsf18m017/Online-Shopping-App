package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingapp.Model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerView=findViewById(R.id.order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Orders>options=new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderRef,Orders.class).build();

        FirebaseRecyclerAdapter<Orders,OrdersViewHolder> adapter=new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull Orders model) {

                holder.username.setText("Username: "+model.getName());
                holder.phone.setText("Phone: "+model.getPhone());
                holder.total.setText("Total Price: "+model.getTotalAmount());
                holder.dateTime.setText("Order at: "+model.getDate()+" "+model.getTime());
                holder.address.setText("Order Address: "+model.getAddress()+","+model.getCity());
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