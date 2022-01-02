package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interfaces.ItemClickListener;
import com.example.shoppingapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name,description,price;
    public ImageView image;
    public ItemClickListener itemListener;

    public  ProductViewHolder(View itemView)
    {
        super(itemView);

        name=itemView.findViewById(R.id.productName);
        price=itemView.findViewById(R.id.productPrice);
        image=itemView.findViewById(R.id.productImage);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemListener=listener;
    }


    @Override
    public void onClick(View v) {
         itemListener.onClick(v,getAdapterPosition(),false);
    }
}
