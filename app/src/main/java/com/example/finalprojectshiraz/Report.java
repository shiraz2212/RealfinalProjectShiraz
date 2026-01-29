package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Report extends AppCompatActivity {
    private Button btnReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Report Stray Animal");
        }
    }


}

