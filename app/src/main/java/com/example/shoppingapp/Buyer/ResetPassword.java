package com.example.shoppingapp.Buyer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPassword extends AppCompatActivity {

    private String check="";
    private TextView reset,questionTitle;
    private EditText phone,question1,question2;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");

        reset=findViewById(R.id.reset_page_title);
        questionTitle=findViewById(R.id.textView);
        phone=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        verify=findViewById(R.id.verify_answers);



    }

    @Override
    protected void onStart() {
        super.onStart();
        phone.setVisibility(View.GONE);
        if(check.equals("login"))
        {
            phone.setVisibility(View.VISIBLE);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(phone.getText().toString().equals("")||question1.getText().toString().equals("")||question2.getText().toString().equals(""))
                    {
                        Toast.makeText(ResetPassword.this,"Please Fill All Fields",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        verifyUser();
                    }
                }
            });
        }
        else
        {
            settingsCall();
        }
    }

    private void verifyUser() {

        String ph=phone.getText().toString();
       final  DatabaseReference ref= (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Users");
       ref.child(ph).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String ans1=snapshot.child("Security Questions").child("Answer1").getValue().toString();
                            String ans2=snapshot.child("Security Questions").child("Answer2").getValue().toString();
                            String txt1=question1.getText().toString();
                            String txt2=question2.getText().toString();
                            if(ans1.equals(txt1)&&ans2.equals(txt2))
                            {
                                AlertDialog builder=new AlertDialog.Builder(ResetPassword.this).create();
                                builder.setTitle("Enter New Password");
                                final EditText password=new EditText(ResetPassword.this);
                                password.setHint("Write New Password Here..");
                                builder.setView(password);

                                builder.setButton(Dialog.BUTTON_POSITIVE,"Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!password.getText().toString().equals(""))
                                        {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(ph).child("password").setValue(password.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                builder.dismiss();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.dismiss();
                                    }
                                });
                                builder.show();
                            }
                            else
                            {
                                Toast.makeText(ResetPassword.this,"Answers Do Not Match",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ResetPassword.this,"User With This Phone Does Not Exist",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void  settingsCall()
    {
        String receivedName,receivedPhone,receivedImage,receivedAddress;
        receivedName=getIntent().getExtras().get("username").toString();
        receivedPhone=getIntent().getExtras().get("phone").toString();
        receivedImage=getIntent().getExtras().get("image").toString();
        receivedAddress=getIntent().getExtras().get("address").toString();
        reset.setText("Set Questions");
        questionTitle.setText("Please GIve Answers of Security Questions");
        verify.setText("Set");
        displayPreviousAnswers(receivedPhone);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer1=question1.getText().toString().toLowerCase();
                String answer2=question2.getText().toString().toLowerCase();
                if(answer1.equals("")||answer2.equals(""))
                {
                    Toast.makeText(ResetPassword.this, "Please Answer First", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(receivedPhone);
                    HashMap<String,Object>map=new HashMap<>();
                    map.put("Answer1",answer1);
                    map.put("Answer2",answer2);
                    ref.child("Security Questions").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPassword.this, "Answers Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ResetPassword.this,Home.class);
                                intent.putExtra("phone",receivedPhone);
                                intent.putExtra("Username",receivedName);
                                intent.putExtra("image",receivedImage);
                                intent.putExtra("address",receivedAddress);
                                startActivity(intent);
                            }

                        }
                    });

                }

            }
        });

    }
    private void displayPreviousAnswers(String receivedPhone)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(receivedPhone);
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String ans1=snapshot.child("Answer1").getValue().toString();
                    String ans2=snapshot.child("Answer2").getValue().toString();
                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}