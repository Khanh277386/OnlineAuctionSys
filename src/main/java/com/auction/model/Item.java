
package com.auction.model;

public abstract class Item implements Entity{
    private final String Id;
    private String name;
    private String description;
    private final Seller seller;
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

    public Seller getSeller() {
        return seller;
    }

    public synchronized void update(String newName, String newDescription) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng");
        }
        this.name = newName;
        this.description = newDescription;
    }

    public abstract void printInfo();

}
