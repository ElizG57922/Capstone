package com.example.storyapp.stories;

public class Story {
    private String storyID;
    private String name;
    private String authorID;
    private String authorName;
    private String description;
    private String storyURL;

    public Story(String storyID, String name, String authorID, String description, String storyURL){
        this.storyID=storyID;
        this.name=name;
        this.authorID=authorID;
        this.authorName=authorID;
        this.description=description;
        this.storyURL=storyURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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
    public String getStoryURL() {
        return storyURL;
    }
    public void setStoryURL(String storyURL) {
        this.storyURL = storyURL;
    }
}