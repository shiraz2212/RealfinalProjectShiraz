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
public abstract class AppDatabase extends RoomDatabase {



        private static AppDatabase db;

        /**
         *
         * @return
         */
        public abstract MyProfileQuery myProfileQuery();

        public abstract  AnimalQuery animalQuery();

        public static AppDatabase getDB(Context context) {
            if (db == null) {
                db = Room.databaseBuilder(context, AppDatabase.class, "myDatabase")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
            return db;

        }

    }




