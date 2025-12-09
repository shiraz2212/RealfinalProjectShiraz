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
import androidx.room.Room;

import com.example.finalprojectshiraz.data.AppDatabase;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;

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
                if (validateInputs()) {
                    // Only proceed to HomeScreen if inputs are valid
                    Intent intent = new Intent(LogIn.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean validateInputs() {
        // Get the input values
        String email = etEnterEmail.getText().toString().trim();
        String password = tvEnterPass.getText().toString().trim();
        boolean isValid = true;



        // Validate email
        if (email.isEmpty()) {
            etEnterEmail.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            etEnterEmail.setError("Please enter a valid email address");
            isValid = false;
        } else {
            etEnterEmail.setError(null);
        }

        // Validate password
        if (password.isEmpty()) {
            tvEnterPass.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            tvEnterPass.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tvEnterPass.setError(null);
        }
if (isValid) {
    AppDatabase db = AppDatabase.getDB(getApplicationContext());
            MyProfile profile = db.getProfile().checkEmail(email);
    if (profile != null && profile.getPassw().equals(password)) {
        Intent intent = new Intent(LogIn.this, HomeScreen.class);
        startActivity(intent);
    } else {
        Toast.makeText(LogIn.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }
}
        return isValid;
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Simple email validation
        return email.contains("@") && 
               email.contains(".") && 
               email.indexOf("@") > 0 && 
               email.lastIndexOf(".") > email.indexOf("@");

    }

}