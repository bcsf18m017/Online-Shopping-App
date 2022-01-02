package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interfaces.ItemClickListener;
import com.example.shoppingapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView itemPrice,itemQuantity,itemName;
    private ItemClickListener listener;
    public ImageView itemImage;

    public CartViewHolder(View itemView) {
        super(itemView);

        itemName=itemView.findViewById(R.id.name_cart_item);
        itemQuantity=itemView.findViewById(R.id.quantity_cart_item);
        itemPrice=itemView.findViewById(R.id.price_cart_item);
        itemImage=itemView.findViewById(R.id.image_cart_item);

    }

    @Override
    public void onClick(View v) {

        listener.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }

}
