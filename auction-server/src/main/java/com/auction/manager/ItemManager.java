package com.auction.manager;

import com.auction.exception.AuthenticationException;
import com.auction.factory.ItemFactory;
import com.auction.model.Admin;
import com.auction.model.Item;
import com.auction.model.ItemType;
import com.auction.model.Seller;
import com.auction.model.User;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import com.auction.utils.DataStorage;

public final class ItemManager {

  private static final String DEFAULT_STORAGE_PATH = "items_data.dat";

  private static final ItemManager INSTANCE = new ItemManager();

  private final ConcurrentHashMap<String, Item> items = new ConcurrentHashMap<>();

  private ItemManager() {loadFromFile(DEFAULT_STORAGE_PATH);}

  public static ItemManager getInstance() {
    return INSTANCE;
  }
  public void saveToFile(String filePath) {
    DataStorage.save(filePath, this.items);
  }
  public Item addItem(
      Seller seller,
      ItemType type,
      String name,
      String description,
      Map<String, String> attributes) {
    Item item = ItemFactory.createItem(type, name, description, seller, attributes);
    items.put(item.getId(), item);
    saveToFile(DEFAULT_STORAGE_PATH);
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
  public ConcurrentHashMap<String, Item> getAllItems() {
    return items;
  }
  private void checkPermission(Item item, User requester) throws AuthenticationException {
    boolean isOwner = requester != null && item.getSeller().getId().equals(requester.getId());
    boolean isAdmin = requester instanceof Admin;
    if (!isOwner && !isAdmin) {
      throw new AuthenticationException("Bạn không có quyền với sản phẩm này");
    }
  }
  public void loadFromFile(String filePath) {
    ConcurrentHashMap<String, Item> loaded = DataStorage.load(filePath);
    if (loaded != null) {
      this.items.clear();
      this.items.putAll(loaded);
      System.out.println("[ItemManager] Đã khôi phục " + this.items.size() + " sản phẩm từ " + filePath);
    }
  }

  private static final class DataStorage {
    private static void save(String filePath, ConcurrentHashMap<String, Item> items) {
      try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath))) {
        output.writeObject(items);
      } catch (IOException e) {
        throw new IllegalStateException("Không thể lưu dữ liệu sản phẩm", e);
      }
    }

    @SuppressWarnings("unchecked")
    private static ConcurrentHashMap<String, Item> load(String filePath) {
      try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
        return (ConcurrentHashMap<String, Item>) input.readObject();
      } catch (IOException | ClassNotFoundException e) {
        return null;
      }
    }
  }
}
