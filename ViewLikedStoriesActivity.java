package com.example.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.storyapp.stories.Story;
import com.example.storyapp.stories.StoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewLikedStoriesActivity extends AppCompatActivity {

    private String authorID, authorName;
    private DatabaseReference databaseAuthor;
    private RecyclerView.Adapter storyAdapter;
    private ArrayList<Story> resultStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_liked_stories);

        Button backButton = findViewById(R.id.back);

        resultStories=new ArrayList<Story>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storyAdapter=new StoryAdapter(getListStories(), ViewLikedStoriesActivity.this);
        recyclerView.setAdapter(storyAdapter);

        authorID = getIntent().getExtras().getString("authorID");
        databaseAuthor = FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

        getUserInfo();
        getMyStories();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLikedStoriesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getUserInfo(){
        databaseAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    authorName=snapshot.child("name").toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getMyStories() {
        databaseAuthor.child("likedStories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match: snapshot.getChildren()){
                        getStoryInfo(match.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getStoryInfo(String key) {
        DatabaseReference storyDB = FirebaseDatabase.getInstance().getReference().child("Uploads").child(key);
        storyDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String storyID = snapshot.getKey();
                    String name="";
                    String description="";
                    String storyURL="";
                    if(snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("desc").getValue()!=null){
                        description=snapshot.child("desc").getValue().toString();
                    }
                    if(snapshot.child("url").getValue()!=null){
                        storyURL=snapshot.child("url").getValue().toString();
                    }

                    Story newStory = new Story(storyID, name, authorID, authorName, description, storyURL);
                    resultStories.add(newStory);
                    storyAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private List<Story> getListStories(){
        return resultStories;
    }
}