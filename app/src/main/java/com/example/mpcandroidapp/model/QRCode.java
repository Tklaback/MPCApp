package com.example.mpcandroidapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "QRCode",foreignKeys = @ForeignKey(entity = Session.class,
        parentColumns = "_id",
        childColumns = "sessionID",
        onDelete = ForeignKey.CASCADE))
public class QRCode {

    @NonNull
    @PrimaryKey
    private String _id;

    private String site;
    private String fs;
    private String contents;

    public String getSecondaryContents() {
        return secondaryContents;
    }

    public void setSecondaryContents(String secondaryContents) {
        this.secondaryContents = secondaryContents;
    }

    private String secondaryContents;
    private String feature_nums;
    private String easting;
    private String northing;
    private String level;
    private String depth;
    private String mbd;
    private String date;
    private String excavator;
    private String comments;

    private String sessionID;

    public String get_id() {
        return _id;
    }

    public String getSessionID() {
        return sessionID;
    }

    public QRCode(@NonNull String _id, String site, String fs, String contents, String secondaryContents, String feature_nums, String easting, String northing, String level, String depth, String mbd, String date, String excavator, String comments, String sessionID) {
        this._id = _id;
        this.site = site;
        this.fs = fs;
        this.contents = contents;
        this.secondaryContents = secondaryContents;
        this.feature_nums = feature_nums;
        this.easting = easting;
        this.northing = northing;
        this.level = level;
        this.depth = depth;
        this.mbd = mbd;
        this.date = date;
        this.excavator = excavator;
        this.comments = comments;
        this.sessionID = sessionID;
    }


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFeature_nums() {
        return feature_nums;
    }

    public void setFeature_nums(String feature_nums) {
        this.feature_nums = feature_nums;
    }

    public String getEasting() {
        return easting;
    }

    public void setEasting(String easting) {
        this.easting = easting;
    }

    public String getNorthing() {
        return northing;
    }

    public void setNorthing(String northing) {
        this.northing = northing;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getMbd() {
        return mbd;
    }

    public void setMbd(String mbd) {
        this.mbd = mbd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcavator() {
        return excavator;
    }

    public void setExcavator(String excavator) {
        this.excavator = excavator;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
