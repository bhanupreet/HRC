package com.hrc.hrc;

public class Item  {
    public String itemName, itemDesc,itemOneDesc,image, product;

    public  Item(){}

    public Item(String itemName, String itemOneDesc, String itemDesc, String image) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemOneDesc = itemOneDesc;
        this.image = image;
    }
}
