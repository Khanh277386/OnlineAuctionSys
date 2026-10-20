package com.auction.model;

public class Vehicle extends Item{
    private int modelYear;
    private double mileage;
    
    @Override
    public String getCategory(){
        return "Phương tiện di chuyển";
    }

    @Override
    public void printDetails() {
    System.out.println("ID: " + getId() + 
                       " | Danh mục: " + getCategory() + 
                       " | Mẫu năm: " + modelYear +
                       " | Số mile đã đi: " + mileage + 
                       " | Giá khởi điểm: $" + getStartingPrice());
    }
    
}
