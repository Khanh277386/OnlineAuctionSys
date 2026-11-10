package com.auction.model;

public class Admin extends User {
  public Admin(String id, String name, String password) {
    super(id, name, password);
  }

  public String getRole() {
    return ROLE_ADMIN;
  }

  public void printInfo() {
    System.out.println("[Admin] " + getName());
  }
}
