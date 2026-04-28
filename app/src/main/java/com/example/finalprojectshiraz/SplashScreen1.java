package com.example.finalprojectshiraz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** מاشتغل
 * شاشة البداية SplashScreen1
 * تعرض صورة ونص عند فتح التطبيق لأول مرة
 * ثم تنتقل تلقائيًا إلى شاشة تسجيل الدخول LogIn بعد مدة قصيرة
 */
public class SplashScreen1 extends AppCompatActivity {

    // عناصر واجهة المستخدم
    private ImageView MileyImage; // صورة البداية
    private TextView tvSplash;    // نص البداية

    /**
     * onCreate
     * تُستدعى عند فتح الشاشة لأول مرة
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // تفعيل Edge-to-Edge لعرض الشاشة بالكامل خلف شريط الحالة وشريط التنقل
        EdgeToEdge.enable(this);

        // ربط الشاشة بملف XML
        setContentView(R.layout.activity_splash_screen1);

        // تأخير لمدة 3 ثواني ثم الانتقال إلى شاشة تسجيل الدخول
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen1.this, LogIn.class);
            startActivity(intent); // الانتقال إلى LogIn
            finish(); // إغلاق SplashScreen1 حتى لا يعود المستخدم إليها بالرجوع
        }, 3000); // 3000 ملي ثانية = 3 ثوانٍ

        // ضبط Edge-to-Edge لضمان عدم تداخل عناصر الشاشة مع أشرطة النظام
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}