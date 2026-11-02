
package com.auction.model;

public class Vehicle extends Item{
    private final String model;
    private final  String brand;
    private final double mileage;
    public Vehicle(String Id, String title, String description, Seller seller, String model, String brand, double mileage) {
    super(Id, title, description, seller);
    this.model = model;
    this.brand = brand;
    this.mileage = mileage;
    }
    public String getModel(){
        return model;
    }

    public String getBrand() {
        return brand;
    }
    
    public double getMileage() {
        return mileage;
    }
    @Override
    public void printInfo() {
        System.out.println("[Phương tiện] " + getName() + " - " + getDescription()
                + " | " + brand + " " + model + " | Đã đi: " + mileage + " km");
    }
}
