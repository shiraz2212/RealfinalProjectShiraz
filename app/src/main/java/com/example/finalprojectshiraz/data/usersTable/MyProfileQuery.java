package com.example.finalprojectshiraz.data.usersTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyProfileQuery {
    @Query("SELECT * FROM MyProfile")
    List<MyProfile> getAll();
    // استخراج مستعمل حسب رقم المميز لهid

    @Query("SELECT * FROM MyProfile WHERE keyid IN (:userIds)")
    List<MyProfile> loadAllByIds(int[] userIds);
    //هل المستعمل موجود حسب الايميل وكلمة السر
    @Query("SELECT * FROM MyProfile WHERE email = :myEmail AND passw = :myPassw LIMIT 1")
    MyProfile checkEmailPassw(String myEmail, String myPassw);
    //فحص هل الايميل موجود من قبل
    @Query("SELECT * FROM MyProfile WHERE email = :myEmail LIMIT 1")
    MyProfile checkEmail(String myEmail);
    @Insert
// اضافة مستعمل او مجموعة مستعملين
    void insertAll(MyProfile... users);
    @Delete
// حذف
    void delete(MyProfile user);
    //حذف حسب الرقم المميز id

    @Insert//اضافة مستعمل واحد
    void insert(MyProfile myUser);
    @Update
//تعديل مستعمل او قائمة مستعملين
    void update(MyProfile...values);

}
