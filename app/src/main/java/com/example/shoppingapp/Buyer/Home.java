package com.example.shoppingapp.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.Model.Product;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String user,phoneReceived,addressReceived,imageReceived;
    private  DatabaseReference productsRef;
    CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user=getIntent().getExtras().get("Username").toString();
        phoneReceived=getIntent().getExtras().get("phone").toString();
        imageReceived=getIntent().getExtras().get("image").toString();
        addressReceived=getIntent().getExtras().get("address").toString();

        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        Paper.init(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(Home.this,Cart.class);
                intent.putExtra("phone",phoneReceived);
                intent.putExtra("image",imageReceived);
                intent.putExtra("address",addressReceived);
                intent.putExtra("username",user);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView=navigationView.getHeaderView(0);
        TextView userName=headerView.findViewById(R.id.user_name);
        CircleImageView userImage=headerView.findViewById(R.id.user_image);
        userName.setText(user);
        if(!imageReceived.equals(""))
        {
            Picasso.get().load(imageReceived).placeholder(R.drawable.profile).into(userImage);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_cart)
        {
            Intent intent=new Intent(Home.this,Cart.class);
            intent.putExtra("phone",phoneReceived);
            intent.putExtra("image",imageReceived);
            intent.putExtra("address",addressReceived);
            intent.putExtra("username",user);
            startActivity(intent);
        }
        else if(id==R.id.nav_search)
        {
            Intent intent=new Intent(Home.this,SearchProduct.class);
            intent.putExtra("phone",phoneReceived);
            intent.putExtra("image",imageReceived);
            intent.putExtra("address",addressReceived);
            intent.putExtra("username",user);
            startActivity(intent);
        }

        else if(id==R.id.nav_settings)
        {
            Intent intent=new Intent(Home.this,Settings.class);
            intent.putExtra("Username",user);
            intent.putExtra("phone",phoneReceived);
            intent.putExtra("image",imageReceived);
            intent.putExtra("address",addressReceived);
            startActivity(intent);
        }

        else if(id==R.id.nav_categories)
        {

        }

        else if(id==R.id.nav_logout)
        {
            Toast.makeText(Home.this, "Hello", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent=new Intent(Home.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product>options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productsRef,Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder>adapter=
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.name.setText(model.getName());
                holder.price.setText(model.getPrice()+"$");
                Picasso.get().load(model.getImage()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Home.this,ProductDetails.class);
                        intent.putExtra("pid",model.getDate()+model.getTime());
                        intent.putExtra("phone",phoneReceived);
                        intent.putExtra("image",model.getImage());
                        intent.putExtra("address",addressReceived);
                        intent.putExtra("username",user);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}