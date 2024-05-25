package com.example.reachout;

import android.net.Uri;

public class item {

    String name, phone;
    int image;


    private Uri imageUri;

    public item(String name, String phone, int image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public item(String name, String phone, Uri imageUri) {
        this.name = name;
        this.phone = phone;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
