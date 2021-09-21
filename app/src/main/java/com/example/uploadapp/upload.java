package com.example.uploadapp;

public class upload {
    String oxygen,temp,place;

    public upload(String id, String temp, String place) {
        this.oxygen = id;
        this.temp = temp;
        this.place = place;
    }

    public upload() {
    }

    public String getOxygen() {
        return oxygen;
    }

    public String getTemp() {
        return temp;
    }

    public String getPlace() {
        return place;
    }
}