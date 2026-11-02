
package com.auction.model;

public class Vehicle extends Item{
    private String model;
    private String brand;
    private double metre;
    public Vehicle(String Id, String title, String description, Seller seller, String model, String brand, double metre) {
    super(Id, title, description, seller); // Gọi hàm khởi tạo của Item
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
    }@Override
    public void printInfo() {
    System.out.println("Item: " + getName() + " | Type: Vehicle" + 
                                    " Model: " + getModel() 
                                + " Brand: "+ getBrand()
                                +  " Stat: " + getStat());
}
}