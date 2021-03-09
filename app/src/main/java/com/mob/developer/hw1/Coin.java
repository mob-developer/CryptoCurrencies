package com.mob.developer.hw1;

public class Coin {
    private String name;
    private String year;
    private String price;

    public Coin(String name, String price, String year) {
        this.name = name;
        this.price = price;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getYear() {
        return year;
    }
}
