package com.example.storyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UploadFileActivity extends AppCompatActivity {
    StorageReference storageRef;
    DatabaseReference databaseRef;
    EditText filenameText, description;
    Button uploadButton, backButton;
    RadioGroup filetype;
    int typeID;
    String typeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        filenameText = findViewById(R.id.fileText);
        uploadButton = findViewById(R.id.uploadButton);
        backButton=findViewById(R.id.back);
        filetype=findViewById(R.id.typegroup);
        description = findViewById(R.id.desc);
        typeID = filetype.getCheckedRadioButtonId();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference("Uploads");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadFileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });
    }

    private void selectFile() {
        typeID = filetype.getCheckedRadioButtonId();
        Intent intent = new Intent();
        switch (typeID){
            case R.id.typePDF:
                intent.setType("application/pdf");
                typeString="pdf";
                break;
            case R.id.typeTXT:
            default:
                intent.setType("text/plain");
                typeString="txt";
                break;
        }
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choosefile"), 12);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode == RESULT_OK && data != null && data.getData()!= null){
            filenameText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadFileFirebase(data.getData());
                }
            });
        }
    }

    private void uploadFileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File loading...");
        progressDialog.show();
        StorageReference ref;
        switch (typeID){
            case R.id.typePDF:
                ref = storageRef.child("upload"+System.currentTimeMillis()+".pdf");
                break;
            case R.id.typeTXT:
            default:
                ref = storageRef.child("upload"+System.currentTimeMillis()+".txt");
                break;
        }

        ref.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());//wait
                Uri uri = uriTask.getResult();

                String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

               // databaseRef.child(databaseRef.push().getKey()).setValue(ref.toString());
                Map storyInfo=new HashMap();
                storyInfo.put("name", filenameText.getText().toString());
                storyInfo.put("url", uri.toString());
                storyInfo.put("author", currentUID);
                storyInfo.put("desc", description.getText().toString());
                storyInfo.put("type", typeString);
                storyInfo.put("rating", 0);
                databaseRef.child(currentUID+System.currentTimeMillis()).updateChildren(storyInfo);
                DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                userDatabase.child(currentUID).child("stories").child(filenameText.getText().toString()).setValue(uri.toString());
                Toast.makeText(UploadFileActivity.this, "File Uploaded", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });
    }
}