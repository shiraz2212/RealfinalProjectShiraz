package com.example.finalprojectshiraz;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AppDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalDetails extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputLayout tilName;
    private TextInputLayout tilAge;
    private TextInputLayout tilGender;
    private TextInputLayout tilBreed;

    private TextInputLayout tilVaccineDetails;
    private TextInputLayout tilNotes;
    private CheckBox cbVaccinated;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        btnSubmit = findViewById(R.id.btnSubmit);
        tilName = findViewById(R.id.tilName);
        tilAge = findViewById(R.id.tilAge);
        tilGender = findViewById(R.id.tilGender);
        tilBreed = findViewById(R.id.tilBreed);

        tilVaccineDetails = findViewById(R.id.tilVaccineDetails);
        tilNotes = findViewById(R.id.tilNotes);
        cbVaccinated = findViewById(R.id.cbVaccinated);
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                Intent intent = new Intent(AnimalDetails.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }

    /**
     * تتحقق من صحة استخدام النموذج.
     *
     * @return إذا كانت كل الحقول صحيحة، فإنها صحيحة; وإلا، فإنها خاطئة.
     */
    private boolean validateForm() {
        boolean isValid = true;
        // Extract values from form fields
        String name = tilName.getEditText().getText().toString().trim();
        String age = tilAge.getEditText().getText().toString().trim();
        String gender = tilGender.getEditText().getText().toString().trim();
        String breed = tilBreed.getEditText().getText().toString().trim();
      //  String vaccineDetails = tilVaccineDetails.getEditText().getText().toString().trim();
       // String notes = tilNotes.getEditText().getText().toString().trim();
    //    boolean vaccinated = cbVaccinated.isChecked();

        // Validate each field
        if (name.isEmpty()) {
            isValid = false;
            tilName.setError("Name is required");
        } else {
            tilName.setError(null);
        }
        if (age.isEmpty()) {
            isValid = false;
            tilAge.setError("Age is required");
        } else {
            tilAge.setError(null);
        }
        if (gender.isEmpty()) {
            isValid = false;
            tilGender.setError("Gender is required");
        } else {
            tilGender.setError(null);
        }
        if (breed.isEmpty()) {
            isValid = false;
            tilBreed.setError("Breed is required");
        } else {
            tilBreed.setError(null);
        }
//        if (vaccineDetails.isEmpty() && !vaccinated) {
//            isValid = false;
//            tilVaccineDetails.setError("Vaccine Details or Vaccinated must be provided");
//        } else {
//            tilVaccineDetails.setError(null);
//        }

        if (isValid)
        {
            Animal a=new Animal();
            a.setAge(age);
            a.setName(name);
            a.setBreed(breed);
            a.setGender(gender);
            AppDatabase.getDB(this).animalQuery().insert(a);
            saveUser(a);
        }
        return isValid;
    }

    public void saveUser(Animal miley) {// الحصول على مرجع إلى عقدة "users" في قاعدة البيانات

        // تهيئة Firebase Realtime Database    //مؤشر لقاعدة البيانات
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
// ‏مؤشر لجدول المستعملين
        DatabaseReference usersRef = database.child("animals");
        // إنشاء مفتاح فريد للمستخدم الجديد
        DatabaseReference newUserRef = usersRef.push();
        // تعيين معرف المستخدم في كائن MyUser
        miley.setKeyid(newUserRef.getKey());
        // حفظ بيانات المستخدم في قاعدة البيانات
        //اضافة كائن "لمجموعة" المستعملين ومعالج حدث لفحص نجاح المطلوب

        newUserRef.setValue(miley)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AnimalDetails.this, "Succeeded to add User",  Toast.LENGTH_SHORT).show();
                        finish();




                        // تم حفظ البيانات بنجاح
                        Log.d(TAG, "تم حفظ المستخدم بنجاح: " + miley.getKeyid());
                        // تحديث واجهة المستخدم أو تنفيذ إجراءات أخرى
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // معالجة الأخطاء
                        Log.e(TAG, "خطأ في حفظ المستخدم: " + e.getMessage(), e);
                        Toast.makeText(AnimalDetails.this, "Failed to add User", Toast.LENGTH_SHORT).show();
                        // عرض رسالة خطأ للمستخدم
                    }
                });
    }

}