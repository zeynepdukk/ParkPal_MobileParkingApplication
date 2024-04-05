package com.example.parkpal1;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.SharedPreferences;

public class Street2Activity extends AppCompatActivity {
    private String userRole;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street2);

        dbHelper = new DatabaseHelper(this);

        // Park yeri durumunu al ve buna göre ImageView'ları güncelle
        updateImageViews();

        // Get userRole from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "Driver"); // Default is "Driver"

        Button parking2_backbutton = findViewById(R.id.parking2_backbutton);
        Button parking2_a1 = findViewById(R.id.parking2_a1);
        Button parking2_b1 = findViewById(R.id.parking2_b1);
        Button parking2_a2 = findViewById(R.id.parking2_a2);
        Button parking2_b2 = findViewById(R.id.parking2_b2);
        Button parking2_a3 = findViewById(R.id.parking2_a3);
        Button parking2_b3 = findViewById(R.id.parking2_b3);
        Button parking2_a4 = findViewById(R.id.parking2_a4);
        Button parking2_b4 = findViewById(R.id.parking2_b4);



        parking2_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Street2Activity.this, SimpleMapActivity.class);
                startActivity(intent);
            }
        });

        // Butonlara tıklama olaylarını atanması için fonksiyon çağrısı
        setButtonClickListener(parking2_a1, "A1");
        setButtonClickListener(parking2_b1, "B1");
        setButtonClickListener(parking2_a2, "A2");
        setButtonClickListener(parking2_b2, "B2");
        setButtonClickListener(parking2_a3, "A3");
        setButtonClickListener(parking2_b3, "B3");
        setButtonClickListener(parking2_a4, "A4");
        setButtonClickListener(parking2_b4, "B4");
    }

    // Butonlara tıklama olaylarını atayan fonksiyon
    private void setButtonClickListener(Button button, final String zone) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openZoneActivity(zone);
            }
        });
    }

    // ZoneActivity'ye yönlendiren fonksiyon
    private void openZoneActivity(String zone) {
        Class<?> targetActivity;
        if (userRole.equals("Driver")) {
            targetActivity = ZoneActivity.class;
        } else {
            targetActivity = ZoneActivityA.class;
        }
        Intent intent = new Intent(Street2Activity.this, targetActivity);
        intent.putExtra("zone", zone);
        startActivity(intent);
    }
    // Park yeri durumuna göre ImageView'ları güncelleyen metot
    private void updateImageViews() {
        updateImageView(R.id.a1_imageView, dbHelper.getStatus("A1"));
        updateImageView(R.id.b1_imageView, dbHelper.getStatus("B1"));
        updateImageView(R.id.a2_imageView, dbHelper.getStatus("A2"));
        updateImageView(R.id.b2_imageView, dbHelper.getStatus("B2"));
        updateImageView(R.id.a3_imageView, dbHelper.getStatus("A3"));
        updateImageView(R.id.b3_imageView, dbHelper.getStatus("B3"));
        updateImageView(R.id.a4_imageView, dbHelper.getStatus("A4"));
        updateImageView(R.id.b4_imageView, dbHelper.getStatus("B4"));
    }

    private void updateImageView(int imageViewId, int status) {
        ImageView imageView = findViewById(imageViewId);
        if (status == 1) {
            imageView.setVisibility(View.VISIBLE); // Park yeri dolu ise görünür yap
        } else {
            imageView.setVisibility(View.INVISIBLE); // Park yeri boş ise görünmez yap
        }
    }
}