package com.example.mpcandroidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.os.StrictMode;
import android.widget.Button;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.SessionDao;
import com.example.mpcandroidapp.model.Session;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        addButton.setOnClickListener(v -> {
//            String fileName = "my_json_" + System.currentTimeMillis() + ".json";
//            createJSONFile();
            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("jsonFile", fileName);
            SessionDao sessionDao = db.sessionDao();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
            LocalDate localDate = LocalDate.now();
            Session session = new Session(UUID.randomUUID().toString(), "dtf.format(localDate)");

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                sessionDao.insert(session);
                DataCache.getInstance().setCurSession(session);
            });
            executorService.shutdown();

            db.close();
            startActivity(intent);
        });

    }

    private void createJSONFile(){

    }
}