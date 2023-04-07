package com.example.mpcandroidapp.dao;


import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

@androidx.room.Database(entities = {QRCode.class, Session.class}, version = 3)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    private static final String DATABASE_NAME = "user-database";

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, DATABASE_NAME)
                    .addMigrations(DatabaseMigration.MIGRATION_1_2)
                    .addMigrations(DatabaseMigration.MIGRATION_2_3)
                    .build();
        }
        return instance;
    }

    public abstract QRCodeDao qrCodeDao();
    public abstract SessionDao sessionDao();
}
