package com.example.mpcandroidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mpcandroidapp.model.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM Session")
    List<Session> getAll();

    @Transaction
    @Query("SELECT * FROM Session")
    List<SessionWithCodes> getUsersWithPlaylists();

    @Insert
    void insert(Session session);

    @Delete
    void delete(Session session);
}