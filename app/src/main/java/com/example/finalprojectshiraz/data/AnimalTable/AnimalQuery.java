package com.example.finalprojectshiraz.data.AnimalTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * واجهة AnimalQuery
 * تُعرّف العمليات الأساسية على جدول Animal في قاعدة البيانات المحلية (Room)
 */
@Dao
public interface AnimalQuery {

    /**
     * جلب كل الحيوانات من الجدول
     * @return قائمة تحتوي على جميع كائنات Animal
     */
    @Query("SELECT * FROM Animal")
    List<Animal> getAllAnimal();

    /**
     * جلب جميع الحيوانات حسب مجموعة من الـ IDs
     * @param AnimalIds مصفوفة أرقام الـ keyid
     * @return قائمة تحتوي على الحيوانات المطابقة لهذه الـ IDs
     */
    @Query("SELECT * FROM Animal WHERE keyid IN (:AnimalIds)")
    List<Animal> loadAllByIds(int[] AnimalIds);

    /**
     * إضافة مجموعة من الحيوانات دفعة واحدة
     * @param animals مصفوفة من كائنات Animal
     */
    @Insert
    void insertAll(Animal... animals);

    /**
     * حذف حيوان معين باستخدام الكائن
     * @param animal كائن Animal المراد حذفه
     */
    @Delete
    void delete(Animal animal);

    /**
     * حذف حيوان باستخدام الـ ID
     * @param id رقم الـ keyid الخاص بالحيوان
     */
    @Query("DELETE FROM Animal WHERE keyid = :id")
    void delete(int id);

    /**
     * إدراج حيوان واحد في الجدول
     * @param animal كائن Animal المراد إضافته
     */
    @Insert
    void insert(Animal animal);

    /**
     * تحديث بيانات حيوان موجود في الجدول
     * @param animals كائن Animal يحتوي على البيانات الجديدة
     */
    @Update
    void update(Animal animals);
}