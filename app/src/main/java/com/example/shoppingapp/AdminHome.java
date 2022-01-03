package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminHome extends AppCompatActivity {

    String category,nameText,descriptionText,priceText,date,time,productKey,downloadImageUrl,user;
    Button add;
    EditText name,price,description;
    ImageView productImage;
    Uri imageUri;
    StorageReference imageRef;
    DatabaseReference productsRef;
    ProgressDialog loadingBar;
    public static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        category=getIntent().getExtras().get("category").toString();

        add=findViewById(R.id.addProduct_Button);
        name=findViewById(R.id.product_name);
        description=findViewById(R.id.product_description);
        price=findViewById(R.id.product_price);

        productImage=findViewById(R.id.product_image);
        loadingBar=new ProgressDialog(this);

        imageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
            nameText=name.getText().toString();
            descriptionText=description.getText().toString();
            priceText=price.getText().toString();
            
            if(imageUri==null)
            {
                Toast.makeText(AdminHome.this, "Add Image", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(nameText))
            {
                Toast.makeText(AdminHome.this, "Product Name Required", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(descriptionText))
            {
                Toast.makeText(AdminHome.this, "Product Description Required", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(priceText))
            {
                Toast.makeText(AdminHome.this, "Product Price Required", Toast.LENGTH_SHORT).show();
            }
            else
            {
                storeProduct();
            }
    }

    private void storeProduct() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait while we adding product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate=new SimpleDateFormat("MM dd, yyyy");
        date=simpleDate.format(calendar.getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleTime=new SimpleDateFormat("HH:mm:ss a");
        time=simpleTime.format(calendar.getTime());
        productKey=date+time;
        StorageReference path=imageRef.child(imageUri.getLastPathSegment()+productKey+".jpg");
        final UploadTask uploadTask=path.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.getMessage();
                Toast.makeText(AdminHome.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminHome.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful())
                       {
                           throw task.getException();

                       }
                       downloadImageUrl=path.getDownloadUrl().toString();
                       return path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                downloadImageUrl=task.getResult().toString();
                                Toast.makeText(AdminHome.this, "Image URL get Successfull", Toast.LENGTH_SHORT).show();
                                saveProductInfo();

                            }
                    }
                });
            }
        });


    }

    private void saveProductInfo() {

        HashMap<String,Object> map=new HashMap<>();
        map.put("ProductID",productKey);
        map.put("Date",date);
        map.put("Time",time);
        map.put("Description",descriptionText);
        map.put("Image",downloadImageUrl);
        map.put("Category",category);
        map.put("Price",priceText);
        map.put("Name",nameText);

        productsRef.child(productKey).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AdminHome.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(AdminHome.this,AdminCategory.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(AdminHome.this,"Error: "+task.getException().toString(),Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            productImage.setImageURI(imageUri);
        }
    }
}