package com.example.finalprojectshiraz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.finalprojectshiraz.data.AppDatabase;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            Intent intent = new Intent(LogIn.this, HomeScreen.class);
            startActivity(intent);
        }

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
                validateInputs();
                // if () {
                // Only proceed to HomeScreen if inputs are valid

            }
            //  }
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
            signIn(email,password);
//            AppDatabase db = AppDatabase.getDB(getApplicationContext());
//            MyProfile profile = db.getProfile().checkEmail(email);
//            if (profile != null && profile.getPassw().equals(password)) {
//                Intent intent = new Intent(LogIn.this, HomeScreen.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(LogIn.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//            }
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


    private void signIn(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LogIn.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }


}