package com.auction.model;

public abstract class Item implements Entity{
    private String Id;
    private String title;
    private String description;
    private double StartingPrice;
    private String sellerId;
    public Item(String Id, String title, String description, double StartingPrice, String sellerId) {
        this.Id = Id;
        this.title = title;
        this.description = description;
        this.StartingPrice = StartingPrice;
        this.sellerId = sellerId;
    }

    public abstract String getCategory();

    public abstract void printDetails();

    public String getId(){
        return Id;
    } 

    public double getStartingPrice(){
        return StartingPrice;
    }

}
