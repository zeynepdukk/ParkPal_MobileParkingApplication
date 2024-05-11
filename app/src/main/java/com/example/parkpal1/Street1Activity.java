package com.example.parkpal1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Street1Activity extends AppCompatActivity {
    private String userRole;
    private DatabaseHelper dbHelper;
    double lat=40.956000;
    double lon=29.079911;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street1);

        dbHelper = new DatabaseHelper(this);
        updateImageViews();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "Driver"); // Default is "Driver"

        Button parking2_backbutton = findViewById(R.id.parking2_backbutton);
        Button a1_button = findViewById(R.id.a1_button);
        Button a4_button = findViewById(R.id.a4_button);
        Button b1_button = findViewById(R.id.b1_button);
        Button b4_button = findViewById(R.id.b4_button);
        Button a2_button = findViewById(R.id.a2_button);
        Button a5_button = findViewById(R.id.a5_button);
        Button b2_button = findViewById(R.id.b2_button);
        Button b5_button = findViewById(R.id.b5_button);
        Button a3_button = findViewById(R.id.a3_button);
        Button a6_button = findViewById(R.id.a6_button);
        Button b3_button = findViewById(R.id.b3_button);
        Button b6_button = findViewById(R.id.b6_button);
        Button c1_button = findViewById(R.id.c1_button);
        Button c3_button = findViewById(R.id.c3_button);
        Button c5_button = findViewById(R.id.c5_button);
        Button c7_button = findViewById(R.id.c7_button);
        Button c2_button = findViewById(R.id.c2_button);
        Button c4_button = findViewById(R.id.c4_button);
        Button c6_button = findViewById(R.id.c6_button);
        Button c8_button = findViewById(R.id.c8_button);

        parking2_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street1Activity.this, SimpleMapActivity.class);
                startActivity(intent);
            }
        });

        setButtonClickListener(a1_button, "A1_1");
        setButtonClickListener(a4_button, "A4_1");
        setButtonClickListener(b1_button, "B1_1");
        setButtonClickListener(b4_button, "B4_1");
        setButtonClickListener(a2_button, "A2_1");
        setButtonClickListener(a5_button, "A5_1");
        setButtonClickListener(b2_button, "B2_1");
        setButtonClickListener(b5_button, "B5_1");
        setButtonClickListener(a3_button, "A3_1");
        setButtonClickListener(a6_button, "A6_1");
        setButtonClickListener(b3_button, "B3_1");
        setButtonClickListener(b6_button, "B6_1");
        setButtonClickListener(c1_button, "C1_1");
        setButtonClickListener(c3_button, "C3_1");
        setButtonClickListener(c5_button, "C5_1");
        setButtonClickListener(c7_button, "C7_1");
        setButtonClickListener(c2_button, "C2_1");
        setButtonClickListener(c4_button, "C4_1");
        setButtonClickListener(c6_button, "C6_1");
        setButtonClickListener(c8_button, "C8_1");
    }

    private void setButtonClickListener(Button button, final String zone) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openZoneActivity(zone);
            }
        });
    }

    private void openZoneActivity(String zone) {
        Class<?> targetActivity;
        if (userRole.equals("Driver")) {
            targetActivity = ZoneActivity.class;
        } else {
            targetActivity = ZoneActivityA.class;
        }
        Intent intent = new Intent(Street1Activity.this, targetActivity);
        intent.putExtra("zone", zone);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("sourceActivity", "Street1Activity");
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    private void updateImageViews() {
        updateImageView(R.id.a1_imageView, dbHelper.getStatus("A1_1"));
        updateImageView(R.id.a4_imageView, dbHelper.getStatus("A4_1"));
        updateImageView(R.id.b1_imageView, dbHelper.getStatus("B1_1"));
        updateImageView(R.id.b4_imageView, dbHelper.getStatus("B4_1"));
        updateImageView(R.id.a2_imageView, dbHelper.getStatus("A2_1"));
        updateImageView(R.id.a5_imageView, dbHelper.getStatus("A5_1"));
        updateImageView(R.id.b2_imageView, dbHelper.getStatus("B2_1"));
        updateImageView(R.id.b5_imageView, dbHelper.getStatus("B5_1"));
        updateImageView(R.id.a3_imageView, dbHelper.getStatus("A3_1"));
        updateImageView(R.id.a6_imageView, dbHelper.getStatus("A6_1"));
        updateImageView(R.id.b3_imageView, dbHelper.getStatus("B3_1"));
        updateImageView(R.id.b6_imageView, dbHelper.getStatus("B6_1"));
        updateImageView(R.id.c1_imageView, dbHelper.getStatus("C1_1"));
        updateImageView(R.id.c3_imageView, dbHelper.getStatus("C3_1"));
        updateImageView(R.id.c5_imageView, dbHelper.getStatus("C5_1"));
        updateImageView(R.id.c7_imageView, dbHelper.getStatus("C7_1"));
        updateImageView(R.id.c2_imageView, dbHelper.getStatus("C2_1"));
        updateImageView(R.id.c4_imageView, dbHelper.getStatus("C4_1"));
        updateImageView(R.id.c6_imageView, dbHelper.getStatus("C6_1"));
        updateImageView(R.id.c8_imageView, dbHelper.getStatus("C8_1"));
    }

    private void updateImageView(int imageViewId, int status) {
        ImageView imageView = findViewById(imageViewId);
        if (imageView != null) {
            if (status == 1) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String returnedZoneCode = data.getStringExtra("returnedZoneCode");
        }
    }
}
