package com.example.chronos_hm40;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseSingleton {
    private static AppDatabase instance;

    private AppDatabaseSingleton() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "course.db")
                    .build();
        }
        return instance;
    }
}
