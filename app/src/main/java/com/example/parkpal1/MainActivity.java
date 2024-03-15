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

    private Button startButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        startButton = findViewById(R.id.startButton);
        dbHelper = new DatabaseHelper(this);

        // Set onClickListener for the start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event
                // For now, let's just start the LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Log the list of users
        logUsers();
    }

    private void logUsers() {
        // Kullanıcı listesini al
        List<User> userList = dbHelper.getAllUsers();

        // Eğer kullanıcı listesi boş değilse
        if (userList != null && !userList.isEmpty()) {
            // Her bir kullanıcı için
            for (User user : userList) {
                // Kullanıcı bilgilerini logla
                Log.d("User", "Username: " + user.getUsername() +
                        ", Email: " + user.getEmail() +
                        ", Role: " + user.getRole());
            }
        } else {
            // Kullanıcı bulunamadıysa
            Log.d("User", "No users found in the database.");
        }
    }
}
