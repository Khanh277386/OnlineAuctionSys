package com.auction.factory;

import com.auction.model.Art;
import com.auction.model.Electronic;
import com.auction.model.Item;
import com.auction.model.ItemType;
import com.auction.model.Seller;
import com.auction.model.Vehicle;
import java.util.Map;
import java.util.UUID;

public final class ItemFactory {

  public static Item createItem(
      ItemType type,
      String name,
      String description,
      Seller seller,
      Map<String, String> attributes) {
    if (type == null || name == null || name.isBlank() || seller == null) {
      throw new IllegalArgumentException("Thiếu loại, tên hoặc người bán của sản phẩm");
    }

    String id = UUID.randomUUID().toString();
    try {
        return switch (type) {
            case ELECTRONIC -> new Electronic(
                    id,
                    name,
                    description,
                    seller,
                    Double.parseDouble(require(attributes, "durability")),
                    Integer.parseInt(require(attributes, "warranty")));
            case ART -> new Art(
                    id,
                    name,
                    description,
                    seller,
                    require(attributes, "style"),
                    Integer.parseInt(require(attributes, "age")));
            case VEHICLE -> new Vehicle(
                    id,
                    name,
                    description,
                    seller,
                    require(attributes, "model"),
                    require(attributes, "brand"),
                    Double.parseDouble(require(attributes, "mileage")));
            default -> throw new IllegalArgumentException("Loại item không hợp lệ: " + type);
        };
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Thuộc tính số không hợp lệ: " + e.getMessage());
    }
  }

  private static String require(Map<String, String> attributes, String key) {
    if (attributes == null || !attributes.containsKey(key)) {
      throw new IllegalArgumentException("Thiếu thuộc tính: " + key);
    }
    return attributes.get(key);
  }
}
