package com.example.storyapp;

public class PutFileInStorage {
    private String name;
    private String url;
    private String author;

    public PutFileInStorage(String name, String url, String author){
        this.name=name;
        this.url=url;
        this.author=author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) { this.author=author;}

    public String getAuthor(){return author;}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
