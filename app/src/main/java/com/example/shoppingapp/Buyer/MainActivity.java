package com.example.shoppingapp.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.Admin.AdminCategory;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.R;

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
            String str1=Paper.book().read("Phone").toString();
            String str2=Paper.book().read("Image").toString();
            String str3=Paper.book().read("Address").toString();
           if(Paper.book().read("User").toString().equals("Admins"))
           {
               Intent intent=new Intent(MainActivity.this, AdminCategory.class);
               intent.putExtra("Username",str);
               intent.putExtra("phone",str1);
               startActivity(intent);
           }
           else
           {
               Intent intent=new Intent(MainActivity.this,Home.class);
               intent.putExtra("Username",str);
               intent.putExtra("phone",str1);
               intent.putExtra("image",str2);
               intent.putExtra("address",str3);
               startActivity(intent);
           }
        }
    }
}