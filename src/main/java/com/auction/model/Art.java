package com.auction.model;

public class Art extends Item {
    
    private String artist;
    private int yearCreated;

    @Override
    public String getCategory(){
        return "Đồ nghệ thuật";
    }

    @Override
    public void printDetails() {
    System.out.println("ID: " + getId() + 
                       " | Danh mục: " + getCategory() + 
                       " | Tác giá: "+ artist +
                       " | Năm sáng tác" + yearCreated +
                       " | Giá khởi điểm: $" + getStartingPrice());
    }
}
