package com.example.storyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyapp.authors.ViewAuthorsActivity;
import com.example.storyapp.authors.ViewSearchedAuthorsActivity;
import com.example.storyapp.stories.ViewLikedStoriesActivity;
import com.example.storyapp.stories.ViewPopularStoriesActivity;
import com.example.storyapp.stories.ViewSearchedStoriesActivity;
import com.example.storyapp.stories.ViewStoriesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth myAuth;
    private String currentUserID;
    RadioGroup searchType;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logoutButton = findViewById(R.id.logout);
        Button viewProfileButton = findViewById(R.id.viewProfile);
        Button uploadFileButton = findViewById(R.id.uploadFile);
        searchText = findViewById(R.id.searchText);
        searchType = findViewById(R.id.searchRadioGroup);
        myAuth = FirebaseAuth.getInstance();
        currentUserID = myAuth.getCurrentUser().getUid();



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAuth.signOut();
                Intent intent = new Intent(MainActivity.this, StartingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        uploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadFileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void search(View view) {
        int typeID = searchType.getCheckedRadioButtonId();
        Intent intent;
        Bundle bundle;
        String keyword;
        switch (typeID){
            case R.id.storyChecked:
                intent = new Intent(view.getContext(), ViewSearchedStoriesActivity.class);
                bundle = new Bundle();
                keyword = searchText.getText().toString();
                bundle.putString("keyword", keyword);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                finish();
                break;
            case R.id.authorChecked:
                intent = new Intent(view.getContext(), ViewSearchedAuthorsActivity.class);
                bundle = new Bundle();
                keyword = searchText.getText().toString();
                bundle.putString("keyword", keyword);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                finish();
                break;
            default:
                break;
        }

    }

    public void viewAuthors(View view) {
        Intent intent = new Intent(MainActivity.this, ViewAuthorsActivity.class);
        startActivity(intent);
        finish();
    }
    public void viewNewStories(View view) {
        Intent intent = new Intent(MainActivity.this, ViewStoriesActivity.class);
        startActivity(intent);
        finish();
    }
    public void viewMyLikedStories(View view) {
        Intent intent = new Intent(view.getContext(), ViewLikedStoriesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("authorID", currentUserID);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
        finish();
    }
    public void viewPopularStories(View view) {
        Intent intent = new Intent(MainActivity.this, ViewPopularStoriesActivity.class);
        startActivity(intent);
        finish();
    }
}