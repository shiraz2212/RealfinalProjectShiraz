package com.example.finalprojectshiraz;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

import com.example.finalprojectshiraz.data.AppDatabase;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.annotations.NonNull;

public class Signup extends AppCompatActivity {
    private Button btnAccount, btnGoogle, btmFacebook, btnLogin2;
    private TextView tvCreate, tvName, tvMass, tvPass, tvPhone, tvOr, tvEmail, tvLog;
    private EditText etMail, etName, etPhone, etAddress, editTextText, etPassword2, emailAddress;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
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
        tvLog = findViewById(R.id.tvLogIn);
        etMail = findViewById(R.id.etMail);
        emailAddress = findViewById(R.id.emailAddress);
        editTextText = findViewById(R.id.editTextText);
        etPassword2 = findViewById(R.id.etPassword2);
        btnLogin2 = findViewById(R.id.btnLogin2);

        btnAccount = findViewById(R.id.btnAccount);


        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // عند الضغط على زر إنشاء الحساب → التحقق من صحة الحقول
                if (validateFields()) {
                    // إذا كانت الحقول صحيحة → الانتقال للشاشة الرئيسية
                    Intent intent = new Intent(Signup.this, HomeScreen.class);
                    startActivity(intent);
                } else {
                    // إذا كانت هناك حقول ناقصة أو غير صحيحة → عرض رسالة للمستخدم
                    Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // عند الضغط على زر تسجيل الدخول → الانتقال لشاشة LogIn
                Intent intent = new Intent(Signup.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    /**
     * دالة للتحقق من صحة جميع الحقول في شاشة التسجيل
     * @return true إذا كانت كل الحقول صحيحة، false إذا كانت هناك أخطاء
     */
    public boolean validateFields() {
        boolean isValid = true;
        // قراءة القيم من الحقول
        String name = etMail.getText().toString().trim();
        String email = emailAddress.getText().toString().trim();
        String password = editTextText.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        // التحقق من الاسم
        if (name.isEmpty()) {
            etMail.setError("Name is required");
            isValid = false;
        } else {
            etMail.setError(null);
        }

        // التحقق من البريد الإلكتروني
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Valid email is required");
            isValid = false;
        } else {
            emailAddress.setError(null);
        }

        // التحقق من كلمة المرور
        if (password.isEmpty() || password.length() < 8) {
            editTextText.setError("Password is required and must be at least 8 characters long");
            isValid = false;
        } else {
            editTextText.setError(null);
        }

        // التحقق من تطابق كلمة المرور
        if (password2.isEmpty() || !password2.equals(password)) {
            etPassword2.setError("Passwords do not match");
            isValid = false;
        } else {
            etPassword2.setError(null);
        }
        // إذا كانت جميع الحقول صحيحة
        if (isValid) {
            // إنشاء كائن مستخدم MyProfile
            MyProfile profile = new MyProfile();
            profile.setFullName(name);
            profile.setMail(email); // تحقق إذا كان اسم الدالة setMail أو setEmail
            profile.setPass(password);

            // تسجيل المستخدم في Firebase Authentication
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // إذا تم التسجيل بنجاح → حفظ البيانات في قاعدة البيانات المحلية
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // الحصول على قاعدة البيانات
                                AppDatabase db = AppDatabase.getDB(Signup.this);
                                // حفظ بيانات المستخدم في جدول MyProfile
                                db.getProfile().insert(profile);
                                // حفظ بيانات المستخدم على Firebase (وظيفة saveUser)
                                saveUser(profile);

                                // 2. Update UI on the main thread
                                // تحديث واجهة المستخدم على Main Thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Signup.this, "Sign up Succeeded", Toast.LENGTH_SHORT).show();
                                        finish(); // إغلاق شاشة التسجيل بعد النجاح
                                    }
                                });
                            }
                        }).start();
                    } else {
                        // إذا فشل التسجيل → عرض رسالة خطأ للمستخدم
                        Toast.makeText(Signup.this, "Signing up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        emailAddress.setError(task.getException().getMessage());
                    }
                }
            });
        }
        return isValid;
    }



    public void saveUser(MyProfile user) {// الحصول على مرجع إلى عقدة "users" في قاعدة البيانات

        // تهيئة Firebase Realtime Database    //مؤشر لقاعدة البيانات
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
// ‏مؤشر لجدول المستعملين
        DatabaseReference usersRef = database.child("profiles");
        // إنشاء مفتاح فريد للمستخدم الجديد
        DatabaseReference newUserRef = usersRef.push();
        // تعيين معرف المستخدم في كائن MyUser
        user.setUserId(newUserRef.getKey());
        // حفظ بيانات المستخدم في قاعدة البيانات
        //اضافة كائن "لمجموعة" المستعملين ومعالج حدث لفحص نجاح المطلوب
        newUserRef.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Signup.this, "Succeeded to add User",  Toast.LENGTH_SHORT).show();
                        finish();




                        // تم حفظ البيانات بنجاح
                        Log.d(TAG, "تم حفظ المستخدم بنجاح: " + user.getUserId());
                        // تحديث واجهة المستخدم أو تنفيذ إجراءات أخرى
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // معالجة الأخطاء
                        Log.e(TAG, "خطأ في حفظ المستخدم: " + e.getMessage(), e);
                        Toast.makeText(Signup.this, "Failed to add User", Toast.LENGTH_SHORT).show();
                        // عرض رسالة خطأ للمستخدم

                    }
                });
    }





}
