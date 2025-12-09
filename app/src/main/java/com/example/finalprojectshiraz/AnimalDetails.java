package com.example.finalprojectshiraz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class AnimalDetails extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputLayout tilName;
    private TextInputLayout tilAge;
    private TextInputLayout tilGender;
    private TextInputLayout tilBreed;

    private TextInputLayout tilVaccineDetails;
    private TextInputLayout tilNotes;
    private CheckBox cbVaccinated;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        btnSubmit = findViewById(R.id.btnSubmit);
        tilName = findViewById(R.id.tilName);
        tilAge = findViewById(R.id.tilAge);
        tilGender = findViewById(R.id.tilGender);
        tilBreed = findViewById(R.id.tilBreed);

        tilVaccineDetails = findViewById(R.id.tilVaccineDetails);
        tilNotes = findViewById(R.id.tilNotes);
        cbVaccinated = findViewById(R.id.cbVaccinated);
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                Intent intent = new Intent(AnimalDetails.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;
        // Extract values from form fields
        String name = tilName.getEditText().getText().toString().trim();
        String age = tilAge.getEditText().getText().toString().trim();
        String gender = tilGender.getEditText().getText().toString().trim();
        String breed = tilBreed.getEditText().getText().toString().trim();
        String vaccineDetails = tilVaccineDetails.getEditText().getText().toString().trim();
        String notes = tilNotes.getEditText().getText().toString().trim();
        boolean vaccinated = cbVaccinated.isChecked();

        // Validate each field
        if (name.isEmpty()) {
            isValid = false;
            tilName.setError("Name is required");
        } else {
            tilName.setError(null);
        }
        if (age.isEmpty()) {
            isValid = false;
            tilAge.setError("Age is required");
        } else {
            tilAge.setError(null);
        }
        if (gender.isEmpty()) {
            isValid = false;
            tilGender.setError("Gender is required");
        } else {
            tilGender.setError(null);
        }
        if (breed.isEmpty()) {
            isValid = false;
            tilBreed.setError("Breed is required");
        } else {
            tilBreed.setError(null);
        }
        if (vaccineDetails.isEmpty() && !vaccinated) {
            isValid = false;
            tilVaccineDetails.setError("Vaccine Details or Vaccinated must be provided");
        } else {
            tilVaccineDetails.setError(null);
        }

        return isValid;
    }


}