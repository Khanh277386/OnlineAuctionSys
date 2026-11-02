package com.auction.model;


public class Seller extends User {

    private double balance;

    public Seller(String id, String name, String password) {
        super(id, name, password);
        this.balance = 0;
    }

    public synchronized double revenue() {
        return balance;
    }


    public synchronized void addRevenue(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Doanh thu cộng thêm phải lớn hơn 0");
        }
        balance += amount;
    }

    @Override
    public String getRole() {
        return ROLE_SELLER;
    }

    @Override
    public void printInfo() {
        System.out.println("[Seller] " + getName() + " | Doanh thu: " + String.format("%,.0f", revenue()));
    }
}
