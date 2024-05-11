package com.example.parkpal1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.HashMap;

public class ZoneActivity extends AppCompatActivity {

    private double lat;
    private double lon;
    private LottieAnimationView animationView;
    private TextView zoneTextView, codeTextView;
    private Button getDirectionsButton, reserveButton;
    private Button backButton;
    private DatabaseHelper dbHelper;
    private HashMap<String, CountDownTimer> timerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_zone);

        animationView=findViewById(R.id.animation_view);
        zoneTextView = findViewById(R.id.zone_text);
        codeTextView = findViewById(R.id.code_text);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        backButton = findViewById(R.id.backButton);
        reserveButton = findViewById(R.id.reserveButton);
        dbHelper = new DatabaseHelper(this);
        timerHashMap = new HashMap<>();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String reservedZoneCode = sharedPreferences.getString("reservedZoneCode", "");
        if (!reservedZoneCode.isEmpty()) {
            startReservationTimer(String.valueOf(sharedPreferences.getLong("countDownMillis", 0)));
        } else {
            //
        }

        Intent intent = getIntent();
        if (intent != null) {
            lat = intent.getDoubleExtra("lat", 0.0);
            lon = intent.getDoubleExtra("lon", 0.0);
            String zone = intent.getStringExtra("zone");
            if (zone != null) {
                dbHelper.addParkingZone(zone);
                zoneTextView.setText(zone);
                String zoneCode = dbHelper.getCode(zone);
            }
            String zoneCode = dbHelper.getCode(zone);
            if (zoneCode == null) {
                dbHelper.addParkingZone(zone);
                zoneCode = dbHelper.getCode(zone);
            }
            codeTextView.setText(zoneCode);
        }
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent(); // Move the declaration inside onClick
                String zone = intent.getStringExtra("zone");
                reserveParkingSpace(zone);
                startReservationTimer(zone);
                Toast.makeText(ZoneActivity.this, "Reserved successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceActivity = getIntent().getStringExtra("sourceActivity");
                Intent intent;
                if (sourceActivity != null && sourceActivity.equals("Street2Activity")) {
                    intent = new Intent(ZoneActivity.this, Street2Activity.class);
                } else if (sourceActivity != null && sourceActivity.equals("Street1Activity")) {
                    intent = new Intent(ZoneActivity.this, Street1Activity.class);
                } else {

                    intent = new Intent(ZoneActivity.this, SimpleMapActivity.class);
                    Log.d("ZoneActivity", "Source Activity: " + sourceActivity);
                }
                startActivity(intent);
            }
        });
        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ZoneActivity", "Lat: " + lat + ", Lon: " + lon);

                Intent intent = new Intent(ZoneActivity.this, GetDirectionActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });
    }


    private void reserveParkingSpace(String zone) {
        boolean reservationSuccessful = dbHelper.reserveParkingSpace(zone);
        if (reservationSuccessful) {
            Toast.makeText(this, "Reserved succesfully!", Toast.LENGTH_SHORT).show();
            startReservationTimer(zone);

        } else {
            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startReservationTimer(final String zone) {
        CountDownTimer existingTimer = timerHashMap.get(zone);
        if (existingTimer != null) {
            existingTimer.cancel();
        }
        CountDownTimer countDownTimer = new CountDownTimer(90000, 1000) { // 1.5 dakika (90 saniye)
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                String timeRemaining = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);

                if (millisUntilFinished <= 30000 && millisUntilFinished > 29000) {
                    Toast.makeText(ZoneActivity.this, "Son 30 saniye!", Toast.LENGTH_SHORT).show();
                } else if (millisUntilFinished <= 3000) {
                    Toast.makeText(ZoneActivity.this, "Son 3 saniye!", Toast.LENGTH_SHORT).show();
                }
            }
            public void onFinish() {
                dbHelper.cancelParkingReservation(zone);
                Toast.makeText(ZoneActivity.this, "Time is up.", Toast.LENGTH_SHORT).show();
                String newCode = dbHelper.generateRandomCode();
                dbHelper.updateCode(zone, newCode);
            }
        }.start();
        timerHashMap.put(zone, countDownTimer);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("returnedZoneCode", codeTextView.getText().toString());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}