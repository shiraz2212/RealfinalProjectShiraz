package com.example.finalprojectshiraz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.finalprojectshiraz.data.AnimalTable.AirPlaneReceiver;
import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AppDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AnimalDetails extends AppCompatActivity {
    private AirPlaneReceiver systemEventIsReceiver;
    private Button btnSubmit;
    private TextInputLayout tilName, tilAge, tilGender, tilBreed, tilLocation;
    private TextInputEditText etLocation;
    private ImageView ivAnimalImage;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> pickImage;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_details);

        btnSubmit = findViewById(R.id.btnSubmit);
        tilName = findViewById(R.id.tilName);
        tilAge = findViewById(R.id.tilAge);
        tilGender = findViewById(R.id.tilGender);
        tilBreed = findViewById(R.id.tilBreed);
        tilLocation = findViewById(R.id.tilLocation);
        etLocation = findViewById(R.id.etLocation);
        ivAnimalImage = findViewById(R.id.ivAnimalImage);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                selectedImageUri = result;
                ivAnimalImage.setImageURI(result);
                ivAnimalImage.setVisibility(View.VISIBLE);
            }
        });

        ivAnimalImage.setOnClickListener(v -> pickImage.launch("image/*"));

        tilLocation.setEndIconOnClickListener(v -> {
            getLastLocation();
        });

        btnSubmit.setOnClickListener(v -> validateAndSave());

        systemEventIsReceiver = new AirPlaneReceiver(btnSubmit);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateLocationUI(location);
            } else {
                Toast.makeText(AnimalDetails.this, "Unable to find location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocationUI(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                etLocation.setText(address);
            } else {
                etLocation.setText(location.getLatitude() + ", " + location.getLongitude());
            }
        } catch (IOException e) {
            etLocation.setText(location.getLatitude() + ", " + location.getLongitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String convertImageToString(Uri uri) {
        if (uri == null) return "";
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) return "";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    private void validateAndSave() {
        String name = tilName.getEditText().getText().toString().trim();
        String age = tilAge.getEditText().getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "يرجى تعبئة الحقول الأساسية", Toast.LENGTH_SHORT).show();
            return;
        }

        Animal a = new Animal();
        a.setName(name);
        a.setAge(age);
        a.setGender(tilGender.getEditText().getText().toString().trim());
        a.setBreed(tilBreed.getEditText().getText().toString().trim());
        a.setLocation(location.isEmpty() ? "N/A" : location);
        a.setImage(convertImageToString(selectedImageUri));

        btnSubmit.setEnabled(false);
        saveToFirebase(a);
    }

    private void saveToFirebase(Animal a) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        String key = dbRef.push().getKey();
        a.setKeyid(key);

        dbRef.child(key).setValue(a).addOnSuccessListener(unused -> {
            new Thread(() -> {
                AppDatabase.getDB(getApplicationContext()).animalQuery().insert(a);
                runOnUiThread(() -> {
                    Toast.makeText(this, "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        }).addOnFailureListener(e -> {
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "فشل الحفظ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(systemEventIsReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(systemEventIsReceiver, filter);
            }
        } catch (Exception e) {}
    }

    @Override
    protected void onStop() {
        super.onStop();
        try { unregisterReceiver(systemEventIsReceiver); } catch (Exception e) {}
    }
}
