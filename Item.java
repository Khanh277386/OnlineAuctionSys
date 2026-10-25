package com.auction.model;

public abstract class Item implements Entity{
    private String Id;
    private String name;
    private String description;
    private String sellerId;
    private Seller seller;
    public Item(String Id, String name, String description,  String sellerId, Seller seller) {
        this.Id = Id;
        this.name = name;
        this.description = description;
        this.sellerId = sellerId;
        this.seller = seller;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getId(){
        return Id;
    } 

    

}
