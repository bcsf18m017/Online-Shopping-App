package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

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

        }
        else
        {

            reset.setText("Set Questions");
            questionTitle.setText("Please GIve Answers of Security Questions");
            verify.setText("Set");
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
                        String p=getIntent().getStringExtra("phone");
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(p);
                        HashMap<String,Object>map=new HashMap<>();
                        map.put("Answer1",answer1);
                        map.put("Answer2",answer2);
                        ref.child("Security Questions").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ResetPassword.this, "Answers Updated Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }

                }
            });
        }
    }
}