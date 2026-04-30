// تحديد الحزمة الخاصة بالشاشة الرئيسية
package com.example.finalprojectshiraz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// لتفعيل عرض Edge-To-Edge (الشاشة كاملة)
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * شاشة الصفحة الرئيسية HomeScreen
 */
public class HomeScreen extends AppCompatActivity {

    private Button btn3;          // زر إضافة حيوان
    private Button btnLocation;   
    private Button btnAdoption;   
    private TextView tvH;
    private ListView lstvAnimals;
    private MyAnimalAdapter adapterAnimals;
    private ImageView settingsVector;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        lstvAnimals = findViewById(R.id.lstvAnimals);
        adapterAnimals = new MyAnimalAdapter(this, R.layout.animal_item_layout);
        lstvAnimals.setAdapter(adapterAnimals);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLocation = findViewById(R.id.btnLocation);
        btnAdoption = findViewById(R.id.btnAdoption);
        btn3 = findViewById(R.id.btn3);
        tvH = findViewById(R.id.tvH);
        settingsVector = findViewById(R.id.settingsVector);

        btn3.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, AnimalDetails.class);
            startActivity(intent);
        });

        btnAdoption.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, Adoption.class);
            startActivity(intent);
        });

        // تشغيل مراقب البيانات من Firebase
        readAnimalsFromFirebase();
    }

    /**
     * جلب البيانات من Firebase وتحديث القائمة تلقائياً عند أي تغيير
     */
    private void readAnimalsFromFirebase() {
        FirebaseDatabase.getInstance().getReference("animals")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Animal> animalList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Animal a = ds.getValue(Animal.class);
                            if (a != null) {
                                animalList.add(a);
                            }
                        }
                        // تحديث القائمة
                        if (adapterAnimals != null) {
                            adapterAnimals.clear();
                            adapterAnimals.addAll(animalList);
                            adapterAnimals.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeScreen.this, "فشل جلب البيانات", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ملاحظة: قمنا بإزالة كود Room من هنا لأنه كان يمسح بيانات Firebase ويظهر بيانات قديمة بدون روابط صور.
        // الاعتماد الآن كلياً على Firebase لضمان ظهور الصور المرفوعة حديثاً.
    }
}
