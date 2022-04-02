package com.example.storyapp.reviews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.storyapp.MainActivity;
import com.example.storyapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView.Adapter reviewAdapter;
    private String currentUserID, storyID, reviewID;
    private ArrayList<Review> resultReviews;
    private EditText reviewTextfield;
    DatabaseReference databaseReviewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        storyID=getIntent().getExtras().getString("storyID");
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReviewer= FirebaseDatabase.getInstance().getReference().child("Uploads").child(storyID).child("reviews");
        getReviews();

        resultReviews=new ArrayList<Review>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
        reviewAdapter=new ReviewAdapter(getDataSetReviews(), ReviewActivity.this);
        recyclerView.setAdapter(reviewAdapter);

        reviewTextfield=findViewById(R.id.reviewTextfield);
        Button sendButton = findViewById(R.id.send);
        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReview();
            }
        });
    }

    private void postReview() {
        String newReviewText=reviewTextfield.getText().toString();
        if(!newReviewText.isEmpty()){
            DatabaseReference newReviewDB=databaseReviewer.push();
            Map newReview = new HashMap();
            newReview.put("createdBy", currentUserID);
            newReview.put("text", newReviewText);
            newReviewDB.setValue(newReview);
        }
        reviewTextfield.setText(null);
    }
    private List<Review> getDataSetReviews(){
        return resultReviews;
    }

    private void getReviews() {
        databaseReviewer.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String review="";
                    if(snapshot.child("text").getValue()!=null){
                        review=snapshot.child("text").getValue().toString();
                    }

                    Review newReview=new Review(review);
                    resultReviews.add(newReview);
                    reviewAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}