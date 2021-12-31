package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategory extends AppCompatActivity {

    private ImageView tShirts,sports,femaleDresses,sweaters
            ,glasses,hats,purses,shoes
            ,laptops,headphones,mobiles,watches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tShirts=findViewById(R.id.tshirt);
        sports=findViewById(R.id.sports);
        femaleDresses=findViewById(R.id.femaleDress);
        sweaters=findViewById(R.id.sweater);
        glasses=findViewById(R.id.glasses);
        hats=findViewById(R.id.hats);
        purses=findViewById(R.id.purses);
        shoes=findViewById(R.id.shoes);
        laptops=findViewById(R.id.laptops);
        headphones=findViewById(R.id.headphones);
        mobiles=findViewById(R.id.mobiles);
        watches=findViewById(R.id.wathces);

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","SportsTShirts");
                startActivity(intent);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Hats Caps");
                startActivity(intent);
            }
        });
        purses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Wallets Bags Purses");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Headphones Handfree");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategory.this,AdminHome.class);
                intent.putExtra("category","Mobile Phones");
                startActivity(intent);
            }
        });




    }
}