package com.example.storyapp.stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.MainActivity;
import com.example.storyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPopularStoriesActivity extends AppCompatActivity {
    private RecyclerView.Adapter storyAdapter;
    private ArrayList<Story> resultStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stories);
        resultStories=new ArrayList<Story>();
        Button backButton = findViewById(R.id.back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager newLayoutManager = new LinearLayoutManager(this);
        newLayoutManager.setReverseLayout(true);
        newLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(newLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPopularStoriesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        storyAdapter=new StoryAdapter(getListStories(), ViewPopularStoriesActivity.this);
        Collections.sort(resultStories, new Comparator<Story>() {//sort by rating
            @Override
            public int compare(Story s1, Story s2) {
                return Integer.compare(s1.getRating(), s2.getRating());
            }
        });
        recyclerView.setAdapter(storyAdapter);

        getStories();
    }

    private void getStories() {
        DatabaseReference storyDB = FirebaseDatabase.getInstance().getReference().child("Uploads");
        storyDB.addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference userDB= FirebaseDatabase.getInstance().getReference().child("Uploads").child(key);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String storyID = snapshot.getKey();
                    String name="";
                    String description="";
                    String authorID="";
                    String storyURL="";
                    int rating=0;
                    if(snapshot.child("author").getValue()!=null){
                        authorID=snapshot.child("author").getValue().toString();
                    }

                    if(snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("desc").getValue()!=null){
                        description=snapshot.child("desc").getValue().toString();
                    }

                    if(snapshot.child("url").getValue()!=null){
                        storyURL=snapshot.child("url").getValue().toString();
                    }
                    if(snapshot.child("rating").getValue()!=null){
                        rating=Integer.parseInt(snapshot.child("rating").getValue().toString());
                    }

                    Story newStory = new Story(storyID, name, authorID, description, storyURL, rating);
                    //    findAuthorName(authorID, newStory);
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