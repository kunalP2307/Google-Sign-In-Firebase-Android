package com.example.googlesignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmailActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button btnContinue;
    User userDetails;
    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        bindComponents();
        addListeners();
        loadUserData();

    }

    private void bindComponents(){
        this.editTextEmail = findViewById(R.id.editEmail);
        this.btnContinue = findViewById(R.id.btnContWithEmail);
    }

    private void addListeners(){

        btnContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();

                if(checkForRegisteredUser()){
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username",userDetails.getUserName());
                    editor.putString("userEmail",userDetails.getUserEmail());
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),NameActivity.class);
                    intent.putExtra("EXTRA_EMAIL",email);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean checkForRegisteredUser(){

        String email = editTextEmail.getText().toString();

        for(User user : userList){
            Log.d("", "checkForRegisteredUser: "+user.getUserEmail());
            if(user.getUserEmail().equals(email)){
                userDetails = user;
                return true;
            }
        }
        return false;

    }


    private void loadUserData(){

        FirebaseDatabase.getInstance().getReference("UserDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postDataSnapshot : snapshot.getChildren()){
                    User user = postDataSnapshot.getValue(User.class);
                    userList.add(user);
                    Log.d("", "onDataChange: "+user.getUserEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}