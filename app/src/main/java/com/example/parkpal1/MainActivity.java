package com.example.parkpal1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, signUpButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        loginButton = findViewById(R.id.loginButton1);
        signUpButton = findViewById(R.id.signUpButton1);
        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity when loginButton is clicked
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // Set click listener for the sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignUpActivity when signUpButton is clicked
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        logUsers();
    }

    private void logUsers() {
        List<User> userList = dbHelper.getAllUsers();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                Log.d("User", "Username: " + user.getUsername() +
                        ", Email: " + user.getEmail() +
                        ", Role: " + user.getRole());
            }
        } else {
            Log.d("User", "No users found in the database.");
        }
    }
}
