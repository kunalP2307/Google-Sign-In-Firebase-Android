package com.example.googlesignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewEmail;
    Button btnLogout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindComponents();
        addListeners();

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);

        String userName = preferences.getString("username","");
        String userEmail = preferences.getString("userEmail","");

        Log.d("ProfileActivity", "onCreate: "+userEmail+userName);
        textViewEmail.setText(userEmail);
        textViewName.setText(userName);
    }

    private void bindComponents(){
        this.textViewName = findViewById(R.id.textName);
        this.textViewEmail = findViewById(R.id.textEmail);
        this.btnLogout = findViewById(R.id.btnLogout);
    }

    private void addListeners(){
        this.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(), "Signed Out!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}