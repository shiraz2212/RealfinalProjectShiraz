package com.example.finalprojectshiraz;

// استيراد الحزم اللازمة
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

import com.example.finalprojectshiraz.data.AppDatabase;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * شاشة تسجيل الدخول LogIn
 * تسمح للمستخدم بإدخال البريد الإلكتروني وكلمة المرور لتسجيل الدخول
 * تستخدم Firebase Authentication للتحقق من صحة بيانات الدخول
 */
public class LogIn extends AppCompatActivity {

    // عناصر واجهة المستخدم
    private TextView tvLogin, tvText, tvEmailAddress, tvPassword, tvEnterPass, tvF;
    private EditText etEnterEmail;
    private Button btnLogin, btnSignUP;

    /**
     * دالة onCreate: تعمل عند فتح الشاشة لأول مرة
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // تفعيل عرض Edge-to-Edge
        EdgeToEdge.enable(this);

        // ربط الشاشة بملف XML
        setContentView(R.layout.activity_log_in);

        // التحقق إذا كان هناك مستخدم مسجل بالفعل
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            // إذا كان المستخدم مسجل الدخول بالفعل → الانتقال مباشرة للصفحة الرئيسية
            Intent intent = new Intent(LogIn.this, HomeScreen.class);
            startActivity(intent);
        }

        // ضبط Edge-to-Edge لضمان عدم تداخل العناصر مع status/navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ربط عناصر الواجهة بالكود
        tvLogin = findViewById(R.id.tvLogin);
        tvText = findViewById(R.id.tvText);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        etEnterEmail = findViewById(R.id.etEnterEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvEnterPass = findViewById(R.id.tvEnterPass);
        tvF = findViewById(R.id.tvF);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUP = findViewById(R.id.btnSignUP);

        /**
         * زر التسجيل → فتح شاشة Signup
         */
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, Signup.class);
                startActivity(intent);
            }
        });

        /**
         * زر تسجيل الدخول → التحقق من صحة البيانات
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    /**
     * دالة للتحقق من صحة البيانات المدخلة
     * @return true إذا كانت البيانات صحيحة، false إذا كانت خاطئة
     */
    private boolean validateInputs() {
        // قراءة القيم من الحقول
        String email = etEnterEmail.getText().toString().trim();
        String password = tvEnterPass.getText().toString().trim();
        boolean isValid = true;

        // التحقق من البريد الإلكتروني
        if (email.isEmpty()) {
            etEnterEmail.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            etEnterEmail.setError("Please enter a valid email address");
            isValid = false;
        } else {
            etEnterEmail.setError(null);
        }

        // التحقق من كلمة المرور
        if (password.isEmpty()) {
            tvEnterPass.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            tvEnterPass.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tvEnterPass.setError(null);
        }

        // إذا كانت البيانات صحيحة → تسجيل الدخول عبر Firebase
        if (isValid) {
            signIn(email, password);
        }

        return isValid;
    }

    /**
     * دالة بسيطة للتحقق من صحة البريد الإلكتروني
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // تحقق بسيط من وجود @ و .
        return email.contains("@") &&
                email.contains(".") &&
                email.indexOf("@") > 0 &&
                email.lastIndexOf(".") > email.indexOf("@");
    }

    /**
     * دالة لتسجيل الدخول عبر Firebase Authentication
     */
    private void signIn(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // تسجيل الدخول ناجح → تحديث واجهة المستخدم
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // فشل تسجيل الدخول → عرض رسالة
                            Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * دالة لتحديث واجهة المستخدم بعد تسجيل الدخول
     * @param user المستخدم الحالي أو null إذا فشل تسجيل الدخول
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LogIn.this, HomeScreen.class);
            startActivity(intent);
            finish(); // إغلاق شاشة تسجيل الدخول
        }
    }
}