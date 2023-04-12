package com.example.tourx.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.tourx.dao.ExcursionDao;
import com.example.tourx.dao.VacationDao;
import com.example.tourx.entity.Excursion;
import com.example.tourx.entity.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 33, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    // extend Dao's
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}