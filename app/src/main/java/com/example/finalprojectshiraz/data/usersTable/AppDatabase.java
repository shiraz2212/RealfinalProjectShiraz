package com.example.finalprojectshiraz.data.usersTable;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalprojectshiraz.data.AnimalTable.AnimalQuery;

public class AppDatabase
{
    @Database(entities = {MyProfile.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        private static AppDatabase db;
        @return
        public abstract MyProfileQuery myProfileQuery();
        @return

        public abstract AnimalQuery animalQuery();
        @return

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


    }
}
