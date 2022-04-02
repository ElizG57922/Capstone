package com.example.storyapp.stories;

public class Story {
    private String storyID;
    private String name;
    private String authorID;
    private String description;
    private int rating;

    public Story(String storyID, String name, String authorID, String description, int rating){
        this.storyID=storyID;
        this.name=name;
        this.authorID=authorID;
        this.description=description;
        this.rating=rating;
    }

    public int getRating(){
        return rating;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStoryID(){
        return storyID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthorID() {
        return authorID;
    }
}