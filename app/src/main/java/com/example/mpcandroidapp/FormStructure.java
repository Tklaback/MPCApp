package com.example.mpcandroidapp;

public class FormStructure {
    private String site, fs, contents, feature_nums, easting, northing, level, depth, mbd, date,
            excavator, comments;

    public FormStructure(String site, String contents, String feature_nums, String easting, String northing, String level, String depth, String mbd, String date, String excavator, String comments) {
        this.site = site;
        this.contents = contents;
        this.feature_nums = feature_nums;
        this.easting = easting;
        this.northing = northing;
        this.level = level;
        this.depth = depth;
        this.mbd = mbd;
        this.date = date;
        this.excavator = excavator;
        this.comments = comments;
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
