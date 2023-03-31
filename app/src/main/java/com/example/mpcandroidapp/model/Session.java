package com.example.mpcandroidapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity(tableName = "Session")
public class Session {
    @PrimaryKey(autoGenerate = false)
    private String _id;

    @ColumnInfo(name = "date")
    private String date;

    public Session(String _id, String date) {
        this._id = _id;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
