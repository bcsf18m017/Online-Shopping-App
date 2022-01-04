package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText name,phone,address;
    private TextView change,close,update;
    private  String URL,checker="";
    private StorageReference picRef;
    private Uri uri;
    private StorageTask uploadTask;
    private String receivedPhone,receivedName,receivedAddress,receivedImage;
    private Button securityQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        profileImageView=findViewById(R.id.setting_image);
        name=findViewById(R.id.setting_name);
        phone=findViewById(R.id.setting_phone);
        address=findViewById(R.id.setting_address);
        change=findViewById(R.id.setting_image_change);
        close=findViewById(R.id.setting_close);
        update=findViewById(R.id.setting_update);
        phone.setEnabled(false);
        securityQuestion=findViewById(R.id.security_questions);

        receivedName=getIntent().getExtras().get("Username").toString();
        receivedPhone=getIntent().getExtras().get("phone").toString();
        receivedImage=getIntent().getExtras().get("image").toString();
        receivedAddress=getIntent().getExtras().get("address").toString();

        securityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.this,ResetPassword.class);
                intent.putExtra("check","settings");
                intent.putExtra("phone",receivedPhone);
                intent.putExtra("username",receivedName);
                intent.putExtra("image",receivedImage);
                intent.putExtra("address",receivedAddress);
                startActivity(intent);
            }
        });



        picRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        userInfoDisplay(profileImageView,name,phone,address);

        if(!receivedImage.equals("")) {
            Picasso.get().load(receivedImage).placeholder(R.drawable.profile).into(profileImageView);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(uri).setAspectRatio(1,1).start(Settings.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            uri=result.getUri();
            profileImageView.setImageURI(uri);
        }
        else
        {
            Toast.makeText(Settings.this, "Error..!! Try Again", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Settings.this,Settings.class);
            intent.putExtra("Username",receivedName);
            intent.putExtra("phone",receivedPhone);
            intent.putExtra("image",receivedImage);
            intent.putExtra("address",receivedAddress);
            startActivity(intent);
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        String textName=receivedName,textAddress=receivedAddress;
        if(!TextUtils.isEmpty(name.getText().toString()))
        {
            textName=name.getText().toString();
        }
        if(!TextUtils.isEmpty(address.getText().toString()))
        {
            textAddress=address.getText().toString();
        }
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object>map=new HashMap<>();
        map.put("name",textName);
        map.put("address",textAddress);
        ref.child(receivedPhone).updateChildren(map);
        Intent intent=new Intent(Settings.this,Home.class);
        intent.putExtra("Username",textName);
        intent.putExtra("phone",receivedPhone);
        intent.putExtra("image",receivedImage);
        intent.putExtra("address",textAddress);
        startActivity(intent);
        Toast.makeText(Settings.this,"Updated Sucessfully",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(Settings.this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getText().toString()))
        {
            Toast.makeText(Settings.this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        ProgressDialog loadingBar=new ProgressDialog(this);
        loadingBar.setTitle("Update Profile");
        loadingBar.setMessage("Please Wait while we are updating your account");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        if(uri!=null)
        {
            final StorageReference fileRef=picRef.child(receivedPhone+".jpg");
            uploadTask=fileRef.putFile(uri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        URL=downloadUrl.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object>map=new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("address",address.getText().toString());
                        map.put("image",URL);
                        ref.child(receivedPhone).updateChildren(map);
                        loadingBar.dismiss();
                        Intent intent=new Intent(Settings.this,Home.class);
                        intent.putExtra("Username",name.getText().toString());
                        intent.putExtra("phone",phone.getText().toString());
                        intent.putExtra("image",URL);
                        intent.putExtra("address",address.getText().toString());
                        startActivity(intent);
                        Toast.makeText(Settings.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(Settings.this,"Error in Updating",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Settings.this,"Image is Not Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText name, EditText phone, EditText address) {

        name.setText(receivedName);
        phone.setText(receivedPhone);
        address.setText(receivedAddress);

//        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(receivedPhone);
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    if(snapshot.child("image").exists())
//                    {
//                        String image=snapshot.child("image").getValue().toString();
//                        String nameText=snapshot.child("name").getValue().toString();
//                        String phoneText=snapshot.child("phone").getValue().toString();
//                        String addressText=snapshot.child("address").getValue().toString();
//
//                        Picasso.get().load(image).into(profileImageView);
//                        name.setText(nameText);
//                        address.setText(addressText);
//                        phone.setText(phoneText);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}