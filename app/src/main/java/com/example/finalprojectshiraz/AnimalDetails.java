package com.example.finalprojectshiraz;

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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshiraz.data.AnimalTable.Animal;
// استيراد كلاس Animal (يمثل كائن الحيوان في قاعدة البيانات)

import com.example.finalprojectshiraz.data.AppDatabase;
// استيراد قاعدة البيانات المحلية (Room)

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// استيراد Firebase Realtime Database

public class AnimalDetails extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputLayout tilName;
    private TextInputLayout tilAge;
    private TextInputLayout tilGender;
    private TextInputLayout tilBreed;
    //حقول إدخال البيانات (الاسم، العمر، الجنس، السلالة)
    private ImageView ivAnimalImage; //صفة كمؤشر لهذا الكائن

    private Uri selectedImageUri;//صفة لحفظ عنوان الصورة بعد اختيارها
    private ActivityResultLauncher<String> pickImage;// ‏كائن لطلب الصورة من الهاتف

    private TextInputLayout tilVaccineDetails;
    private TextInputLayout tilNotes;
    private CheckBox cbVaccinated;

    // مُشغّلات لطلب الأذونات
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;
    // دالة لفحص وطلب الأذونات
    private void checkAndRequestPermissions() {
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            // فحص إذن قراءة الصور
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                //-الإصدارات الأقدم تحتاج READ_EXTERNAL_STORAGE إذا كان الإذن ممنوح بالفعل
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_details);

// Initialize the ActivityResultLauncher for picking images
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    selectedImageUri = result;
                    ivAnimalImage.setImageURI(result);
                    ivAnimalImage.setVisibility(View.VISIBLE);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
                }
            }
        });
        ivAnimalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch("image/*"); // Launch the image picker
            }
        });

        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_IMAGES permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadMediaVideoPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_VIDEO permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
//استدعاء دالة الفحص (سيتم تطبيقها لاحقا)
        //    checkAndRequestPermissions();


        //   ربط عناصر الواجهة بالكود
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
                        // عرض رسالة خطأ للمستخدم`
                    }

                });
    }

}