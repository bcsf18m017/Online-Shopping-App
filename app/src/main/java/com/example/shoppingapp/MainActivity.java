package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppingapp.Model.Users;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button loginButton,signupButton;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=(Button)findViewById(R.id.main_login_button);
        signupButton=(Button)findViewById(R.id.main_signup_button);

        loadingBar=new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,signupActivity.class);
                startActivity(intent);
            }
        });

        if(Paper.book().contains(Prevalent.userPhoneKey))
        {
            String str=Paper.book().read("Username").toString();
           if(Paper.book().read("User").toString().equals("Admins"))
           {
               Intent intent=new Intent(MainActivity.this,AdminCategory.class);
               intent.putExtra("Username",str);
               startActivity(intent);
           }
           else
           {
               Intent intent=new Intent(MainActivity.this,Home.class);
               intent.putExtra("Username",str);
               startActivity(intent);
           }
        }
    }
}