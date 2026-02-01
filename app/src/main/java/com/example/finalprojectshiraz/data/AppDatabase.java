package com.example.finalprojectshiraz.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalprojectshiraz.data.AnimalTable.Animal;
import com.example.finalprojectshiraz.data.AnimalTable.AnimalQuery;
import com.example.finalprojectshiraz.data.usersTable.MyProfile;
import com.example.finalprojectshiraz.data.usersTable.MyProfileQuery;

@Database(entities = {MyProfile.class, Animal.class}, version = 1)
/**
 * الفئة المسؤولة عن بناء قاعدة البيانات بكل جداولها
 * وتوفر لنا كائن للتعامل مع قاعدة البيانات
 */
public abstract class AppDatabase extends RoomDatabase {
    /**
     * كائن للتعامل مع قاعدة البيانات
     */


    private static AppDatabase db;

    /**
     * يعيد كائن لعمليات جدول المستعملين
     *
     * @return
     */



    /**
     * @return الـ query للجدول الخاص بالمستخدمين
     */
    public abstract AnimalQuery animalQuery();

    /**
     * بناء قاعدة البيانات واعادة كائن يؤشر عليها
     *
     * @param context
     * @return
     */
    public static AppDatabase getDB(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, "myDatabase")//بناء قاعدة البيانات واعادة كائن يؤشر عليها
                    .allowMainThreadQueries()
                    .build();
        }
        return db;

    }


    public abstract MyProfileQuery getProfile();
    /**
     * “يا Room، أنا عندي DAO اسمه MyProfileQuery، وانت بدك تولّد الكود الحقيقي تبعه”
     */
}




