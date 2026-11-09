package com.auction.model;

public class Art extends Item {

  private final String style;
  private final int age;

  public Art(String Id, String name, String description, Seller seller, String style, int age) {
    super(Id, name, description, seller);
    this.style = style;
    this.age = age;
  }

  public String getStyle() {
    return style;
  }

  public int getAge() {
    return age;
  }

  @Override
  public void printInfo() {
    System.out.println(
        "[Nghệ thuật] "
            + getName()
            + " - "
            + getDescription()
            + " | Phong cách: "
            + style
            + " | Tuổi: "
            + age
            + " năm");
  }
}
