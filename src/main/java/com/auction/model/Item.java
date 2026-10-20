package com.auction.model;

public abstract class Item implements Entity{
    private String id;
    private String title;
    private String description;
    private double startingPrice;
    private String sellerId;

    public abstract String getCategory();

    public abstract void printDetails();

    public String getId(){
        return id;
    } 

    public double getstartingPrice(){
        return startingPrice;
    }

}
