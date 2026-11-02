
package com.auction.model;

public abstract class Item implements Entity{
    private String Id;
    private String name;
    private String description;
    private Seller seller;
    public Item(String Id, String name, String description, Seller seller) {
        this.Id = Id;
        this.name = name;
        this.description = description;
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

    public abstract void printInfo();

}
