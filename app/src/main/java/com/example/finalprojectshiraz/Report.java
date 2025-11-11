package com.example.finalprojectshiraz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Report Stray Animal");
        }
    }
}