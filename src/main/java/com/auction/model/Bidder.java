package com.auction.model;

import com.auction.observer.Observer;

public class Bidder extends User implements Observer{
    private double balance;
    public Bidder(String id, String name, String password){
        super(id, name, password);
        this.balance = 0;
    }

    public synchronized boolean addFund(double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
        }
        balance += amount;
        return true;
    }

    public synchronized boolean deductFund(double amount){
        if (amount <= 0 || amount > balance){
            return false;
        }
        balance -= amount;
        return true;
    }

    public synchronized double getBalance(){
        return balance;
    }

    public String getRole(){
        return ROLE_BIDDER;
    }

    public void printInfo() {
        System.out.println("[Bidder] " + getName() + " | Số dư: " + String.format("%,.0f", getBalance()));
    }

    public void update(Bid bid) {
        System.out.println("  [Thông báo tới " + getName() + "] " + bid);
    }
}