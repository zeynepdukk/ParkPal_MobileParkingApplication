package com.example.parkpal1;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Street3Activity extends AppCompatActivity {
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
        setContentView(R.layout.street3);

        userRole = "Attendant";
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
                Intent intent = new Intent(Street3Activity.this, SimpleMapActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for Get Direction button
        Button getDirectionButton = findViewById(R.id.parking3_getDirection);
        getDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street3Activity.this, GetDirectionActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);

            }
        });
        if (userRole.equals("Attendant")) {
            addButton.setVisibility(View.VISIBLE);
            minusButton.setVisibility(View.VISIBLE);
        } else { // Kullanıcı bir Driver ise, butonları gizle
            addButton.setVisibility(View.GONE);
            minusButton.setVisibility(View.GONE);
        }

        // Set onClickListener for "Add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment availableSpace and decrement fullSpace
                int availableSpace = Integer.parseInt(availableSpaceTextView.getText().toString()) + 1;
                int fullSpace = Integer.parseInt(fullSpaceTextView.getText().toString()) - 1;
                updateSpaces(availableSpace, fullSpace);
            }
        });

        // Set onClickListener for "Minus" button
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

    // Update the text views with the values from the database
    private void updateTextViews() {
        int availableSpace = dbHelper.getAvailableSpace();
        int fullSpace = dbHelper.getFullSpace();
        availableSpaceTextView.setText(String.valueOf(availableSpace));
        fullSpaceTextView.setText(String.valueOf(fullSpace));
    }

    // Update the values in the database and text views
    private void updateSpaces(int availableSpace, int fullSpace) {
        // Update the values in the database
        dbHelper.updateSpaces(availableSpace, fullSpace);
        // Update the text views
        availableSpaceTextView.setText(String.valueOf(availableSpace));
        fullSpaceTextView.setText(String.valueOf(fullSpace));
    }
}
