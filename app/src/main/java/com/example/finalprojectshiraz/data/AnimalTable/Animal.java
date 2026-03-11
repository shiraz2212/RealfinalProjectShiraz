// تحديد اسم الحزمة (المسار) التي يوجد بها هذا الكلاس داخل المشروع
package com.example.finalprojectshiraz.data.AnimalTable;

// استيراد Annotation الخاصة بإنشاء جدول في قاعدة بيانات Room
import androidx.room.Entity;

// استيراد Annotation لتحديد المفتاح الأساسي في الجدول
import androidx.room.PrimaryKey;

// تعريف هذا الكلاس كـ Entity (أي جدول) داخل قاعدة بيانات Room
@Entity
public class Animal {

    // تعريف المفتاح الأساسي للجدول مع توليد تلقائي للقيمة
    @PrimaryKey(autoGenerate = true)
    public long id; // رقم تعريفي فريد لكل حيوان

    public String name;        // اسم الحيوان
    public String type;        // نوع الحيوان (قط، كلب، طائر...)
    public String breed;       // السلالة
    public String gender;      // الجنس (ذكر / أنثى)
    public String age;         // عمر الحيوان
    public String color;       // لون الحيوان
    public String size;        // حجم الحيوان (صغير، متوسط، كبير)
    public String location;    // موقع الحيوان
    public String image;       // مسار أو رابط صورة الحيوان
    public String description; // وصف إضافي للحيوان
    public String status;      // حالة الحيوان (متاح للتبني، تم التبني...)
    public String keyid;       // معرف خارجي (مثلاً من Firebase)

    // إعادة تعريف دالة toString لعرض بيانات الكائن كنص
    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", keyid='" + keyid + '\'' +
                '}';
    }

    // Getter للحصول على اسم الحيوان
    public String getName() {
        return name;
    }

    // Setter لتعديل اسم الحيوان
    public void setName(String name) {
        this.name = name;
    }

    // Getter للحصول على نوع الحيوان
    public String getType() {
        return type;
    }

    // Setter لتعديل نوع الحيوان
    public void setType(String type) {
        this.type = type;
    }

    // Getter للحصول على السلالة
    public String getBreed() {
        return breed;
    }

    // Setter لتعديل السلالة
    public void setBreed(String breed) {
        this.breed = breed;
    }

    // Getter للحصول على الجنس
    public String getGender() {
        return gender;
    }

    // Setter لتعديل الجنس
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter للحصول على العمر
    public String getAge() {
        return age;
    }

    // Setter لتعديل العمر
    public void setAge(String age) {
        this.age = age;
    }

    // Getter للحصول على اللون
    public String getColor() {
        return color;
    }

    // Setter لتعديل اللون
    public void setColor(String color) {
        this.color = color;
    }

    // Getter للحصول على الحجم
    public String getSize() {
        return size;
    }

    // Setter لتعديل الحجم
    public void setSize(String size) {
        this.size = size;
    }

    // Getter للحصول على الموقع
    public String getLocation() {
        return location;
    }

    // Setter لتعديل الموقع
    public void setLocation(String location) {
        this.location = location;
    }

    // Getter للحصول على مسار الصورة
    public String getImage() {
        return image;
    }

    // Setter لتعديل مسار الصورة
    public void setImage(String image) {
        this.image = image;
    }

    // Getter للحصول على الوصف
    public String getDescription() {
        return description;
    }

    // Setter لتعديل الوصف
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter للحصول على الحالة
    public String getStatus() {
        return status;
    }

    // Setter لتعديل الحالة
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter للحصول على المعرف الخارجي
    public String getKeyid() {
        return keyid;
    }

    // Setter لتعديل المعرف الخارجي
    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }
}