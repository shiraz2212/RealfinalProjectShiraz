package com.example.finalprojectshiraz.data.AnimalTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnimalQuery
{
    @Query("SELECT * FROM Animal")

    List<Animal> getAll();
    @Query("SELECT * FROM Animal WHERE Keyid IN (:AnimalIds)")
    List<Animal> loadAllByIds  (int[] AnimalIds);
    @Insert
    void insertAll(Animal... animals);
    @Delete
    void delete(Animal animal);
    @Query("Delete FROM Animal WHERE Keyid = :id")
    void delete (int id);
    @Insert
    void insert (Animal animal);
    @Update
    void update (Animal animals);



}
