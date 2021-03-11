package com.mob.developer.hw1;

public class Coin {
    private String name;
    private String imgAddress;
    private String price;
    private String abbrName;
    private String change7d;
    private String change1h;
    private String change24h;

    public Coin(String name, String imgAddress, String price, String abbrName, String change7d, String change1h, String change24h) {
        this.name = name;
        this.imgAddress = imgAddress;
        this.price = price;
        this.abbrName = abbrName;
        this.change7d = change7d;
        this.change1h = change1h;
        this.change24h = change24h;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public String getChange7d() {
        return change7d;
    }

    public void setChange7d(String change7d) {
        this.change7d = change7d;
    }

    public String getChange1h() {
        return change1h;
    }

    public void setChange1h(String change1h) {
        this.change1h = change1h;
    }

    public String getChange24h() {
        return change24h;
    }

    public void setChange24h(String change24h) {
        this.change24h = change24h;
    }


    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImgAddress() {
        return imgAddress;
    }
}
