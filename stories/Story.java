package com.example.storyapp.stories;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Story {
    private String storyID;
    private String name;
    private String authorID;
    private String authorName;
    private String description;
    private String storyURL;
    private int rating;

    public Story(String storyID, String name, String authorID, String description, String storyURL, int rating){
        this.storyID=storyID;
        this.name=name;
        this.authorID=authorID;
        findAuthorName(authorID);
        this.description=description;
        this.storyURL=storyURL;
        this.rating=rating;
    }
    public Story(String storyID, String name, String authorID, String authorName, String description, String storyURL, int rating){
        this.storyID=storyID;
        this.name=name;
        this.authorID=authorID;
        this.authorName=authorName;
        this.description=description;
        this.storyURL=storyURL;
        this.rating=rating;
    }
    private void findAuthorName(String authorID) {
        DatabaseReference authorDB = FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

        authorDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if(snapshot.child("name").getValue()!=null) {
                        authorName=snapshot.child("name").getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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