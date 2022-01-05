package com.example.shoppingapp.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity {

    EditText name,address,phone,city;
    Button confirmButton;String total;
    private String receivedPhone,receivedAddress,receivedImage,receivedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


        receivedPhone=getIntent().getStringExtra("phone");
        receivedImage=getIntent().getStringExtra("image");
        receivedAddress=getIntent().getStringExtra("address");
        receivedName=getIntent().getStringExtra("username");
        name=findViewById(R.id.shipment_name);
        address=findViewById(R.id.shipment_address);
        city=findViewById(R.id.shipment_city);
        phone=findViewById(R.id.shipment_phone);

        confirmButton=findViewById(R.id.shipment_confirm_button);
        total=getIntent().getStringExtra("Total");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


    }


    private void validateData() {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(ConfirmOrder.this, "Please Provide Your Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone.getText().toString()))
        {
            Toast.makeText(ConfirmOrder.this, "Please Provide Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getText().toString()))
        {
            Toast.makeText(ConfirmOrder.this, "Please Provide Your Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(city.getText().toString()))
        {
            Toast.makeText(ConfirmOrder.this, "Please Provide Your City", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confrimOrder();
        }
    }
//
//    private void confirmOrder() {
//
//        final String date,time;
//        Calendar calendar=Calendar.getInstance();
//        SimpleDateFormat format=new SimpleDateFormat("MM dd, yyyy");
//        date=format.format(calendar.getTime());
//
//        format=new SimpleDateFormat("HH:mm:ss a");
//        time=format.format(calendar.getTime());
//
//        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(date+time);
//        HashMap<String,Object>map=new HashMap<>();
//        map.put("TotalAmount",total);
//        map.put("Name",name.getText().toString());
//        map.put("Address",address.getText().toString());
//        map.put("City",city.getText().toString());
//        map.put("Phone",receivedPhone);
//        map.put("Date",date);
//        map.put("Time",time);
//        map.put("State","Not Shipped");
//        orderRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//
//                    FirebaseDatabase.getInstance().getReference().child("Cart List")
//                            .child(receivedPhone).child("Products").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()) {
//                                final DatabaseReference temp= FirebaseDatabase.getInstance().getReference().child("OrderedProducts").child(date+time);
//                                for (DataSnapshot post : snapshot.getChildren())
//                                {
//                                    ArrayList<String>arr=new ArrayList<>();
//                                    for(DataSnapshot o:post.getChildren())
//                                    {
//                                        arr.add(o.getValue().toString());
//                                    }
//                                    HashMap<String,Object>m=new HashMap<>();
//                                    m.put("ProductID",arr.get(6));
//                                    m.put("Name",arr.get(4));
//                                    m.put("Description",arr.get(1));
//                                    m.put("Date",arr.get(0));
//                                    m.put("Time",arr.get(8));
//                                    m.put("Price",arr.get(5));
//                                    m.put("Quantity",arr.get(7));
//                                    m.put("Image",arr.get(3));
//                                    m.put("Discount",arr.get(2));
//                                    temp.child(arr.get(6)).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            FirebaseDatabase.getInstance().getReference().child("Cart List")
//                                                    .child(receivedPhone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful())
//                                                    {
//                                                        Toast.makeText(ConfirmOrder.this,"Order Has Been Placed",Toast.LENGTH_SHORT).show();
//                                                        Intent intent=new Intent(ConfirmOrder.this,Home.class);
//                                                        intent.putExtra("Username",receivedName);
//                                                        intent.putExtra("phone",receivedPhone);
//                                                        intent.putExtra("image",receivedImage);
//                                                        intent.putExtra("address",receivedAddress);
//                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                }
//            }
//        });
//    }


    private void confrimOrder() {

        final String date,time;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("MM dd, yyyy");
        date=format.format(calendar.getTime());

        format=new SimpleDateFormat("HH:mm:ss a");
        time=format.format(calendar.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(date+time);
        HashMap<String,Object>map=new HashMap<>();
        map.put("TotalAmount",total);
        map.put("Name",name.getText().toString());
        map.put("Address",address.getText().toString());
        map.put("City",city.getText().toString());
        map.put("Phone",phone.getText().toString());
        map.put("Date",date);
        map.put("Time",time);
        map.put("State","Not Shipped");
        addOrderedProducts(date+time);


        orderRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    //addOrderedProducts(date+time);
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child(receivedPhone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ConfirmOrder.this,"Order Has Been Placed",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ConfirmOrder.this,Home.class);
                                intent.putExtra("Username",receivedName);
                                intent.putExtra("phone",receivedPhone);
                                intent.putExtra("image",receivedImage);
                                intent.putExtra("address",receivedAddress);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void addOrderedProducts(String oid)
    {

        FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child(receivedPhone).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    final DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("OrderedProducts").child(oid);
                            temp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            if(snapshot1.getValue()==null)
                            {
                                for (DataSnapshot post : snapshot.getChildren())
                                {
                                    ArrayList<String> arr = new ArrayList<>();
                                    for (DataSnapshot o : post.getChildren()) {
                                        arr.add(o.getValue().toString());
                                    }
                                    HashMap<String, Object> m = new HashMap<>();
                                    m.put("ProductID", arr.get(6));
                                    m.put("Name", arr.get(4));
                                    m.put("Description", arr.get(1));
                                    m.put("Date", arr.get(0));
                                    m.put("Time", arr.get(8));
                                    m.put("Price", arr.get(5));
                                    m.put("Quantity", arr.get(7));
                                    m.put("Image", arr.get(3));
                                    m.put("Discount", arr.get(2));
                                    temp.child(arr.get(6)).updateChildren(m);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}