package com.example.parkpal1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ZoneActivityA extends AppCompatActivity {

    private TextView zoneText, codeText;
    private Button markButton, backButton;
    private DatabaseHelper dbHelper;

    private String zone;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_a);

        // View'leri tanımla
        zoneText = findViewById(R.id.zone_text);
        codeText = findViewById(R.id.code_text);
        markButton = findViewById(R.id.markButton);
        backButton = findViewById(R.id.backButton);

        dbHelper = new DatabaseHelper(this);

        // Geri butonuna tıklama olayını ata
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zone = extras.getString("zone");
            zoneText.setText(zone);
            status = dbHelper.getStatus(zone); // Park yeri doluluk durumunu al
        }

        // Park yeri doluluk durumuna göre markButton'un metnini güncelle
        updateMarkButton();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Street2Activity'e geri dön
                Intent intent = new Intent(ZoneActivityA.this, Street2Activity.class);
                startActivity(intent);
            }
        });
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Park yeri dolu ise boş yap, boş ise dolu yap
                if (status == 1) {
                    status = 0;
                } else {
                    status = 1;
                }
                // Doluluk durumunu güncelle
                dbHelper.updateStatus(zone, status);
                // Metni güncelle
                updateMarkButton();
            }
        });

        // Diğer işlemler devam ediyor...
    }
    private void updateMarkButton() {
        if (status == 1) {
            markButton.setText("Boşalt"); // Eğer park yeri dolu ise, "Boşalt" olarak değiştir
            showToast("Park yeri başarıyla dolu olarak işaretlendi.");
        } else {
            markButton.setText("Dolu"); // Eğer park yeri boş ise, "Dolu" olarak değiştir
            showToast("Park yeri başarıyla boş olarak işaretlendi.");
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
