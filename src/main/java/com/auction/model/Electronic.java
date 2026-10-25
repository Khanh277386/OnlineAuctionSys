package com.auction.model;

public class Electronic extends Item{
    private double durability;
    private int warranty;
    
    public Electronic(String Id, String name, String description,  String sellerId,Seller seller, double durability, int warranty) {
    super(Id, name, description, sellerId , seller); // Gọi hàm khởi tạo của Item
    this.durability = durability;
    this.warranty = warranty;
}
    
    
    public int getWarrantyInfo(){
        return warranty;
    }

   
    public double getDurabilityInfo() {
        return durability;
    }
}