package com.auction.model;

public class Electronics extends Item{
    private String brand;
    private int warrantyMonths;
    
    public Electronics(String Id, String title, String description, double StartingPrice, String sellerId, String brand, int warrantyMonths) {
    super(Id, title, description, StartingPrice, sellerId); // Gọi hàm khởi tạo của Item
    this.brand = brand;
    this.warrantyMonths = warrantyMonths;
}
    
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