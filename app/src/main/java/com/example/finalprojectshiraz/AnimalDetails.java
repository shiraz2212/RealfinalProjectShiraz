package com.example.finalprojectshiraz;

// استيراد المكتبات اللازمة للنظام والأندرويد
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

// استيراد مكتبات AndroidX و Google للموقع وقواعد البيانات
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalprojectshiraz.data.AnimalTable.AirPlaneReceiver;
import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AnimalTable.AnimalReminderReceiver;
import com.example.finalprojectshiraz.data.AppDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// كلاس تفاصيل الحيوان المسؤول عن إضافة وتعديل بيانات الحيوانات
public class AnimalDetails extends AppCompatActivity {
    // تعريف المتغيرات للمكونات في واجهة المستخدم والمستقبلات
    private AirPlaneReceiver systemEventIsReceiver; // مستقبل لوضع الطيران
    private Button btnSubmit; // زر الحفظ
    private TextInputLayout tilName, tilAge, tilGender, tilBreed, tilLocation, tilReminder; // حاويات حقول الإدخال
    private TextInputEditText etLocation, etReminder; // حقول النص للإدخال
    private ImageView ivAnimalImage; // صورة الحيوان
    private Uri selectedImageUri; // مسار الصورة المختارة
    private ActivityResultLauncher<String> pickImage; // مشغل اختيار الصورة من المعرض
    private FusedLocationProviderClient fusedLocationClient; // عميل الحصول على الموقع
    private Calendar reminderCalendar = Calendar.getInstance(); // كائن التقويم لتخزين وقت التذكير

    // مشغل طلب إذن التنبيهات (للأندرويد 13 فما فوق)
    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher = 
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                // عرض رسالة في حال رفض الإذن
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // تمكين العرض على كامل الشاشة
        setContentView(R.layout.activity_animal_details); // ربط الكود بملف التصميم XML

        // ربط المتغيرات بالمعرفات الموجودة في ملف التصميم
        btnSubmit = findViewById(R.id.btnSubmit);
        tilName = findViewById(R.id.tilName);
        tilAge = findViewById(R.id.tilAge);
        tilGender = findViewById(R.id.tilGender);
        tilBreed = findViewById(R.id.tilBreed);
        tilLocation = findViewById(R.id.tilLocation);
        etLocation = findViewById(R.id.etLocation);
        tilReminder = findViewById(R.id.tilReminder);
        etReminder = findViewById(R.id.etReminder);
        ivAnimalImage = findViewById(R.id.ivAnimalImage);

        // تهيئة عميل خدمات الموقع
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // تهيئة مشغل اختيار الصور ومعالجة النتيجة
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                selectedImageUri = result;
                ivAnimalImage.setImageURI(result); // عرض الصورة المختارة
                ivAnimalImage.setVisibility(View.VISIBLE);
            }
        });

        // عند الضغط على الصورة، يتم فتح المعرض لاختيار صورة
        ivAnimalImage.setOnClickListener(v -> pickImage.launch("image/*"));

        // عند الضغط على أيقونة الموقع، يتم جلب الموقع الحالي
        tilLocation.setEndIconOnClickListener(v -> getLastLocation());

        // عند الضغط على حقل التذكير، يتم فتح منتقي التاريخ والوقت
        etReminder.setOnClickListener(v -> showDateTimePicker());
        tilReminder.setEndIconOnClickListener(v -> showDateTimePicker());

        // عند الضغط على زر الحفظ، يتم التحقق من البيانات وحفظها
        btnSubmit.setOnClickListener(v -> validateAndSave());

        // تهيئة مستقبل وضع الطيران لتعطيل الزر في حال تفعيله
        systemEventIsReceiver = new AirPlaneReceiver(btnSubmit);

        // طلب إذن التنبيهات للأجهزة الحديثة
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    // دالة لعرض منتقي التاريخ والوقت (كود المدرس)
    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        // إنشاء ديالوج لاختيار التاريخ
        new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth); // ضبط التاريخ المختار
            // إنشاء ديالوج لاختيار الوقت بعد اختيار التاريخ
            new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay); // ضبط الساعة المختارة
                date.set(Calendar.MINUTE, minute); // ضبط الدقيقة المختارة
                date.set(Calendar.SECOND, 0);
                reminderCalendar = (Calendar) date.clone(); // حفظ الوقت المختار في المتغير العام
                etReminder.setText(date.getTime().toString()); // عرض التاريخ والوقت في حقل النص
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    // دالة لجدولة المنبه لإظهار التذكير (كود المدرس)
    private void scheduleAlarm(Animal task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // إنشاء نية (Intent) لتشغيل مستقبل التذكيرات
        Intent intent = new Intent(this, AnimalReminderReceiver.class);
        intent.putExtra("title", task.getName()); // إرسال اسم الحيوان كعنوان
        intent.putExtra("text", "Health reminder for your animal"); // إرسال نص التذكير
        // إعداد النية المعلقة (PendingIntent)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            // التحقق من إصدار النظام لجدولة المنبه بشكل دقيق
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getReminderTime(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, task.getReminderTime(), pendingIntent);
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getReminderTime(), pendingIntent);
            }
        }
    }

    // دالة للحصول على آخر موقع معروف للجهاز
    private void getLastLocation() {
        // التحقق من أذونات الموقع
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // طلب الأذونات في حال عدم توفرها
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        // جلب الموقع الحالي وتحديث واجهة المستخدم
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                updateLocationUI(location);
            } else {
                Toast.makeText(AnimalDetails.this, "Unable to find location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // دالة لتحويل إحداثيات الموقع إلى عنوان نصي
    private void updateLocationUI(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // جلب سطر العنوان الأول
                etLocation.setText(address);
            } else {
                etLocation.setText(location.getLatitude() + ", " + location.getLongitude());
            }
        } catch (IOException e) {
            etLocation.setText(location.getLatitude() + ", " + location.getLongitude());
        }
    }

    // معالجة نتيجة طلب أذونات الموقع
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(); // جلب الموقع بعد الحصول على الإذن
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // دالة لتحويل الصورة إلى نص (String) لحفظها في قاعدة البيانات
    public String convertImageToString(Uri uri) {
        if (uri == null) return "";
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) return "";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream); // ضغط الصورة
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT); // تحويل مصفوفة البايتات لنص
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    // دالة للتحقق من صحة البيانات المدخلة قبل الحفظ
    private void validateAndSave() {
        String name = tilName.getEditText().getText().toString().trim();
        String age = tilAge.getEditText().getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // التأكد من إدخال الاسم والعمر
        if (name.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "يرجى تعبئة الحقول الأساسية", Toast.LENGTH_SHORT).show();
            return;
        }

        // إنشاء كائن حيوان جديد وتعبئة بياناته
        Animal a = new Animal();
        a.setName(name);
        a.setAge(age);
        a.setGender(tilGender.getEditText().getText().toString().trim());
        a.setBreed(tilBreed.getEditText().getText().toString().trim());
        a.setLocation(location.isEmpty() ? "N/A" : location);
        a.setImage(convertImageToString(selectedImageUri));
        
        // ضبط وقت التذكير إذا تم اختياره
        if (!etReminder.getText().toString().isEmpty()) {
            a.setReminderTime(reminderCalendar.getTimeInMillis());
        }

        btnSubmit.setEnabled(false); // تعطيل الزر لمنع تكرار الضغط
        saveToFirebase(a); // حفظ البيانات في فيربيس
    }

    // دالة لحفظ بيانات الحيوان في Firebase وقاعدة البيانات المحلية Room
    private void saveToFirebase(Animal a) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        String key = dbRef.push().getKey(); // توليد مفتاح فريد جديد
        a.setKeyid(key);

        // حفظ البيانات في Realtime Database
        dbRef.child(key).setValue(a).addOnSuccessListener(unused -> {
            // جدولة التذكير إذا كان الوقت في المستقبل
            if (a.getReminderTime() > System.currentTimeMillis()) {
                scheduleAlarm(a);
            }
            // حفظ البيانات محلياً في خيط منفصل (Thread) لتجنب تعليق واجهة المستخدم
            new Thread(() -> {
                AppDatabase.getDB(getApplicationContext()).animalQuery().insert(a);
                runOnUiThread(() -> {
                    Toast.makeText(this, "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                    finish(); // العودة للشاشة السابقة
                });
            }).start();
        }).addOnFailureListener(e -> {
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "فشل الحفظ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // تسجيل مستقبل وضع الطيران عند بدء النشاط
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(systemEventIsReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(systemEventIsReceiver, filter);
            }
        } catch (Exception e) {}
    }

    @Override
    protected void onStop() {
        super.onStop();
        // إلغاء تسجيل المستقبل عند إيقاف النشاط لمنع تسرب الذاكرة
        try { unregisterReceiver(systemEventIsReceiver); } catch (Exception e) {}
    }
}
