package com.example.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText nameText, emailText, passwordText;
    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        myAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent  = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        registerButton = findViewById(R.id.register);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        nameText = findViewById(R.id.name);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameText.getText().toString();
                final String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();
                if (!name.equals("") && !email.equals("") && !password.equals("")) {
                    myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Error! Make sure password is at least 6 characters.", Toast.LENGTH_SHORT).show();
                            } else {
                                String userID = myAuth.getCurrentUser().getUid();
                                DatabaseReference curUserId = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                                Map userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("profilePicURL", "defaultImage");

                                curUserId.updateChildren(userInfo);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Fields cannot be blank", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        myAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        myAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}