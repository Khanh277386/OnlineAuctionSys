package com.auction.model;

public class Electronic extends Item {
  private final double durability;
  private final int warranty;
  private static final long serialVersionUID = 1L;
  public Electronic(
      String id, String name, String description, Seller seller, double durability, int warranty) {
    super(id, name, description, seller);
    this.durability = durability;
    this.warranty = warranty;
  }

  public int getWarrantyInfo() {
    return warranty;
  }

  public double getDurabilityInfo() {
    return durability;
  }

  @Override
  public void printInfo() {
    System.out.println(
        "[Điện tử] "
            + getName()
            + " - "
            + getDescription()
            + " | Bảo hành: "
            + warranty
            + " tháng | Độ bền: "
            + durability
            + "%");
  }
}
