package com.example.facebook.model;

public class Post {
    String _id, status, image, name;

    public Post( String status, String image, String name) {

        this.status = status;
        this.image = image;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
