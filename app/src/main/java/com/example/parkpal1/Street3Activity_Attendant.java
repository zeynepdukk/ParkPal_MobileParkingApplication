package com.example.parkpal1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Street3Activity_Attendant extends AppCompatActivity {
    double lat=40.956987;
    double lon=29.079050;
    private TextView availableSpaceTextView;
    private TextView fullSpaceTextView;

    private Button addButton;
    private Button minusButton;
    private String userRole;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street3_attendant);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        availableSpaceTextView = findViewById(R.id.available_space);
        fullSpaceTextView = findViewById(R.id.full_space);
        addButton = findViewById(R.id.addButton);
        minusButton = findViewById(R.id.minusButton);

        updateTextViews();
        // Set onClickListener for Back button
        Button backButton = findViewById(R.id.parking3_backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street3Activity_Attendant.this, SimpleMapActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment availableSpace and decrement fullSpace
                int availableSpace = Integer.parseInt(availableSpaceTextView.getText().toString()) + 1;
                int fullSpace = Integer.parseInt(fullSpaceTextView.getText().toString()) - 1;
                updateSpaces(availableSpace, fullSpace);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement availableSpace and increment fullSpace
                int availableSpace = Integer.parseInt(availableSpaceTextView.getText().toString()) - 1;
                int fullSpace = Integer.parseInt(fullSpaceTextView.getText().toString()) + 1;
                updateSpaces(availableSpace, fullSpace);
            }
        });
    }
    private void updateTextViews() {
        int availableSpace = dbHelper.getAvailableSpace();
        int fullSpace = dbHelper.getFullSpace();
        availableSpaceTextView.setText(String.valueOf(availableSpace));
        fullSpaceTextView.setText(String.valueOf(fullSpace));
    }

    private void updateSpaces(int availableSpace, int fullSpace) {
        // Update the values in the database
        dbHelper.updateSpaces(availableSpace, fullSpace);
        // Update the text views
        availableSpaceTextView.setText(String.valueOf(availableSpace));
        fullSpaceTextView.setText(String.valueOf(fullSpace));
    }
}