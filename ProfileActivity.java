package com.example.storyapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.storyapp.connections.ViewConnectionsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText nameText, phoneText;
    private ImageView profilePic;
    private DatabaseReference customerDB;
    private String userID, name, phone, profilePicURL;
    private Uri resultURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameText = findViewById(R.id.name);
        phoneText = findViewById(R.id.phone);
        profilePic = findViewById(R.id.profilePic);
        Button saveButton = findViewById(R.id.save);
        FirebaseAuth myAuth = FirebaseAuth.getInstance();
        userID= myAuth.getCurrentUser().getUid();
        customerDB= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        getUserInfo();
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getResult.launch(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
    }
    ActivityResultLauncher<Intent>getResult=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        final Uri imageURI = result.getData().getData();
                        resultURI=imageURI;
                        profilePic.setImageURI(resultURI);
                }
            }
    });
    private void saveProfile(){
        name=nameText.getText().toString();
        phone=phoneText.getText().toString();
        Map userInfo=new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        customerDB.updateChildren(userInfo);
        if(resultURI!=null){
            try {
                StorageReference path= FirebaseStorage.getInstance().getReference().child("profileImage").child(userID);
                Bitmap bmp= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultURI);
                ByteArrayOutputStream byteOutput=new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 15, byteOutput);
                byte[] data=byteOutput.toByteArray();
                UploadTask uploadTask=path.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map userInfo=new HashMap();
                                userInfo.put("profilePicURL", uri.toString());
                                customerDB.updateChildren(userInfo);
                                Toast.makeText(ProfileActivity.this, "Save successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getUserInfo(){
        customerDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null) {
                        name = map.get("name").toString();
                        nameText.setText(name);
                    }
                    if(map.get("phone")!=null) {
                        phone = map.get("phone").toString();
                        phoneText.setText(phone);
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
}