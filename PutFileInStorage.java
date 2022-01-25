package com.example.storyapp;

public class PutFileInStorage {
    private String name;
    private String url;

    public PutFileInStorage(String name, String url){
        this.name=name;
        this.url=url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
