package com.example.shoppingapp.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
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
        Toast.makeText(ConfirmOrder.this, total, Toast.LENGTH_SHORT).show();

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

    private void confrimOrder() {

        final String date,time;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("MM dd, yyyy");
        date=format.format(calendar.getTime());

        format=new SimpleDateFormat("HH:mm:ss a");
        time=format.format(calendar.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(receivedPhone);
        HashMap<String,Object>map=new HashMap<>();
        map.put("TotalAmount",total);
        map.put("Name",name.getText().toString());
        map.put("Address",address.getText().toString());
        map.put("City",city.getText().toString());
        map.put("Phone",phone.getText().toString());
        map.put("Date",date);
        map.put("Time",time);
        map.put("State","Not Shipped");
        orderRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
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
}