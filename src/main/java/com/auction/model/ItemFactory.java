package com.auction.model;

public class ItemFactory {
    public Item createItem(String type, String Id, String name, double price){
        if (type == null){
            return null;
        }
// Dùng switch-case thay vì if-else:
// 1. Giúp code gọn gàng, dễ mở rộng khi thêm loại Item mới (Factory Pattern)
// 2. Tối ưu hiệu năng tìm kiếm các nhánh
        switch (type.toLowerCase()){
            case "electronics":
                return new Electronic(Id,name,"N/A", "Unknown", null, 0.0, 0) ;

            case "art":
                return new Art(Id, name, "N/A", null, "Unknown","Unknown", 0);

            case "vehicle" :
                return new Vehicle(Id, name, "N/A", null, "Unknown","N/A", "N/A", 0.0);          
            
            default:
                throw new IllegalArgumentException("Loai item khong hop le: " + type);
        }
    }
}
