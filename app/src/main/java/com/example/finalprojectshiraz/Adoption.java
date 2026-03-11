package com.example.finalprojectshiraz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Adoption extends AppCompatActivity {

    @Override
    /**
     * هذه الدالة تستدعي في كل مرة يتم انشاء هذا النموذج لتعيين المحتوي
     *
     * @param savedInstanceState هذا المتغير لاستدعاء الكلاس الأب للنموذج الذي تم انشاءه فيه
     */
    protected void onCreate(Bundle savedInstanceState) { // أول دالة يتم تنفيذها عند فتح الشاشة
        super.onCreate(savedInstanceState);//استدعاء دالة الأب - ضروري حتى يجهّز النظام الشاشة بشكل صحيح
        EdgeToEdge.enable(this);//يفعّل العرض بكامل الشاشة
        setContentView(R.layout.Adoption);//ربط هذه الشاشة بملف التصميم XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { //الهدف: منع تداخل المحتوى مع أزرار النظام
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());// -حصل على أبعاد شريط الحالة وشريط التنقل
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);// ضيف Padding حسب أبعاد النظام حتى لا يغطي شريط النظام محتوى الشاشة
            return insets;//نعيد القيم بعد تعديلها
        });
    }
    /**
     * هاي أول دالة بتشتغل لما شاشة التبنّي تنفتح
     * يهيّئ شاشة Adoption
     * يربطها بالـ XML
     *  يضبط العرض الكامل والحواف
     */
}