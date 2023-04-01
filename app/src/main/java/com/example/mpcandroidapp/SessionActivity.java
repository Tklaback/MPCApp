package com.example.mpcandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.SessionDao;
import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;


import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_session);

        Button addButton = findViewById(R.id.addJson);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SessionDao sessionDao = db.sessionDao();

        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());
            RecyclerView recyclerView = findViewById(R.id.recycler_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);


            CustomAdapter adapter = new CustomAdapter(DataCache.getInstance().getAllSessions());
            recyclerView.setAdapter(adapter);
        }).start();


        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, QRCodeActivity.class);

            Session session = new Session(UUID.randomUUID().toString(), java.time.LocalDate.now().toString());

            executorService.submit(() -> {
                sessionDao.insert(session);
                DataCache.getInstance().setCurSession(session);
            });
            executorService.shutdown();

            startActivity(intent);
        });

    }
}