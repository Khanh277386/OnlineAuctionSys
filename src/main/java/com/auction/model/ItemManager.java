package com.auction.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemManager {

    private ConcurrentHashMap<String, Item> items;
    
    private static ItemManager instance;

    private ItemManager() {
        items = new ConcurrentHashMap<>();
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public void addItem(Seller seller, ItemType type, String name, String description, Map<String, String> attributes) {
        Item newItem = ItemFactory.createItem(type, name, description, seller, attributes);
        if (newItem != null) {
            items.put(newItem.getId(), newItem); 
            System.out.println("Đã thêm vật phẩm vào kho: " + newItem.getName());
        }
    }

    public List<Item> getItemsBySeller(String sellerId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {

            if (item != null && sellerId.equals("N/A")) { 
                result.add(item);
            }
        }
        return result;
    }

    public void updateItem(String itemId, String requester) {
    }

    public void deleteItem(String itemId, String requester) {
    }
}