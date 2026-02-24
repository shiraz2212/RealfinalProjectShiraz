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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AnimalTable.MyAnimalAdapter;
import com.example.finalprojectshiraz.data.AppDatabase;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
/**
 * شاشة الصفحة الرئيسية HomeScreen
 * تعرض قائمة الحيوانات، أزرار للتنقل إلى شاشات أخرى، وواجهة Edge-to-Edge
 */
public class HomeScreen extends AppCompatActivity {
    private Button btn3;
    private TextView tvH;

    private TextView tv13;
    private Button btnReport;
    private Button btnLocation;
    private Button btnAdoption;
    private TextView tvLocation;

    private Spinner spnrAnimalType;
    private FloatingActionButton fabAddAnimal;//إضافة حيوان جديد

private ListView lstvAnimals;//القائمة اللي بتعرض الحيوانات
private MyAnimalAdapter adapterAnimals;//ربط البيانات بالواجهة
    private ImageView settingsVector;



    /**
     * الدالة onCreate: تُستدعى أول مرة عند فتح الشاشة
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate → أول دالة تُستدعى عند فتح الشاشة
        super.onCreate(savedInstanceState);//لازم تستدعيها عشان Activity تشتغل بشكل طبيعي
        EdgeToEdge.enable(this);//لتمكين عرض الشاشة بالكامل خلف الـ status bar و navigation bar
        setContentView(R.layout.activity_home_screen);//كل العناصر اللي عرّفناها (Button, TextView, ListView) موجودة في هذا XML
        lstvAnimals = findViewById(R.id.lstvAnimals);
        adapterAnimals = new MyAnimalAdapter(this, R.layout.task_item_layout);//هون عم تنشئ Adapter جديد من الكلاس MyAnimalAdapter.
        lstvAnimals.setAdapter(adapterAnimals);//
        // السطرين هدول وظيفتهم ربط البيانات مع الـ ListView حتى تنعرض على الشاشة
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { ////الهدف: منع تداخل المحتوى مع أزرار النظام
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());//// -حصل على أبعاد شريط الحالة وشريط التنقل
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);//;// ضيف Padding حسب أبعاد النظام حتى لا يغطي شريط النظام محتوى الشاشة
            return insets;
//يهيئ Activity
//
//يجعلها Edge-to-Edge (عرض كامل للشاشة)
//
//يضمن أن كل العناصر داخل الشاشة ما تتداخل مع status/navigation bars
        });
        lstvAnimals=findViewById(R.id.lstvAnimals);
        btnReport = findViewById(R.id.btnReport);
        btnLocation = findViewById(R.id.btnLocation);
        btnAdoption = findViewById(R.id.btnAdoption);
        btn3 = findViewById(R.id.btn3);
        tvH = findViewById(R.id.tvH);
        settingsVector=findViewById(R.id.settingsVector);



        // عند الضغط على زر btn3 → فتح شاشة إضافة حيوان جديد
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, AnimalDetails.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر btnReport → فتح شاشة التقارير
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Report.class);
                startActivity(intent);
            }
        });


        // عند الضغط على زر btnAdoption → فتح شاشة التبني
        btnAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Adoption.class);
                startActivity(intent);
            }
        });

        // عند الضغط على أيقونة الإعدادات → فتح شاشة الإعدادات
        settingsVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Settings.class);
                startActivity(intent);
            }
        });
        // عند الضغط على أيقونة الإعدادات → فتح شاشة الإعدادات
        settingsVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Settings.class);
                startActivity(intent);
            }
        });
    }

    /**
     * دالة onResume: تُستدعى عند العودة للشاشة
     * لتحديث قائمة الحيوانات بعرض البيانات الحالية من قاعدة البيانات
     */
    @Override
    protected void onResume() {
        super.onResume();
        //استخراج المعطيات من قاعدة البيانات
        List<Animal> allAnimal= AppDatabase.getDB(this).animalQuery().getAllAnimal();
        //تنظيف المنسق من جميع المعطيات السابقة
        adapterAnimals.clear();

        // إضافة جميع الحيوانات الجديدة
        adapterAnimals.addAll(allAnimal);

        // إعلام الـ ListView بتحديث البيانات
        adapterAnimals.notifyDataSetChanged();
    }
}