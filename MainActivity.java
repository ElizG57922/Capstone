package com.example.storyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyapp.authors.ViewAuthorsActivity;
import com.example.storyapp.stories.ViewLikedStoriesActivity;
import com.example.storyapp.stories.ViewPopularStoriesActivity;
import com.example.storyapp.stories.ViewStoriesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth myAuth;
    private String currentUserID;
    private static final String[] dropdownChoices = {"Story", "Author"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logoutButton = findViewById(R.id.logout);
        Button viewProfileButton = findViewById(R.id.viewProfile);
        Button uploadFileButton = findViewById(R.id.uploadFile);
        Button searchButton = findViewById(R.id.searchButton);
        EditText searchText = findViewById(R.id.searchText);
        Spinner dropdown = findViewById(R.id.dropdownMenu);
        myAuth = FirebaseAuth.getInstance();
        currentUserID = myAuth.getCurrentUser().getUid();

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, dropdownChoices);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


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
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0://Story
                // Whatever you want to happen when the first item gets selected
                break;
            case 1://Author
                // Whatever you want to happen when the second item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
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