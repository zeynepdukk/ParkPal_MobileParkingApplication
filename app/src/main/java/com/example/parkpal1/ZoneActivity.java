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
    private DatabaseHelper dbHelper;// Geri sayımı saklamak için değişken
    private HashMap<String, CountDownTimer> timerHashMap; // Park yeri için zamanlayıcıları depolamak için kullanılacak

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_zone);

        // XML dosyasındaki bileşenleri Java'da tanımla
        animationView=findViewById(R.id.animation_view);
        zoneTextView = findViewById(R.id.zone_text);
        codeTextView = findViewById(R.id.code_text);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        backButton = findViewById(R.id.backButton);
        reserveButton = findViewById(R.id.reserveButton);

        dbHelper = new DatabaseHelper(this);
        timerHashMap = new HashMap<>(); // HashMap'i başlat

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String reservedZoneCode = sharedPreferences.getString("reservedZoneCode", "");
        if (!reservedZoneCode.isEmpty()) {
            // Rezerve edilmiş bir park yeri varsa geri sayım başlat
            startReservationTimer(String.valueOf(sharedPreferences.getLong("countDownMillis", 0)));
        } else {
            // Rezerve edilmiş bir park yeri yoksa geri sayımı başlatma
            // Burada gerekirse başka bir işlem yapabilirsiniz
        }

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

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent(); // Move the declaration inside onClick
                String zone = intent.getStringExtra("zone");
                // Park yeri rezervasyonunu başlat
                reserveParkingSpace(zone);
                // Rezervasyon süresini başlat
                startReservationTimer(zone);
                // Kullanıcıya rezervasyon yapıldığını bildir
                Toast.makeText(ZoneActivity.this, "Park yeri rezerve edildi.", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceActivity = getIntent().getStringExtra("sourceActivity");

                Intent intent;
                // String sourceActivity = getIntent().getStringExtra("sourceActivity");
                if (sourceActivity != null && sourceActivity.equals("Street2Activity")) {
                    intent = new Intent(ZoneActivity.this, Street2Activity.class);
                } else if (sourceActivity != null && sourceActivity.equals("Street1Activity")) {
                    intent = new Intent(ZoneActivity.this, Street1Activity.class);
                } else {
                    // If the source activity is not specified or recognized,
                    // you can handle it based on your app's requirements.
                    // For example, you can navigate to the main activity.
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
                intent.putExtra("lat", lat); // ZoneActivity'den gelen lat değeri
                intent.putExtra("lon", lon); // ZoneActivity'den gelen lon değeri
                startActivity(intent);
            }
        });
    }


    private void reserveParkingSpace(String zone) {
        // Rezervasyon talebini işle
        boolean reservationSuccessful = dbHelper.reserveParkingSpace(zone);
        if (reservationSuccessful) {
            // Rezervasyon başarılıysa kullanıcıya bildirim göster
            Toast.makeText(this, "Park alanı başarıyla rezerve edildi!", Toast.LENGTH_SHORT).show();
            // Zamanlayıcıyı başlat
            startReservationTimer(zone);

        } else {
            // Rezervasyon başarısızsa kullanıcıya bildirim göster
            Toast.makeText(this, "Park alanı rezerve edilemedi. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startReservationTimer(final String zone) {
        // Daha önce başlatılmış bir geri sayım varsa, onu iptal et
        CountDownTimer existingTimer = timerHashMap.get(zone);
        if (existingTimer != null) {
            existingTimer.cancel();
        }

        // Yeni bir geri sayım başlat
        CountDownTimer countDownTimer = new CountDownTimer(90000, 1000) { // 1.5 dakika (90 saniye)
            public void onTick(long millisUntilFinished) {
                // Her saniyede bir işlem yap
                // Kalan süreyi ekrana güncelle
                long secondsRemaining = millisUntilFinished / 1000;
                String timeRemaining = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
                // Kalan süreyi kullanıcıya göster


                // Son 30 saniyede ise ve geri sayımın son 3 saniyesine geldiyse, toast gönder
                if (millisUntilFinished <= 30000 && millisUntilFinished > 29000) {
                    Toast.makeText(ZoneActivity.this, "Son 30 saniye!", Toast.LENGTH_SHORT).show();
                } else if (millisUntilFinished <= 3000) {
                    Toast.makeText(ZoneActivity.this, "Son 3 saniye!", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFinish() {
                // Süre dolduğunda işlem yap
                // Park alanını rezerve etme süresi dolduğunda, park alanını boş olarak işaretle
                dbHelper.cancelParkingReservation(zone);
                // Kullanıcıya sürenin dolduğuna dair bildirim göster
                Toast.makeText(ZoneActivity.this, "Rezervasyon süresi doldu. Park alanı boş olarak işaretlendi.", Toast.LENGTH_SHORT).show();
                // Kalan süreyi sıfırla

                // Rezerve edilen park yerlerinin kodlarını güncelle
                String newCode = dbHelper.generateRandomCode();
                dbHelper.updateCode(zone, newCode);
            }
        }.start();

        // Oluşturulan zamanlayıcıyı HashMap'e ekle
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