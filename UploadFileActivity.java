package com.example.storyapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


//https://www.youtube.com/watch?v=lY9zSr6cxko
//https://developer.android.com/training/secure-file-sharing/share-file
public class UploadFileActivity extends AppCompatActivity {
    StorageReference storageRef;
    DatabaseReference databaseRef;
    EditText text;
    Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        text = findViewById(R.id.fileText);
        uploadButton = findViewById(R.id.uploadButton);
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");

       // uploadButton.setEnabled(false);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });

    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
    //    Intent chooserIntent = Intent.createChooser(intent, "choosefile");
  //      getResult.launch(chooserIntent);
        startActivityForResult(Intent.createChooser(intent, "choosefile"), 12);
    }


  /*  ActivityResultLauncher<Intent> getResult=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        uploadButton.setEnabled(true);
                        text.setText(result.getData().getDataString().substring(result.getData().getDataString().lastIndexOf("/")+1));
                        
                        uploadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadPDFFirebase(result.getData().getData());
                            }
                        });

                    }
                }
            });

   */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode == RESULT_OK && data != null && data.getData()!= null){
      //      uploadButton.setEnabled(true);
            text.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPDFFirebase(data.getData());
                }
            });
        }
    }

    private void uploadPDFFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File loading...");
        progressDialog.show();

        StorageReference ref = storageRef.child("upload"+System.currentTimeMillis()+".pdf");
        ref.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                PutFileInStorage putPDF = new PutFileInStorage(text.getText().toString(), uri.toString());
                databaseRef.child(databaseRef.push().getKey()).setValue(putPDF);
                Toast.makeText(UploadFileActivity.this, "File Upload", Toast.LENGTH_LONG).show();
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
/*
    private void createGoal(String activity, String timeframe, String number, String unit) throws IOException {

        //creates an instance of the Main Dashboard class in order to access the variable counterString.
        MainDashboard dBoard = new MainDashboard();

        //Names the 0.txt, 1.txt, 2.txt, and so on
        File file = new File(dBoard.counterString + ".txt");

        //Creates the actual file
        file.createNewFile();

        //Creates the writer object that will write to the file
        FileWriter writer = new FileWriter(file);

        //Writes to the text file
        writer.write(activity + " : " + "0 / "+ number + " " + unit + " in " + timeframe);

        //Closes the Writer
        writer.close();

        //Creates a Uri from the file to be uploaded
        upload = Uri.fromFile(new File(activity + ".txt"));

        //Uploads the file exactly as the documentation says, but it doesn't work
        UploadTask uploadTask = storageRef.putFile(upload);
// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...

                //Deletes the file from the local system
                file.delete();

            }
        });
    }
}*/