package com.example.finalprojectshiraz.data.AnimalTable; // تعريف الحزمة التي تحتوي هذا الكلاس

import static com.example.finalprojectshiraz.R.*; // استيراد موارد المشروع بشكل ثابت

import android.content.Context; // استيراد كلاس السياق
import android.view.LayoutInflater; // استيراد كلاس بناء الواجهة من XML
import android.view.View; // استيراد عنصر واجهة عام
import android.view.ViewGroup; // استيراد مجموعة عناصر واجهة
import android.widget.ArrayAdapter; // استيراد ArrayAdapter الذي سنرث منه
import android.widget.ImageButton; // استيراد زر بصورة
import android.widget.ImageView; // استيراد عنصر عرض صورة
import android.widget.ListView; // استيراد ListView
import android.widget.Spinner; // استيراد Spinner
import android.widget.TextView; // استيراد عنصر عرض نص

import androidx.annotation.NonNull; // توضيح أن القيمة لا يمكن أن تكون null
import androidx.annotation.Nullable; // توضيح أن القيمة يمكن أن تكون null
import androidx.appcompat.app.AppCompatActivity; // استيراد Activity متقدمة

import com.example.finalprojectshiraz.R; // استيراد ملف الموارد R
import com.google.android.material.floatingactionbutton.FloatingActionButton; // استيراد زر عائم

/**
 * كلاس MyAnimalAdapter
 *
 * كلاس مخصص لعرض كائنات من نوع Animal داخل ListView.
 * يرث من ArrayAdapter ويقوم بربط بيانات الحيوان مع التصميم الخاص بكل عنصر.
 */
public class MyAnimalAdapter extends ArrayAdapter<Animal>
{
    /**
     * متغير يمثل رقم ملف تصميم العنصر (layout resource id)
     */
    private final int itemLayout;

    /**
     * Constructor
     *
     * يقوم بإنشاء Adapter جديد لعرض الحيوانات داخل ListView.
     *
     * @param context  السياق (Activity) الذي يعمل فيه الـ Adapter
     * @param resource رقم ملف التصميم الخاص بعنصر واحد في القائمة
     */
    public MyAnimalAdapter(@NonNull Context context, int resource) {
        super(context, resource); // استدعاء المُنشئ الأساسي في ArrayAdapter
        this.itemLayout = resource; // تخزين رقم ملف التصميم
    }

    /**
     * الدالة getView
     *
     * مسؤولة عن إنشاء عنصر رسومي واحد وعرض بيانات الحيوان بداخله.
     *
     * @param position موقع العنصر داخل القائمة (يبدأ من 0)
     * @param convertView عنصر قديم لإعادة استخدامه لتحسين الأداء
     * @param parent عنصر الأب الذي يحتوي القائمة (مثل ListView)
     * @return View عنصر رسومي جاهز للعرض
     */
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // إنشاء متغير للعنصر الرسومي وإعادة استخدامه إن وجد
        View vitem = convertView;

        // إذا لم يكن هناك عنصر جاهز، يتم إنشاؤه من ملف التصميم
        if (vitem == null)
            vitem = LayoutInflater.from(getContext()).inflate(itemLayout, parent, false);

        // ربط عناصر الواجهة من ملف التصميم
        ImageView imageView = vitem.findViewById(R.id.imgVitm);
        TextView tvItmTitle = vitem.findViewById(R.id.tvItmTitle);
        TextView tvItmText = vitem.findViewById(R.id.tvItmText);
        TextView tvItemImportance = vitem.findViewById(R.id.tvItemImportance);
        ImageButton imgBtnSendSms = vitem.findViewById(R.id.imgBtnSendSmsitm);
        ImageButton imgBtnCall = vitem.findViewById(R.id.imgBtnCallitm);
        ImageButton imgBtnEdit = vitem.findViewById(R.id.imgBtnEdititm);
        ImageButton imgBtnDelete = vitem.findViewById(R.id.imgBtnDeleteitm);

        // الحصول على كائن الحيوان الحالي حسب موقعه
        Animal current = getItem(position);

        // تعيين بيانات الحيوان داخل عناصر الواجهة
        tvItmTitle.setText(current.getName()); // عرض اسم الحيوان
        tvItmText.setText(current.getDescription()); // عرض وصف الحيوان
        tvItemImportance.setText("Importance: " + current.getType()); // عرض نوع الحيوان

        // إعادة العنصر بعد تعبئته بالبيانات
        return vitem;
    }
}