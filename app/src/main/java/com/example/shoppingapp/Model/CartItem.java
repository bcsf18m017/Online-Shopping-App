package com.example.shoppingapp.Model;

public class CartItem {

    private String ProductID,Name,Description,Discount,Price,Quantity,Date,Time,Image;

    public CartItem()
    {}
    public CartItem(String productID, String name, String description, String discount, String price, String quantity, String date, String time,String image) {
        ProductID = productID;
        Name = name;
        Description = description;
        Discount = discount;
        Price = price;
        Quantity = quantity;
        Date = date;
        Time = time;
        Image=image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
