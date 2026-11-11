package com.auction.model;

public abstract class User implements Entity {
  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_BIDDER = "BIDDER";
  public static final String ROLE_SELLER = "SELLER";

  private static final long serialVersionUID = 1L;
  private final String id;
  private final String name;
  private final String password;

  protected User(String id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  public String getPassword(){return password;}
  public boolean checkPassword(String input) {
    return password.equals(input);
  }

  public abstract String getRole();

  public abstract void printInfo();
}
