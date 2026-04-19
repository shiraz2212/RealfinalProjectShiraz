package com.example.finalprojectshiraz.data.AnimalTable;

// استيراد المكتبات اللازمة للتعامل مع نظام البث والواجهة
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

// تعريف كلاس AirPlaneReceiver الذي يرث من BroadcastReceiver لاستقبال أحداث النظام
public class AirPlaneReceiver extends BroadcastReceiver {
    // تعريف متغير خاص من نوع Button للتحكم في الزر
    private Button save;//נגדיר תכונה של לחצן בתוך המחלקה.

    // منشئ الكلاس الذي يأخذ الزر كمعامل لربطه بالمتغير المحلي
    public AirPlaneReceiver(Button btnAddTask) { //נצור שיטה בונה המקבלת כפרמטר הפניה של הלחצן, ונציב בתוך התכונה.
        save=btnAddTask;
    }

    // منشئ افتراضي فارغ للكلاس
    public  AirPlaneReceiver()
    {

    }

    // الدالة التي يتم تنفيذها تلقائياً عند استقبال أي بث (نوايا) من النظام
    @Override
    public void onReceive(Context context, Intent intent) {
        // التحقق مما إذا كان نوع البث المستلم هو تغيير وضع الطيران
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
            // استخراج قيمة حالة وضع الطيران الحالية (مفعل أو غير مفعل)
            boolean isEnabled = intent.getBooleanExtra("state", false);
            // إذا كان وضع الطيران مفعلاً
            if (isEnabled) {
                // عرض رسالة تنبيهية للمستخدم تفيد بأن وضع الطيران مفعل والمزامنة متوقفة
                Toast.makeText(context, "System: Airplane Mode is ON. Sync is disabled.", Toast.LENGTH_LONG).show();
                // تعطيل الزر لمنع الضغط عليه
                save.setEnabled(false);//נשנה את המצב הלחצן בתוך onReceive
                // تغيير النص الظاهر على الزر لتوضيح الحالة
                save.setText("AirPalne is on");//נשנה את המצב הלחצן בתוך onReceive
            } else {
                // إذا كان وضع الطيران معطلاً، عرض رسالة تفيد بعودة المزامنة
                Toast.makeText(context, "System: Airplane Mode is OFF. Sync is back!", Toast.LENGTH_LONG).show();
                // إعادة تفعيل الزر ليصبح قابلاً للضغط
                save.setEnabled(true);//נשנה את המצב הלחצן בתוך onReceive
                // إعادة النص الأصلي للزر أو توضيح أنه متاح
                save.setText("AirPalne is off");//נשנה את המצב הלחצן בתוך onReceive
            }
        }
    }
}
