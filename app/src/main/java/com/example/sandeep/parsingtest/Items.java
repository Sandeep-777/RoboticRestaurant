package com.example.sandeep.parsingtest;

public class Items {


    private String title;
    private int image;
    private int number;
    private String cost;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber(){ return  number; }

    public void setNumber(int sel){ this.number = sel; }

    public String getCost(){ return  cost; }

    public void setCost(String cost){ this.cost = cost; }

    public int getThumbnail() {
        return image;
    }

    public void setThumbnail(int thumbnail) {
        this.image = thumbnail;
    }

}

