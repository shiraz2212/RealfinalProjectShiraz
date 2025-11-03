package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity
{
    private Button btnAccount,btnGoogle,btmFacebook;
    private TextView tvCreate,tvName, tvMass, tvPass, tvPhone, tvOr,tvEmail,tvSUW,tvLog;
    private EditText etMail, etName, etPhone, etAddress, etPassword, etPassword2,emailAddress;



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
            etPassword = findViewById(R.id.etPassword);
            etPassword2 = findViewById(R.id.etPassword2);
            emailAddress = findViewById(R.id.emailAddress);

        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}