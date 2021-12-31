package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class signupActivity extends AppCompatActivity {

    Button signUp;
    EditText name,phone,password,confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp=findViewById(R.id.signUp);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.reg_phoneNumber);
        password=findViewById(R.id.reg_password);
        confirmPassword=findViewById(R.id.reg_confirm_password);

    }
}