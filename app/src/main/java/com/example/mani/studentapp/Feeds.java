package com.example.mani.studentapp;

/**
 * Created by mani on 2/13/18.
 */

public class Feeds {

    private String title,desc;
    int image;

    public Feeds(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
