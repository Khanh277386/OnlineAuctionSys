
package com.auction.model;

public class Art extends Item {
    
    private String style;
    private int age;
    public Art(String Id, String name, String description, Seller seller, String style, int age) {
    super(Id, name, description,  seller); // Gọi hàm khởi tạo của Item
    this.style = style;
    this.age = age;
}

 
    public String getStyle(){
        return style;
    }

  
    public int getAge() {
        return age;
    }
    @Override
    public void printInfo() {
    System.out.println("Item: " + getName() + " | Type: Art" + 
                                    " Style: " + getStyle() 
                                + " Age: "+ getAge());
}
}
