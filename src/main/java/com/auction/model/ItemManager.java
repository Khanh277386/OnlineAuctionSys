package com.auction.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemManager {
   private static final ItemManager INSTANCE = new ItemManager();

    private final ConcurrentHashMap<String, Item> items = new ConcurrentHashMap<>();

    private ItemManager() {
    }

    public static ItemManager getInstance() {
        return INSTANCE;
    }


    public Item addItem(Seller seller, ItemType type, String name, String description,
                        Map<String, String> attributes) {
        Item item = ItemFactory.createItem(type, name, description, seller, attributes);
        items.put(item.getId(), item);
        return item;
    }


    public void updateItem(String itemId, String newName, String newDescription, User requester)
            throws AuthenticationException {
        Item item = getItem(itemId);
        checkPermission(item, requester);
        item.update(newName, newDescription);
    }
    public void deleteItem(String itemId, User requester) throws AuthenticationException {
        Item item = getItem(itemId);
        checkPermission(item, requester);
        items.remove(itemId);
    }

    public Item getItem(String itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NoSuchElementException("Không tìm thấy sản phẩm: " + itemId);
        }
        return item;
    }
    public List<Item> getItemsBySeller(String sellerId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getSeller().getId().equals(sellerId)) {
                result.add(item);
            }
        }
        return result;
    }
    private void checkPermission(Item item, User requester) throws AuthenticationException {
        boolean isOwner = requester != null
                && item.getSeller().getId().equals(requester.getId());
        boolean isAdmin = requester instanceof Admin;
        if (!isOwner && !isAdmin) {
            throw new AuthenticationException("Bạn không có quyền với sản phẩm này");
        }
    }
}
    
}
