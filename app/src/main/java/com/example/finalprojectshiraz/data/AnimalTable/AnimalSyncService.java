package com.example.finalprojectshiraz.data.AnimalTable;

// استيراد المكتبات اللازمة لعمل الخدمة (Service) والتعامل مع Firebase
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * فئة AnimalSyncService: خدمة خلفية (Background Service)
 * تُستخدم لمزامنة بيانات الحيوانات (Animal) مع قاعدة بيانات Firebase في الخلفية.
 */
public class AnimalSyncService extends Service {
    
    // منشئ الكلاس الافتراضي
    public AnimalSyncService() {
    }

    /**
     * يتم استدعاء هذه الدالة عند بدء تشغيل الخدمة عبر startService
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // قراءة البيانات المرسلة مع "النية" (Intent)
        if (intent != null && intent.hasExtra("task_extra")) {
            // استخراج كائن Animal من الإضافات (Extras)
            Animal task = (Animal) intent.getSerializableExtra("task_extra");
            // استدعاء دالة الحفظ في Firebase
            saveMyTaskToFirebase(task);
        }
        
        // START_NOT_STICKY: تعني إذا قتل النظام الخدمة، فلا تقم بإعادة تشغيلها تلقائياً
        return START_NOT_STICKY;
    }

    /**
     * دالة مخصصة لحفظ كائن Animal في قاعدة بيانات Firebase Realtime Database
     */
    private void saveMyTaskToFirebase(Animal task) {
        // الحصول على مرجع للجدول المسمى "tasks" (أو animals) في Firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tasks");
        
        // إنشاء مفتاح فريد (Key) جديد للسجل
        String key = myRef.push().getKey();
        // تعيين هذا المفتاح للكائن لربطه بقاعدة البيانات
        task.setKeyid(key);

        // رفع بيانات الكائن إلى المسار المحدد تحت المفتاح الجديد
        myRef.child(key).setValue(task).addOnCompleteListener(fbTask -> {
            if (fbTask.isSuccessful()) {
                // عرض رسالة نجاح للمستخدم عند اكتمال الرفع بنجاح
                Toast.makeText(getApplicationContext(), "Sync Successful", Toast.LENGTH_SHORT).show();
            }
            // إيقاف الخدمة تلقائياً بعد انتهاء المهمة لتوفير موارد الجهاز (البطارية والرام)
            stopSelf();
        });
    }

    /**
     * هذه الدالة مطلوبة لأننا نرث من كلاس Service
     * تعيد null لأننا نستخدم Started Service وليس Bound Service
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; 
    }
}
