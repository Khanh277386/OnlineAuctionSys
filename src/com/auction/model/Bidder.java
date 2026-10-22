package com.auction.model;

public class Bidder extends User {
    private double maxAutoBid;

    public Bidder(String id, String username, String password, double maxAutoBid) {
        super(id, username, password, "BIDDER");
        this.maxAutoBid = maxAutoBid;
    }

    public double getMaxAutoBid() {
        return maxAutoBid;
    }

    public void setMaxAutoBid(double maxAutoBid) {
        this.maxAutoBid = maxAutoBid;
    }

    @Override
    public void printInfo() {
        System.out.println("Bidder [ID: " + getId() + ", Username: " + getUsername()
                + ", Role: " + getRole() + ", MaxAutoBid: " + maxAutoBid + "]");
    }
}