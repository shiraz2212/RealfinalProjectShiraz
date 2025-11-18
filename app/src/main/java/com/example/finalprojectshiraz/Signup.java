package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity
{
    private Button btnAccount,btnGoogle,btmFacebook;
    private TextView tvCreate,tvName, tvMass, tvPass, tvPhone, tvOr,tvEmail,tvSUW,tvLog;
    private EditText etMail, etName, etPhone, etAddress, editTextText, etPassword2,emailAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

            });


            btnGoogle = findViewById(R.id.btnGoogle);
            btmFacebook = findViewById(R.id.btmFacebook);
            tvCreate = findViewById(R.id.tvCreate);
            tvName = findViewById(R.id.tvName);
            tvMass = findViewById(R.id.tvMass);
            tvPass = findViewById(R.id.tvPass);
            tvPhone = findViewById(R.id.tvPhone);
            tvOr = findViewById(R.id.tvOr);
            tvEmail = findViewById(R.id.tvEmail);
            tvSUW = findViewById(R.id.tvSUW);
            tvLog = findViewById(R.id.tvLogIn);
            etMail = findViewById(R.id.etMail);
            tvName = findViewById(R.id.tvName);
            tvPhone = findViewById(R.id.tvPhone);
            emailAddress = findViewById(R.id.emailAddress);
        editTextText = findViewById(R.id.editTextText);
            etPassword2 = findViewById(R.id.etPassword2);
            emailAddress = findViewById(R.id.emailAddress);

        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Intent intent = new Intent(Signup.this, HomeScreen.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validateFields() {
        boolean isValid = true;

        String name = etMail.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = editTextText.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        if (name.isEmpty()) {
            etMail.setError("Name is required");
            isValid = false;
        } else {
            etMail.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Valid email is required");
            isValid = false;
        } else {
            emailAddress.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            editTextText.setError("Password is required and must be at least 8 characters long");
            isValid = false;
        } else {
            editTextText.setError(null);
        }

        if (password2.isEmpty() || !password2.equals(password)) {
            etPassword2.setError("Passwords do not match");
            isValid = false;
        } else {
            etPassword2.setError(null);
        }

        return isValid;
    }
}