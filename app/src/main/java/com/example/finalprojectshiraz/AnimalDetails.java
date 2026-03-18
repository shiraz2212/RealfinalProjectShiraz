// تحديد الحزمة الخاصة بهذا الكلاس داخل المشروع
package com.example.finalprojectshiraz;

// استيراد TAG لاستخدامه في Log
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

// استيراد ميزات EdgeToEdge لجعل الشاشة تمتد للحواف
import androidx.activity.EdgeToEdge;

// لاستقبال نتيجة اختيار صورة أو طلب إذن
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// استيراد كلاس Animal (يمثل جدول في قاعدة البيانات المحلية)
import com.example.finalprojectshiraz.data.AnimalTable.Animal;

// استيراد قاعدة البيانات المحلية Room
import com.example.finalprojectshiraz.data.AppDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

// عناصر الإدخال الحديثة من Material Design
import com.google.android.material.textfield.TextInputLayout;

// استيراد Firebase Realtime Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * شاشة إدخال تفاصيل الحيوان
 */
public class AnimalDetails extends AppCompatActivity {

    // زر الإرسال
    private Button btnSubmit;

    // حقول إدخال البيانات الأساسية
    private TextInputLayout tilName;
    private TextInputLayout tilAge;
    private TextInputLayout tilGender;
    private TextInputLayout tilBreed;

    // عرض صورة الحيوان
    private ImageView ivAnimalImage;

    // لتخزين رابط الصورة المختارة
    private Uri selectedImageUri;

    // كائن مسؤول عن فتح معرض الصور
    private ActivityResultLauncher<String> pickImage;

    // حقول إضافية
    private TextInputLayout tilVaccineDetails;
    private TextInputLayout tilNotes;
    private CheckBox cbVaccinated;

    // مشغلات طلب الأذونات
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    /**
     * فحص وطلب الأذونات حسب إصدار أندرويد
     */

// دالة لفحص وطلب الأذونات
    private void checkAndRequestPermissions() {
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission already granted");
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // أندرويد 10 و 11 و 12// على هذه الإصدارات، READ_EXTERNAL_STORAGE له سلوك مختلف
            // إذا كنت تستخدم Scoped Storage بشكل صحيح، قد لا تحتاج إلى هذا الإذن
            // ولكن إذا كنت تحتاج إلى الوصول إلى جميع الصور، فقد تحتاج إلى READ_EXTERNAL_STORAGE
            // في هذا المثال، سنفحص READ_EXTERNAL_STORAGE للإصدارات الأقدم من 13
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission already granted (for older versions)");
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        } else { // أندرويد 9 والإصدارات الأقدم
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission already granted (for older versions)");
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        }


        // فحص وطلب إذن READ_MEDIA_VIDEO (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaVideoPermission.launch(android.Manifest.permission.READ_MEDIA_VIDEO);
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission already granted");
                Toast.makeText(this, "إذن قراءة الفيديو ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        }// ملاحظة: إذن INTERNET لا يحتاج إلى فحص أو
    }





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // تفعيل العرض بالحواف
        EdgeToEdge.enable(this);

        // ربط ملف التصميم
        setContentView(R.layout.activity_animal_details);

        /**
         * تهيئة مشغل اختيار الصورة
         */
        pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        // إذا تم اختيار صورة
                        if (result != null) {

                            // حفظ رابط الصورة
                            selectedImageUri = result;

                            // عرض الصورة في واجهة المستخدم
                            ivAnimalImage.setImageURI(result);
                            ivAnimalImage.setVisibility(View.VISIBLE);
                        }
                    }
                });


        ivAnimalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch("image/*"); // Launch the image picker
            }
        });



        /**
         * تهيئة مشغلات طلب الأذونات
         */
        requestReadMediaImagesPermission =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (isGranted) {
                                // عرض رسالة نجاح
                                Toast.makeText(this,
                                        "تم منح إذن قراءة الصور",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // عرض رسالة رفض
                                Toast.makeText(this,
                                        "تم رفض إذن قراءة الصور",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

        requestReadExternalStoragePermission =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (isGranted) {
                                Toast.makeText(this,
                                        "تم منح إذن التخزين",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this,
                                        "تم رفض إذن التخزين",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

        /**
         * ربط عناصر الواجهة بالكود
         */
        btnSubmit = findViewById(R.id.btnSubmit);
        tilName = findViewById(R.id.tilName);
        tilAge = findViewById(R.id.tilAge);
        tilGender = findViewById(R.id.tilGender);
        tilBreed = findViewById(R.id.tilBreed);
        tilVaccineDetails = findViewById(R.id.tilVaccineDetails);
        tilNotes = findViewById(R.id.tilNotes);
        cbVaccinated = findViewById(R.id.cbVaccinated);

        // عند الضغط على زر الإرسال
        btnSubmit.setOnClickListener(v -> {

            // التحقق من صحة البيانات
            if (validateForm()) {

                // الانتقال للشاشة الرئيسية
                Intent intent = new Intent(
                        AnimalDetails.this,
                        HomeScreen.class);

                startActivity(intent);
            }
        });
    }

    /**
     * التحقق من صحة الحقول
     */
    private boolean validateForm() {

        boolean isValid = true;

        // استخراج البيانات من الحقول
        String name = tilName.getEditText().getText().toString().trim();
        String age = tilAge.getEditText().getText().toString().trim();
        String gender = tilGender.getEditText().getText().toString().trim();
        String breed = tilBreed.getEditText().getText().toString().trim();

        // التحقق من كل حقل
        if (name.isEmpty()) {
            tilName.setError("Name is required");
            isValid = false;
        } else {
            tilName.setError(null);
        }

        if (age.isEmpty()) {
            tilAge.setError("Age is required");
            isValid = false;
        } else {
            tilAge.setError(null);
        }

        if (gender.isEmpty()) {
            tilGender.setError("Gender is required");
            isValid = false;
        } else {
            tilGender.setError(null);
        }

        if (breed.isEmpty()) {
            tilBreed.setError("Breed is required");
            isValid = false;
        } else {
            tilBreed.setError(null);
        }

        // إذا كل البيانات صحيحة، إنشاء كائن Animal وحفظه
        if (isValid) {

            Animal a = new Animal();
            a.setName(name);
            a.setAge(age);
            a.setGender(gender);
            a.setBreed(breed);

            // حفظ في قاعدة البيانات المحلية Room
            AppDatabase.getDB(this)
                    .animalQuery()
                    .insert(a);

            // حفظ في Firebase
              saveUser(a);
        }
        // TODO: 18/03/2026  :  لازم اخلص اخر اشي بمهمة السيرفس(رقم 3.3 )
        return isValid;
    }

    /**
     * حفظ الحيوان في Firebase
     */
    public void saveUser(Animal miley) {

        // الحصول على مرجع قاعدة البيانات
        DatabaseReference database =
                FirebaseDatabase.getInstance().getReference();

        // إنشاء عقدة animals
        DatabaseReference animalsRef =
                database.child("animals");

        // إنشاء مفتاح فريد لكل حيوان
        DatabaseReference newAnimalRef =
                animalsRef.push();

        // تخزين المفتاح داخل الكائن
        miley.setKeyid(newAnimalRef.getKey());

        // حفظ البيانات في Firebase
        newAnimalRef.setValue(miley)
                .addOnSuccessListener(aVoid -> {
                    // عند النجاح
                    Toast.makeText(this,
                            "Succeeded to add User",
                            Toast.LENGTH_SHORT).show();

                    finish();

                    Log.d(TAG,
                            "تم الحفظ بنجاح: "
                                    + miley.getKeyid());
                })
                .addOnFailureListener(e -> {
                    // عند الفشل
                    Log.e(TAG,
                            "خطأ في الحفظ: "
                                    + e.getMessage());

                    Toast.makeText(this,
                            "Failed to add User",
                            Toast.LENGTH_SHORT).show();
                });
    }
}