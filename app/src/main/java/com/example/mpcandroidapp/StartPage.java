package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.SessionDao;
import com.example.mpcandroidapp.model.Session;


import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartPage extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_start_page);

        Button addButton = findViewById(R.id.addJson);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SessionDao sessionDao = db.sessionDao();

//        executorService.submit(() -> {
//            DataCache.getInstance().setAllSessions(sessionDao.getAll());
//            RecyclerView recyclerView = findViewById(R.id.recycler_view);
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//
//
//            CustomAdapter adapter = new CustomAdapter(DataCache.getInstance().getAllSessions());
//            recyclerView.setAdapter(adapter);
//        });
//        executorService.shutdown();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataCache.getInstance().setAllSessions(sessionDao.getAll());
                RecyclerView recyclerView = findViewById(R.id.recycler_view);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);


                CustomAdapter adapter = new CustomAdapter(DataCache.getInstance().getAllSessions());
                recyclerView.setAdapter(adapter);
            }
        }).start();


        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);

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