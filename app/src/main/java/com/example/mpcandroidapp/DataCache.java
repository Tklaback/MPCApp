package com.example.mpcandroidapp;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

import java.util.List;

public class DataCache {

    private static final DataCache instance = new DataCache();

    private DataCache(){};

    public static DataCache getInstance(){
        return instance;
    }

    public Session getCurSession() {
        return this.curSession;
    }

    public void setCurSession(Session curSession) {
        this.curSession = curSession;
    }

    private Session curSession;

    public QRCode getCurQRCode() {
        return curQRCode;
    }

    public void setCurQRCode(QRCode curQRCode) {
        this.curQRCode = curQRCode;
    }

    private QRCode curQRCode;

    public List<Session> getAllSessions() {
        return allSessions;
    }

    public void setAllSessions(List<Session> allSessions) {
        this.allSessions = allSessions;
    }

    List<Session> allSessions;
}
