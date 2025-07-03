package com.example.drugfoodapplication.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.drugfoodapplication.data.dao.FoodDao;
import com.example.drugfoodapplication.data.dao.InteractionDao;
import com.example.drugfoodapplication.data.dao.MedicationDao;
import com.example.drugfoodapplication.data.dao.UserDao;
import com.example.drugfoodapplication.data.entity.Food;
import com.example.drugfoodapplication.data.entity.Interaction;
import com.example.drugfoodapplication.data.entity.Medication;
import com.example.drugfoodapplication.data.entity.User;

@Database(entities = {User.class, Medication.class, Food.class, Interaction.class}, version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract MedicationDao medicationDao();
    public abstract FoodDao foodDao();
    public abstract InteractionDao interactionDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "drug_food_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
