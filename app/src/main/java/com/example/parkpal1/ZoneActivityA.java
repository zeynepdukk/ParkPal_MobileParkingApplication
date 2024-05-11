package com.example.parkpal1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ZoneActivityA extends AppCompatActivity {

    private TextView zoneText;
    private Button emptyButton, backButton, checkCode;
    private DatabaseHelper dbHelper;
    private EditText codeEditText;
    private String zone;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_attendant);

        zoneText = findViewById(R.id.zone_text);
        codeEditText = findViewById(R.id.codeEditText);
        emptyButton = findViewById(R.id.emptyButton);
        backButton = findViewById(R.id.backButton);
        checkCode = findViewById(R.id.checkCode);
        dbHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zone = extras.getString("zone");
            zoneText.setText(zone);
            status = dbHelper.getStatus(zone);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceActivity = getIntent().getStringExtra("sourceActivity");

                Intent intent;
                if (sourceActivity != null && sourceActivity.equals("Street2Activity")) {
                    intent = new Intent(ZoneActivityA.this, Street2Activity.class);
                } else if (sourceActivity != null && sourceActivity.equals("Street1Activity")) {
                    intent = new Intent(ZoneActivityA.this, Street1Activity.class);
                } else {
                    intent = new Intent(ZoneActivityA.this, SimpleMapActivity.class);
                    Log.d("ZoneActivity", "Source Activity: " + sourceActivity);
                }
                startActivity(intent);
            }
        });

        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = dbHelper.getStatus(zone);
                if (status == 1) {
                    status = 0;
                    dbHelper.updateStatus(zone, status);
                    String newCode = dbHelper.generateRandomCode();
                    dbHelper.updateCode(zone, newCode);
                    Toast.makeText(ZoneActivityA.this, "Zone marked empty successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ZoneActivityA.this, "Already empty zone.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(enteredCode)) {
                    Toast.makeText(ZoneActivityA.this, "Please enter the code.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String zoneCode = dbHelper.getCode(zone);
                if (enteredCode.equals(zoneCode)) {
                    status = 1;
                    dbHelper.updateStatus(zone, status);
                    Toast.makeText(ZoneActivityA.this, "Zone marked full successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ZoneActivityA.this, "Wrong code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}