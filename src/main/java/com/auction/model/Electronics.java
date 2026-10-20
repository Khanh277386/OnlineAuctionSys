package com.auction.model;

public class Electronics extends Item{
    private String brand;
    private int warrantyMonths;

    @Override
    public String getCategory(){
        return "Đồ điện tử";
    }

    @Override
    public void printDetails() {
    System.out.println("ID: " + getId() + 
                       " | Danh mục: " + getCategory() + 
                       " | Thương hiệu: " + brand + 
                       " | Bảo hành: " + warrantyMonths + " tháng" +
                       " | Giá khởi điểm: $" + getStartingPrice());
    }
}