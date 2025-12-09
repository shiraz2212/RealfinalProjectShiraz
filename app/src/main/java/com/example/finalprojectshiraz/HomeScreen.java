package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {
    private Button btn3;
    private TextView tvH;
    private TextView tv11;
    private TextView tv13;
    private Button btnReport;
    private Button btnLocation;
    private Button btnAdoption;
    private TextView tvLocation;


//lk;k;lk;lk;l
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn3 = findViewById(R.id.btn3);
        tvH = findViewById(R.id.tvH);
        tv11 = findViewById(R.id.tv11);
        tv13 = findViewById(R.id.tv13);
        btnReport = findViewById(R.id.btnReport);
        btnLocation = findViewById(R.id.btnLocation);
        btnAdoption = findViewById(R.id.btnAdoption);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, AnimalDetails.class);
                startActivity(intent);
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Report.class);
                startActivity(intent);
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(HomeScreen.this, Map.class);
                startActivity(intent);
            }
        });
        btnAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Adoption.class);
                startActivity(intent);
            }
        });
    }
}