package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapp.Model.Users;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    EditText phone,password;
    Button login;
    private CheckBox checkBox;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.login);
        phone=findViewById(R.id.phoneNumber);
        password=findViewById(R.id.password);

        loadingBar=new ProgressDialog(this);

        checkBox=findViewById(R.id.rememberMeCheckBox);
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });
    }

    private void validateUser() {
        String ph=phone.getText().toString();
        String pwd=password.getText().toString();

        if(TextUtils.isEmpty(ph))
        {
            Toast.makeText(loginActivity.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pwd))
        {
            Toast.makeText(loginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Checking");
            loadingBar.setMessage("Please wait while checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            validateAndLogin(ph,pwd);
        }



    }

    private void validateAndLogin(String ph, String pwd) {

        final DatabaseReference root;
        root= FirebaseDatabase.getInstance().getReference();

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(ph).exists())
                {
                    Users userData=snapshot.child("Users").child(ph).getValue(Users.class);
                    if(userData.getPhone().equals(ph) && userData.getPassword().equals(pwd))
                    {
                        Toast.makeText(loginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        if(checkBox.isChecked())
                        {
                            Paper.book().write(Prevalent.userPhoneKey,ph);
                            Paper.book().write(Prevalent.userPasswordKey,pwd);
                        }
                        Intent intent =new Intent(loginActivity.this,Home.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(loginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
                else
                {
                    Toast.makeText(loginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}