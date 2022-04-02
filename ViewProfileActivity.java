package com.example.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.storyapp.stories.Story;
import com.example.storyapp.stories.StoryAdapter;
import com.example.storyapp.stories.ViewLikedStoriesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewProfileActivity extends AppCompatActivity {
    private RecyclerView.Adapter storyAdapter;
    private ArrayList<Story> resultStories;
    private String currentUserID, authorID;
    private EditText nameTextField, bioTextField;
    private ImageView profilePic;
    private String name, bio, profilePicURL;
    private Button editButton;
    DatabaseReference databaseAuthor, databaseUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        nameTextField = findViewById(R.id.name);
        bioTextField = findViewById(R.id.bio);
        profilePic = findViewById(R.id.profilePic);
        Button backButton = findViewById(R.id.back);
        editButton=findViewById(R.id.editButton);
        editButton.setEnabled(false);

        FirebaseAuth myAuth = FirebaseAuth.getInstance();

        authorID = getIntent().getExtras().getString("authorID");
        if(authorID.equals(myAuth.getCurrentUser().getUid())) { //if the current user is selected
            editButton.setEnabled(true);
            editButton.setVisibility(View.VISIBLE);
        }
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseUploads = FirebaseDatabase.getInstance().getReference().child("Uploads");
        databaseAuthor = FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

        resultStories=new ArrayList<Story>();
        RecyclerView recyclerView = findViewById(R.id.myStoryRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storyAdapter=new StoryAdapter(getListStories(), ViewProfileActivity.this);
        recyclerView.setAdapter(storyAdapter);

        getMyStories();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getUserInfo();
    }

    private void getUserInfo(){
        databaseAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null) {
                        name = map.get("name").toString();
                        nameTextField.setText(name);
                    }
                    if(map.get("bio")!=null) {
                        bio = map.get("bio").toString();
                        bioTextField.setText(bio);
                    }
                    if(map.get("profilePicURL")!=null) {
                        profilePicURL = map.get("profilePicURL").toString();
                        switch (profilePicURL){
                            case "defaultImage":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(profilePic);
                                break;
                            default://has image URL
                                Glide.with(getApplication()).load(profilePicURL).into(profilePic);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getMyStories() {
        databaseAuthor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("stories").exists()){
                    for(DataSnapshot match: snapshot.child("stories").getChildren()){
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
                    int rating=0;
                    if(snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("desc").getValue()!=null){
                        description=snapshot.child("desc").getValue().toString();
                    }
                    if(snapshot.child("rating").getValue()!=null){
                        rating=Integer.parseInt(snapshot.child("rating").getValue().toString());
                    }

                    Story newStory = new Story(storyID, name, authorID, description, rating);
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

    public void viewLikedStories(View view) {
        Intent intent = new Intent(view.getContext(), ViewLikedStoriesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("authorID", authorID);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
        finish();
    }
}