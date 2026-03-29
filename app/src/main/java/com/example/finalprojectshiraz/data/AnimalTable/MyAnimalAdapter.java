package com.example.finalprojectshiraz.data.AnimalTable; // تعريف الحزمة التي تحتوي هذا الكلاس

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context; // استيراد كلاس السياق
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater; // استيراد كلاس بناء الواجهة من XML
import android.view.MenuItem;
import android.view.View; // استيراد عنصر واجهة عام
import android.view.ViewGroup; // استيراد مجموعة عناصر واجهة
import android.widget.ArrayAdapter; // استيراد ArrayAdapter الذي سنرث منه
import android.widget.ImageButton; // استيراد زر بصورة
import android.widget.ImageView; // استيراد عنصر عرض صورة
import android.widget.PopupMenu;
import android.widget.TextView; // استيراد عنصر عرض نص
import android.widget.Toast;

import androidx.annotation.NonNull; // توضيح أن القيمة لا يمكن أن تكون null
import androidx.annotation.Nullable; // توضيح أن القيمة يمكن أن تكون null
import androidx.core.content.PermissionChecker;

import com.example.finalprojectshiraz.AnimalDetails;
import com.example.finalprojectshiraz.R; // استيراد ملف الموارد R

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


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
     *  פתיחת אפליקצית שליחת sms
     * @param msg .. ההודעה שרוצים לשלוח
     * @param phone
     */
    public void openSendSmsApp(String msg, String phone)
    {
        //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        //מעבירים מספר הטלפון
        smsIntent.setData(Uri.parse("smsto:"+phone));
        //ההודעה שנרצה שתופיע באפליקצית ה סמס
        smsIntent.putExtra("sms_body",msg);
        smsIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //פתיחת אפליקציית ה סמס
        getContext().startActivity(smsIntent);
    }

    /**
     *  פתיחת אפליקצית שליחת whatsapp
     * @param msg .. ההודעה שרוצים לשלוח
     * @param phone
     */
    public void openSendWhatsAppV2(String msg, String phone)
    {
        //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);;
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+phone+"&text="+ URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            Toast.makeText(getContext(), "there is no whatsapp!!", Toast.LENGTH_SHORT).show();
        }
        sendIntent.setData(Uri.parse(url));
        sendIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //פתיחת אפליקציית ה סמס
        getContext().startActivity(sendIntent);
    }

    /**
     * ביצוע שיחה למפסר טלפון
     * todo הוספת הרשאה בקובץ המניפיסט
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     * @param phone מספר טלפון שרוצים להתקשר אליו*/
    private void callAPhoneNymber(String phone){
        //בדיקה אם יש הרשאה לביצוע שיחה
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//בדיקת גרסאות
            //בדיקה אם ההרשאה לא אושרה בעבר
            if (checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_DENIED) {
                //רשימת ההרשאות שרוצים לבקש אישור
                String[] permissions = {Manifest.permission.CALL_PHONE};
                //בקשת אישור הרשאות (שולחים קוד הבקשה)
                //התשובה תתקבל בפעולה onRequestPermissionsResult
                requestPermissions((Activity) getContext(),permissions, 100);
            }
            else{
                //אינטנט מרומז לפתיחת אפליקצית ההודות סמס
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData(Uri.parse("tel:" + phone));
                getContext().startActivity(phone_intent);
            }
        }}


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
        ImageButton btnSendSMS = vitem.findViewById(R.id.btnSendSMS);
        ImageButton imgBtnCall = vitem.findViewById(R.id.imgBtnCallitm);
        ImageButton imgBtnEdit = vitem.findViewById(R.id.imgBtnEdititm);
        ImageButton imgBtnDelete = vitem.findViewById(R.id.imgBtnDeleteitm);

        // الحصول على كائن الحيوان الحالي حسب موقعه
        Animal current = getItem(position);

        // تعيين بيانات الحيوان داخل عناصر الواجهة
        tvItmTitle.setText(current.getName()); // عرض اسم الحيوان
        tvItmText.setText(current.getDescription()); // عرض وصف الحيوان
        tvItemImportance.setText("Importance: " + current.getType()); //// عرض نوع الحيوان
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendSmsApp(current.getDescription(),"");// אם יש טלפון המשימה מעבירים במקום ה ״״
            }
        });


        // إعادة العنصر بعد تعبئته بالبيانات
        return vitem;

    }
    public void showPopUpMenu(View v, Animal item)
    {
        // بناء قائمة popup menu
        PopupMenu popup = new PopupMenu(getContext(), v);; // الكائن الذي سبب فتح القائمة

        // ملف القائمة
        popup.inflate(R.menu.popup_menu);

        // إضافة معالج حدث لاختيار عنصر من القائمة
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

//                if(menuItem.getItemId()==R.id.mnAddTask){
//                    // هنا نكتب رد الفعل لاختيار هذا العنصر من القائمة
//                    Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getContext(), AnimalDetails.class);
//                    getContext().startActivity(i);
//                    startActivity(i);
//                }

                if(menuItem.getItemId()==R.id.del){
                    Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                }

                if(menuItem.getItemId()==R.id.edit){
                    Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        popup.show(); // فتح عرض القائمة
    }
}