package com.example.mpcandroidapp.dao;


import androidx.room.RoomDatabase;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

@androidx.room.Database(entities = {QRCode.class, Session.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract QRCodeDao qrCodeDao();
    public abstract SessionDao sessionDao();
}
