package com.example.finalprojectshiraz;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshiraz.R;
import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class Adoption extends AppCompatActivity {

    private Spinner spnAnimals;
    private EditText etAdopterName, etAdopterPhone, etAdopterNotes;
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adoption);

        // Connect UI elements
        spnAnimals = findViewById(R.id.spnAnimals);
        etAdopterName = findViewById(R.id.etAdopterName);
        etAdopterPhone = findViewById(R.id.etAdopterPhone);
        etAdopterNotes = findViewById(R.id.etAdopterNotes);
        Button btnSubmitAdoption = findViewById(R.id.btnSubmitAdoption);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAnimals();

        btnSubmitAdoption.setOnClickListener(v -> submitAdoptionRequest());
    }

    private void loadAnimals() {
        try {
            animalList = AppDatabase.getDB(this).animalQuery().getAllAnimal();
            
            List<String> animalNames = new ArrayList<>();
            if (animalList != null) {
                for (Animal animal : animalList) {
                    animalNames.add(animal.getName() + " (" + animal.getType() + ")");
                }
            }

            if (animalNames.isEmpty()) {
                animalNames.add("No animals available");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, animalNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnAnimals.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading animals", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitAdoptionRequest() {
        String name = etAdopterName.getText().toString().trim();
        String phone = etAdopterPhone.getText().toString().trim();
        String notes = etAdopterNotes.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in your name and phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (animalList == null || animalList.isEmpty()) {
            Toast.makeText(this, "No animal selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Logic for submitting adoption request
        String message = "Request for " + spnAnimals.getSelectedItem().toString() + " by " + name;
        if (!notes.isEmpty()) {
            message += "\nNotes: " + notes;
        }

        Toast.makeText(this, "Request Sent: " + message, Toast.LENGTH_LONG).show();
        finish();
    }
}
