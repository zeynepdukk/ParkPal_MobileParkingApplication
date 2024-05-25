package com.example.parkpal1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Street3Activity_Driver extends AppCompatActivity {
    double lat=40.956987;
    double lon=29.079050;
    private TextView availableSpaceTextView;
    private TextView fullSpaceTextView;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street3_driver);

        dbHelper = new DatabaseHelper(this);
        availableSpaceTextView = findViewById(R.id.available_space);
        fullSpaceTextView = findViewById(R.id.full_space);
        Button backButton = findViewById(R.id.parking3_backbutton);
        updateTextViews();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street3Activity_Driver.this, SimpleMapActivity.class);
                startActivity(intent);
            }
        });

        Button getDirectionButton = findViewById(R.id.parking3_getDirection);
        getDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street3Activity_Driver.this, GetDirectionActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);

            }
        });
}
    private void updateTextViews() {
        int availableSpace = dbHelper.getAvailableSpace();
        int fullSpace = dbHelper.getFullSpace();
        availableSpaceTextView.setText(String.valueOf(availableSpace));
        fullSpaceTextView.setText(String.valueOf(fullSpace));
    }
}
