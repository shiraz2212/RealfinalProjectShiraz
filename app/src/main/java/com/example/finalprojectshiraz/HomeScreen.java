package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshiraz.data.AnimalTable.MyAnimalAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeScreen extends AppCompatActivity {
    private Button btn3;
    private TextView tvH;
    private TextView tv11;
    private TextView tv13;
    private Button btnReport;
    private Button btnLocation;
    private Button btnAdoption;
    private TextView tvLocation;

    private Spinner spnrAnimalType;
    private FloatingActionButton fabAddAnimal;
    private ListView lstvAnimals;
    private MyAnimalAdapter adapter;





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

        adapter = new MyAnimalAdapter(this, R.layout.task_item_layout);
        lstvAnimals.setAdapter(adapter);
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
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the adapter data when the activity resumes
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}