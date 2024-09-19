package com.example.appdevynmustard;

public class InventoryItem {
    public int id;
    private String name;
    private float price;
    private int count;

    public InventoryItem(String name, float price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    // getters and setters for item variables
    public float getPrice() {
        return price;
    }
    public int getCount() {
        return count;
    }
    public void setName(String updatedName) {
        this.name = updatedName;
    }
    public void setCount(int updatedCount) {
        this.count = updatedCount;
    }
    public void setPrice(float updatedPrice) {
        this.price = updatedPrice;
    }
    public int getId() { return id; }
}