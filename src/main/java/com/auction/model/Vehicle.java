package com.auction.model;

public class Vehicle extends Item{
    private String model;
    private String brand;
    private double metre;
    public Vehicle(String Id, String title, String description, Seller seller, String sellerId, String model, String brand, double metre) {
    super(Id, title, description, sellerId, seller); // Gọi hàm khởi tạo của Item
    this.model = model;
    this.brand = brand;
    this.metre = metre;
}
    public String getModel(){
        return model;
    }

    public String getBrand() {
        return brand;
    }
    
    public double getStat(){
        return metre;
    }
}
