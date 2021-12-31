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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class signupActivity extends AppCompatActivity {

    Button signUp;
    EditText name,phone,password,confirmPassword;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp=findViewById(R.id.signUp);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.reg_phoneNumber);
        password=findViewById(R.id.reg_password);
        confirmPassword=findViewById(R.id.reg_confirm_password);

        loadingBar=new ProgressDialog(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String n=name.getText().toString();
        String ph=phone.getText().toString();
        String pwd=password.getText().toString();
        String confirmPwd=confirmPassword.getText().toString();

        if(TextUtils.isEmpty(n))
        {
            Toast.makeText(signupActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ph))
        {
            Toast.makeText(signupActivity.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pwd))
        {
            Toast.makeText(signupActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(!pwd.equals(confirmPwd))
        {
            Toast.makeText(signupActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
        }
        else if (pwd.length()<5)
        {
            Toast.makeText(signupActivity.this, "Password Length Must be Atleast 5", Toast.LENGTH_SHORT).show();
        }
        else if(ph.length()!=11)
        {
            Toast.makeText(signupActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while creating account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            validatePhoneNumber(n,ph,pwd,confirmPwd);
        }


    }

    private void validatePhoneNumber(String n, String ph, String pwd, String confirmPwd) {
         final DatabaseReference root;
         root= FirebaseDatabase.getInstance().getReference();
         root.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(!(snapshot.child("Users").child(ph).exists()))
                 {
                     HashMap<String,Object>userDataMap=new HashMap<>();
                     userDataMap.put("name",n);
                     userDataMap.put("phone",ph);
                     userDataMap.put("password",pwd);

                     root.child("Users").child(ph).updateChildren(userDataMap)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(signupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent=new Intent(signupActivity.this,loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(signupActivity.this, "Network Error! Please Try Again Later", Toast.LENGTH_SHORT).show();
                                    }
                                 }
                             });
                 }
                 else
                 {
                     Toast.makeText(signupActivity.this, ph+" Already Exists", Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();
                     Toast.makeText(signupActivity.this, "Try Using Another Phone Number", Toast.LENGTH_SHORT).show();
                     Intent intent=new Intent(signupActivity.this,MainActivity.class);
                     startActivity(intent);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}