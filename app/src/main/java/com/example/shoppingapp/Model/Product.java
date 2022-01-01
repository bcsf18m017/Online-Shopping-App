package com.example.shoppingapp.Model;

public class Product {
    private String productID,Category,Name,Description,Image,Date,Price,Time;


    public Product()
    {}

    public Product(String pid,String category, String name, String description, String image, String date, String price, String time) {
        productID=pid;
        Category = category;
        Name = name;
        Description = description;
        Image = image;
        Date = date;
        Price = price;
        Time = time;
    }

    public String getPid() {
        return productID;
    }

    public void setPid(String pid) {
        this.productID = pid;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
