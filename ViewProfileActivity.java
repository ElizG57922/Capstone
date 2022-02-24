package com.example.storyapp;

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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.storyapp.messages.Message;
import com.example.storyapp.messages.MessageActivity;
import com.example.storyapp.messages.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProfileActivity extends AppCompatActivity {
    private RecyclerView.Adapter storyAdapter;
    private String currentUserID, authorID;//, messageID;
   // private ArrayList<Story> resultStories;
    private EditText nameTextField, bioTextField;
    private ImageView profilePic;
    private String name, bio, profilePicURL;
    DatabaseReference databaseAuthor, databaseUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        authorID = getIntent().getExtras().getString("authorID");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // databaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("match").child(connectionID).child("messageID");
        databaseUploads = FirebaseDatabase.getInstance().getReference().child("Uploads");
        databaseAuthor = FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);
        //  getMessageID();

        //resultMessages=new ArrayList<Message>();
    //    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    //    recyclerView.setNestedScrollingEnabled(false);
    //    recyclerView.setHasFixedSize(false);
    //    RecyclerView.LayoutManager profileLayoutManager = new LinearLayoutManager(ViewProfileActivity.this);
    //    recyclerView.setLayoutManager(profileLayoutManager);
        // messageAdapter=new MessageAdapter(getDataSetMessages(), MessageActivity.this);
        //recyclerView.setAdapter(messageAdapter);

        nameTextField = findViewById(R.id.name);
        bioTextField = findViewById(R.id.bio);
        profilePic = findViewById(R.id.profilePic);
        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getUserInfo();
        // Button sendButton = findViewById(R.id.send);

     /*   sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }*/

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

    private void sendMessage() {
        String newMessageText=nameTextField.getText().toString();
        if(!newMessageText.isEmpty()){
            DatabaseReference newMessageDB=databaseUploads.push();
            Map newMessage = new HashMap();
            newMessage.put("createdBy", currentUserID);
            newMessage.put("text", newMessageText);
            newMessageDB.setValue(newMessage);
        }
        nameTextField.setText(null);
    }/*
    private List<Message> getDataSetMessages(){
        return resultMessages;
    }
    private void getMessageID(){
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    messageID=snapshot.getValue().toString();
                    databaseMessenger=databaseMessenger.child(messageID);
                    getMessages();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getMessages() {
        databaseMessenger.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message=null;
                    String createdBy=null;
                    if(snapshot.child("text").getValue()!=null){
                        message=snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("createdBy").getValue()!=null){
                        message=snapshot.child("createdBy").getValue().toString();
                    }
                    if(message!=null && createdBy!=null){
                        boolean currentUserBoolean=false;
                        if(createdBy.equals(currentUserID)){
                            currentUserBoolean=true;
                        }
                        Message newMessage=new Message(message, currentUserBoolean);
                        resultMessages.add(newMessage);
                        messageAdapter.notifyDataSetChanged();
                    }
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
    }*/

}