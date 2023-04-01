package com.example.mpcandroidapp.dao;


import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

@androidx.room.Database(entities = {QRCode.class, Session.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public static Database instance;
    private static final String DATABASE_NAME = "user-database";

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract QRCodeDao qrCodeDao();
    public abstract SessionDao sessionDao();
}
