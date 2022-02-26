package com.example.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;


public class ViewStoryActivity extends AppCompatActivity {
    private String currentUserID, storyID;
    private EditText nameTextField, descTextField, authorTextField;
    private PDFView pdfView;
    private TextView textView;
    private String name, desc, author, storyURL;
    DatabaseReference databaseAuthor, databaseUploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        storyID = getIntent().getExtras().getString("storyID");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // databaseUser= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("match").child(connectionID).child("messageID");
        databaseUploads = FirebaseDatabase.getInstance().getReference().child("Uploads").child(storyID);
     //   databaseAuthor = FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

        nameTextField = findViewById(R.id.name);
        descTextField = findViewById(R.id.desc);
        authorTextField = findViewById(R.id.authorName);
        Button backButton = findViewById(R.id.back);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        textView = findViewById(R.id.storyText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewStoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getStoryInfo();
    }


    private void getStoryInfo(){
        databaseUploads.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null) {
                        name = map.get("name").toString();
                        nameTextField.setText(name);
                    }
                    if(map.get("desc")!=null) {
                        desc = map.get("desc").toString();
                        descTextField.setText(desc);
                    }
                    if(map.get("author")!=null) {
                        String authorID = map.get("author").toString();
                        findAuthorName(authorID);
                    }

                    if(map.get("url")!=null) {
                        storyURL = map.get("url").toString();
                        if(map.get("type").equals("pdf"))
                            new GetPDFFirebase().execute(storyURL);
                        else if (map.get("type").equals("txt")){
                            File file = new File(storyURL);
                            try {

                                FileInputStream fis = new FileInputStream(file);
                                String string = "";
                                StringBuilder stringBuilder = new StringBuilder();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                                while (true) {
                                    try {
                                        if ((string = reader.readLine()) == null) break;
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    stringBuilder.append(string).append("\n");
                                    textView.setText(stringBuilder);
                                }
                                fis.close();
                                FileReader r = new FileReader(file);
                                r.close();
                            } catch (Exception e) { }
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void findAuthorName(String authorID) {
        DatabaseReference authorDB=FirebaseDatabase.getInstance().getReference().child("Users").child(authorID);

        authorDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if(snapshot.child("name").getValue()!=null) {
                        String authorName=snapshot.child("name").getValue().toString();
                        authorTextField.setText("By "+authorName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    class GetPDFFirebase extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream pdfStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    pdfStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return pdfStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}