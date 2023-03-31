package com.example.mpcandroidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_page);

        Button addButton = findViewById(R.id.addJson);

        addButton.setOnClickListener(v -> {
            String fileName = "my_json_" + System.currentTimeMillis() + ".json";
//            createJSONFile();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("jsonFile", fileName);
            startActivity(intent);
        });

    }

    private void createJSONFile(){

    }
}