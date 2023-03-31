package com.example.mpcandroidapp.dao;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

import java.util.List;

public class SessionWithCodes {
    @Embedded public Session session;
    @Relation(
            parentColumn = "_id",
            entityColumn = "sessionID"
    )
    public List<QRCode> qrCodes;
}