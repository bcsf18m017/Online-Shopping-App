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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Admin.AdminCategory;
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
    private TextView admin,user,forgetPassword;
    String parentDBName="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.login);
        phone=findViewById(R.id.phoneNumber);
        password=findViewById(R.id.password);
        admin=findViewById(R.id.adminLink);
        user=findViewById(R.id.clientLink);
        forgetPassword=findViewById(R.id.forgetPassword);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this,ResetPassword.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });

        loadingBar=new ProgressDialog(this);

        checkBox=findViewById(R.id.rememberMeCheckBox);
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                parentDBName="Admins";
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setVisibility(View.VISIBLE);
                user.setVisibility(View.INVISIBLE);
                parentDBName="Users";
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
                if(snapshot.child(parentDBName).child(ph).exists())
                {
                    Users userData=snapshot.child(parentDBName).child(ph).getValue(Users.class);
                    if(userData.getPhone().equals(ph) && userData.getPassword().equals(pwd))
                    {
                        Toast.makeText(loginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Prevalent.currentUser=userData;
                        if(checkBox.isChecked())
                        {
                            Paper.book().write("User",parentDBName);
                            Paper.book().write("Username",userData.getName());
                            Paper.book().write("Phone",userData.getPhone());
                            Paper.book().write(Prevalent.userPhoneKey,ph);
                            Paper.book().write(Prevalent.userPasswordKey,pwd);
                            Paper.book().write("Image",userData.getImage()==null?"":userData.getImage());
                            Paper.book().write("Address", userData.getAddress()==null?"":userData.getAddress());
                        }
                        if(parentDBName.equals("Users"))
                        {
                            Intent intent =new Intent(loginActivity.this,Home.class);
                            intent.putExtra("Username",userData.getName());
                            intent.putExtra("phone",userData.getPhone());
                            intent.putExtra("image",userData.getImage()==null?"":userData.getImage());
                            intent.putExtra("address",userData.getAddress());
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent =new Intent(loginActivity.this, AdminCategory.class);
                            startActivity(intent);
                        }
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