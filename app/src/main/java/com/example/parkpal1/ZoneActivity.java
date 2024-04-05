package com.example.parkpal1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ZoneActivity extends AppCompatActivity {

    private TextView zoneTextView, codeTextView;
    private Button getDirectionsButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_zone);

        // XML dosyasındaki bileşenleri Java'da tanımla
        zoneTextView = findViewById(R.id.zone_text);
        codeTextView = findViewById(R.id.code_text);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        backButton=findViewById(R.id.backButton);
        Intent intent = getIntent();
        if (intent != null) {
            String zone = intent.getStringExtra("zone");
            if (zone != null) {
                // Zone değerini zoneTextView'e ata
                zoneTextView.setText(zone);
            }
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZoneActivity.this, Street2Activity.class);
                startActivity(intent);
            }
        });
        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZoneActivity.this, GetDirectionActivity.class);
                startActivity(intent);
            }
        });


    }
}
