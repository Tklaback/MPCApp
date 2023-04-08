package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.dao.SessionDao;
import com.example.mpcandroidapp.model.Session;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionActivity extends AppCompatActivity {

    Database db;

    @Override
    public void onResume() {
        super.onResume();

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_session);

        Button addButton = findViewById(R.id.addSession);

        SessionDao sessionDao = db.sessionDao();

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_session);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (recyclerView != null){
                    recyclerView.setLayoutManager(layoutManager);

                    SessionAdapter adapter = new SessionAdapter(recyclerView, DataCache.getInstance().getAllSessions(), SessionActivity.this);
                    recyclerView.setAdapter(adapter);

                    SwipeToDeleteCallback swipeHandler = new SwipeToDeleteCallback(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            }
        };
        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());
            mHandler.sendMessage(Message.obtain());
        }).start();

        addButton.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();

            SessionDialogFragment dialogFragment = new SessionDialogFragment();

            dialogFragment.show(manager, "NEW SESSION");

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_session);

        Button addButton = findViewById(R.id.addSession);

        SessionDao sessionDao = db.sessionDao();

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_session);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (recyclerView != null){
                    recyclerView.setLayoutManager(layoutManager);

                    SessionAdapter adapter = new SessionAdapter(recyclerView, DataCache.getInstance().getAllSessions(), SessionActivity.this);
                    recyclerView.setAdapter(adapter);

                    SwipeToDeleteCallback swipeHandler = new SwipeToDeleteCallback(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            }
        };
        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());

            mHandler.sendMessage(Message.obtain());
        }).start();


        addButton.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();

            SessionDialogFragment dialogFragment = new SessionDialogFragment();

            dialogFragment.show(manager, "NEW SESSION");

        });

    }

}