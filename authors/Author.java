package com.example.storyapp.authors;

public class Author {
    private String userID;
    private String name;
    private String bio;
    private String profilePicURL;

    public Author(String userID, String name, String bio, String profilePicURL){
        this.userID=userID;
        this.name=name;
        this.bio=bio;
        this.profilePicURL=profilePicURL;
    }
    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getProfilePicURL() {
        return profilePicURL;
    }
    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}