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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street3);

        // Initialize views
        availableSpaceTextView = findViewById(R.id.available_space);
        fullSpaceTextView = findViewById(R.id.full_space);

        // Set initial values
        availableSpaceTextView.setText("35");
        fullSpaceTextView.setText("15");

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
    }
}
