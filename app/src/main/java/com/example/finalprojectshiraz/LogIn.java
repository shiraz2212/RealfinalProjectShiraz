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

public class LogIn extends AppCompatActivity {
private TextView tvLogin;
private TextView tvText;
private TextView tvEmailAddress;
private EditText etEnterEmail;
private TextView tvPassword;
private TextView tvEnterPass;
private TextView tvF;
private Button btnLogin;
private Button btnSignUP;
    @Override

    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvLogin = findViewById(R.id.tvLogin);
        tvText = findViewById(R.id.tvText);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        etEnterEmail = findViewById(R.id.etEnterEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvEnterPass = findViewById(R.id.tvEnterPass);
        tvF = findViewById(R.id.tvF);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUP = findViewById(R.id.btnSignUP);

        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, Signup.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }

}