package com.example.storyapp.authors;

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
import com.example.storyapp.stories.ViewSearchedStoriesActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAuthorsActivity extends AppCompatActivity {
    private RecyclerView.Adapter authorAdapter;
    private ArrayList<Author> resultAuthors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_authors);
        resultAuthors=new ArrayList<Author>();
        Button backButton=findViewById(R.id.back);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager newLayoutManager = new LinearLayoutManager(ViewAuthorsActivity.this);
        recyclerView.setLayoutManager(newLayoutManager);
        authorAdapter=new AuthorAdapter(getListAuthors(), ViewAuthorsActivity.this);
        recyclerView.setAdapter(authorAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAuthorsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getAuthors();
    }

    private void getAuthors() {
        DatabaseReference connectionDB = FirebaseDatabase.getInstance().getReference().child("Users");
        connectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match: snapshot.getChildren()){
                        getAuthorInfo(match.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getAuthorInfo(String key) {
        DatabaseReference userDB= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userID = snapshot.getKey();
                    String name="";
                    String bio="";
                    String profilePicURL="";
                    if(snapshot.child("name").getValue()!=null){
                        name=snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("bio").getValue()!=null){
                        bio=snapshot.child("bio").getValue().toString();
                    }
                    if(snapshot.child("profilePicURL").getValue()!=null){
                        profilePicURL=snapshot.child("profilePicURL").getValue().toString();
                    }

                    Author newAuthor = new Author(userID, name, bio, profilePicURL);
                    resultAuthors.add(newAuthor);
                    authorAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private List<Author> getListAuthors(){
        return resultAuthors;
    }
}