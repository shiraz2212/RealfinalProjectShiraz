package com.example.finalprojectshiraz.data.AnimalTable;

import androidx.room.Dao;

import java.util.List;

@Dao
public interface AnimalQuery
{
    List<Animal> getAll();
    List<Animal> loadAllByIds(int[] userIds);



}
