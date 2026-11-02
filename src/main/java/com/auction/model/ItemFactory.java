package com.auction.model;

import java.util.Map;
import java.util.UUID;

public class ItemFactory {

    public static Item createItem(ItemType type, String name, String description, Seller seller, Map<String, String> attributes) {
        if (type == null) {
            return null;
        }


        String id = UUID.randomUUID().toString();
// Dùng switch-case thay vì if-else:
// 1. Giúp code gọn gàng, dễ mở rộng khi thêm loại Item mới (Factory Pattern)
// 2. Tối ưu hiệu năng tìm kiếm các nhánh
        switch (type) {
            case ELECTRONIC:
                double durability = Double.parseDouble(attributes.getOrDefault("durability", "100.0"));
                int warranty = Integer.parseInt(attributes.getOrDefault("warranty", "12"));
                return new Electronic(id, name, description, seller, durability, warranty);

            case ART:
                String style = attributes.getOrDefault("style", "Unknown");
                int age = Integer.parseInt(attributes.getOrDefault("age", "0"));
                return new Art(id, name, description, seller, style, age);

            case VEHICLE:
                String model = attributes.getOrDefault("model", "Unknown");
                String brand = attributes.getOrDefault("brand", "Unknown");
                double metre = Double.parseDouble(attributes.getOrDefault("metre", "0.0"));
                return new Vehicle(id, name, description, seller, model, brand, metre);

            default:
                throw new IllegalArgumentException("Loại item không hợp lệ: " + type);
        }
    }
}