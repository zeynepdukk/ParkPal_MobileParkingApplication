package com.example.parkpal1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ZoneActivity extends AppCompatActivity {

    private double lat;
    private double lon;
    private TextView zoneTextView, codeTextView;
    private Button getDirectionsButton;
    private Button backButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_zone);

        // XML dosyasındaki bileşenleri Java'da tanımla
        zoneTextView = findViewById(R.id.zone_text);
        codeTextView = findViewById(R.id.code_text);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        backButton=findViewById(R.id.backButton);

        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        if (intent != null) {
            lat = intent.getDoubleExtra("lat", 0.0);
            lon = intent.getDoubleExtra("lon", 0.0);
            String zone = intent.getStringExtra("zone");
            if (zone != null) {
                // Zone değerini zoneTextView'e ata
                dbHelper.addParkingZone(zone);
                zoneTextView.setText(zone);
                String zoneCode = dbHelper.getCode(zone); // Park yeri kodunu al
            }
            String zoneCode = dbHelper.getCode(zone); // Park yeri kodunu al
            if (zoneCode == null) {
                dbHelper.addParkingZone(zone); // Eğer kod yoksa yeni bir park yeri ekleyerek kod oluştur
                zoneCode = dbHelper.getCode(zone); // Yeni oluşturulan kodu al
            }
            codeTextView.setText(zoneCode);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String sourceActivity = getIntent().getStringExtra("sourceActivity");
                if (sourceActivity != null && sourceActivity.equals("Street1Activity")) {
                    intent = new Intent(ZoneActivity.this, Street1Activity.class);
                } else {
                    intent = new Intent(ZoneActivity.this, Street2Activity.class);
                }
                startActivity(intent);
            }
        });
        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ZoneActivity", "Lat: " + lat + ", Lon: " + lon);

                Intent intent = new Intent(ZoneActivity.this, GetDirectionActivity.class);
                intent.putExtra("lat", lat); // ZoneActivity'den gelen lat değeri
                intent.putExtra("lon", lon); // ZoneActivity'den gelen lon değeri
                startActivity(intent);
            }
        });

    }
}