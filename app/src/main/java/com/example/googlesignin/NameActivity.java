package com.example.googlesignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class NameActivity extends AppCompatActivity {

    EditText editTextEmail,editTextName;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);


        bindComponents();
        addListeners();
        showEmail();

    }

    private void bindComponents(){
        this.editTextEmail = findViewById(R.id.editSignInEmail);
        this.editTextName = findViewById(R.id.editSignInName);
        this.btnSignIn = findViewById(R.id.btnSignInWithEmail);
    }

    private void addListeners(){

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData())
                    registerUser();
            }
        });

    }

    private void showEmail(){
        String email = getIntent().getStringExtra("EXTRA_EMAIL");
        editTextEmail.setText(email);
    }

    private boolean isValidData(){

        String email = editTextEmail.getText().toString();
        String userName = editTextName.getText().toString();

        if(email.isEmpty()){
            editTextEmail.setError("Please Enter Email");
            editTextEmail.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Enter Valid Email");
            editTextEmail.requestFocus();
            return false;
        }

        if(userName.isEmpty()){
            editTextName.setError("Please Enter Full Name");
            editTextName.requestFocus();
            return false;
        }

        return true;

    }

    private void registerUser(){

        String email = editTextEmail.getText().toString();
        String userName = editTextName.getText().toString();

        User user = new User(userName,email);
        writeSharedPrefs(user);

        FirebaseDatabase.getInstance().getReference("UserDetails")
                .child(UUID.randomUUID().toString())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

    }

    private void writeSharedPrefs(User user){

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username",user.getUserName());
        editor.putString("userEmail",user.getUserEmail());
        editor.commit();
    }
}