// تحديد الحزمة الخاصة بالشاشة الرئيسية
package com.example.finalprojectshiraz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// لتفعيل عرض Edge-To-Edge (الشاشة كاملة)
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// استيراد كلاس Animal (يمثل جدول الحيوانات في قاعدة البيانات)
import com.example.finalprojectshiraz.data.AnimalTable.Animal;

// استيراد Adapter المخصص لعرض الحيوانات داخل ListView
import com.example.finalprojectshiraz.data.AnimalTable.MyAnimalAdapter;

// استيراد قاعدة البيانات المحلية Room
import com.example.finalprojectshiraz.data.AppDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * شاشة الصفحة الرئيسية HomeScreen
 * وظيفتها:
 * - عرض قائمة الحيوانات
 * - التنقل بين الشاشات (إضافة، تبني، تقارير، إعدادات)
 */
public class HomeScreen extends AppCompatActivity {

    // أزرار التنقل
    private Button btn3;          // زر إضافة حيوان
    private Button btnReport;     // زر التقارير
    private Button btnLocation;   // زر الموقع (غير مستخدم حالياً)
    private Button btnAdoption;   // زر التبني

    // عناصر نصية
    private TextView tvH;
    private TextView tv13;
    private TextView tvLocation;

    // قائمة اختيار نوع الحيوان (غير مستخدمة حالياً)
    private Spinner spnrAnimalType;

    // زر عائم لإضافة حيوان (غير مستخدم حالياً)
    private FloatingActionButton fabAddAnimal;

    // ListView لعرض الحيوانات
    private ListView lstvAnimals;

    // Adapter لربط البيانات بالـ ListView
    private MyAnimalAdapter adapterAnimals;

    // أيقونة الإعدادات
    private ImageView settingsVector;

    /**
     * onCreate
     * تُستدعى أول مرة عند فتح الشاشة
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // تفعيل العرض بالحواف (Edge-To-Edge)
        EdgeToEdge.enable(this);

        // ربط ملف التصميم XML
        setContentView(R.layout.activity_home_screen);

        // ربط الـ ListView من XML
        lstvAnimals = findViewById(R.id.lstvAnimals);

        // إنشاء Adapter جديد
        adapterAnimals = new MyAnimalAdapter(this, R.layout.task_item_layout);

        // ربط الـ Adapter بالـ ListView
        lstvAnimals.setAdapter(adapterAnimals);

        /**
         * منع تداخل المحتوى مع شريط الحالة وشريط التنقل
         */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    // إضافة Padding حسب حجم أشرطة النظام
                    v.setPadding(systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        // ربط بقية عناصر الواجهة
        btnReport = findViewById(R.id.btnReport);
        btnLocation = findViewById(R.id.btnLocation);
        btnAdoption = findViewById(R.id.btnAdoption);
        btn3 = findViewById(R.id.btn3);
        tvH = findViewById(R.id.tvH);
        settingsVector = findViewById(R.id.settingsVector);

        /**
         * عند الضغط على زر إضافة حيوان
         * الانتقال إلى شاشة AnimalDetails
         */
        btn3.setOnClickListener(view -> {
            Intent intent =
                    new Intent(HomeScreen.this, AnimalDetails.class);
            startActivity(intent);
        });

        /**
         * عند الضغط على زر التقارير
         */

        /**
         * عند الضغط على زر التبني
         */
        btnAdoption.setOnClickListener(view -> {
            Intent intent =
                    new Intent(HomeScreen.this, Adoption.class);
            startActivity(intent);
        });

        /**
         * عند الضغط على أيقونة الإعدادات
         */
        settingsVector.setOnClickListener(view -> {
            Intent intent =
                    new Intent(HomeScreen.this, Settings.class);
            startActivity(intent);
        });
    }

    /**
     * onResume
     * تُستدعى عند الرجوع إلى الشاشة
     * لتحديث البيانات المعروضة في القائمة
     */
    @Override
    protected void onResume() {

        super.onResume();

        // جلب جميع الحيوانات من قاعدة البيانات
        List<Animal> allAnimal =
                AppDatabase.getDB(this)
                        .animalQuery()
                        .getAllAnimal();

        // تنظيف البيانات القديمة من الـ Adapter
        adapterAnimals.clear();

        // إضافة البيانات الجديدة
        adapterAnimals.addAll(allAnimal);

        // تحديث العرض في ListView
        adapterAnimals.notifyDataSetChanged();
    }
}