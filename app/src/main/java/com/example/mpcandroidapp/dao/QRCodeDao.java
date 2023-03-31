package com.example.mpcandroidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mpcandroidapp.model.QRCode;

import java.util.List;

@Dao
public interface QRCodeDao {
    @Query("SELECT * FROM QRCode")
    List<QRCode> getAll();

    @Query("SELECT * FROM QRCode WHERE sessionID = sessionID")
    List<QRCode> loadAllInSession(String sessionID);

    @Insert
    void addQRCode(QRCode qrCode);

    @Delete
    void delete(QRCode qrCode);
}