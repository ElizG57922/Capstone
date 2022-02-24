package com.example.storyapp.stories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewStoriesActivity extends AppCompatActivity {
    private RecyclerView.Adapter storyAdapter;
    private String currentUserID;
    private ArrayList<Story> resultStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stories);
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        resultStories=new ArrayList<Story>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager matchLayoutManager = new LinearLayoutManager(ViewStoriesActivity.this);
        recyclerView.setLayoutManager(matchLayoutManager);
        storyAdapter=new StoryAdapter(getDataSetMatches(), ViewStoriesActivity.this);
        recyclerView.setAdapter(storyAdapter);

        getStories();
    }

    private void getStories() {
        DatabaseReference connectionDB = FirebaseDatabase.getInstance().getReference().child("Uploads");
        connectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match: snapshot.getChildren()){
                        getStoryInfo(match.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
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
                    String authorName="";
                    String storyURL="";
                    if(snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("desc").getValue()!=null){
                        description=snapshot.child("desc").getValue().toString();
                    }
                    if(snapshot.child("author").getValue()!=null){
                        authorID=snapshot.child("author").getValue().toString();
                    }
                    if(snapshot.child("url").getValue()!=null){
                        storyURL=snapshot.child("url").getValue().toString();
                    }

                    Story newStory = new Story(storyID, name, authorID, description, storyURL);
                    findAuthorName(authorID, newStory);
                    resultStories.add(newStory);
                    storyAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void findAuthorName(String authorID, Story newStory) {
        DatabaseReference authorDB=FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

            authorDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if(snapshot.child("name").getValue()!=null) {
                        String authorName=snapshot.child("name").getValue().toString();
                        newStory.setAuthorName(authorName);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private List<Story> getDataSetMatches(){
        return resultStories;
    }
}