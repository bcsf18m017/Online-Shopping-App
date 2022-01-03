package com.example.shoppingapp.Model;

public class Orders {
    private String Address,City,Date,Name,Time,Phone,State,TotalAmount;
    public Orders()
    {}

    public Orders(String address, String city, String date, String name, String time, String phone, String state, String totalAmount) {
        Address = address;
        City = city;
        Date = date;
        Name = name;
        Time = time;
        Phone = phone;
        State = state;
        TotalAmount = totalAmount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
