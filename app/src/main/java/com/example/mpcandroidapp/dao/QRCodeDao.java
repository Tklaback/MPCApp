package com.example.mpcandroidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mpcandroidapp.model.QRCode;

import java.util.List;

@Dao
public interface QRCodeDao {
    @Query("SELECT * FROM QRCode")
    List<QRCode> getAll();

    @Query("SELECT * FROM QRCode WHERE sessionID = (:ID)")
    List<QRCode> loadAllInSession(String ID);

    @Query("SELECT * FROM QRCode WHERE sessionID = (:QRCodeID)")
    QRCode getQRCode(String QRCodeID);

    @Insert
    void addQRCode(QRCode qrCode);

    @Delete
    void delete(QRCode qrCode);

    @Query("UPDATE QRCode SET site = :newSiteValue, " +
            "contents = :contents,  feature_nums = :featureNums," +
            "easting = :easting, northing = :northing," +
            "level = :level, depth = :depth, " +
            "mbd = :mbd, date = :date," +
            "excavator = :excavator, comments = :comments WHERE _id = :QRCodeID")
    void updateQRCodeSite(String newSiteValue, String contents,
                          String featureNums, String easting,
                          String northing, String level,
                          String depth, String mbd,
                          String date, String excavator,
                          String comments,
                          String QRCodeID);

}