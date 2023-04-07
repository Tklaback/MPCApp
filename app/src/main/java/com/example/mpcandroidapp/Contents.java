package com.example.mpcandroidapp;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.mpcandroidapp.model.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contents {
    private Context context;

    HashMap<String, ArrayList<String>> data = new HashMap<>();

    public HashMap<String, ArrayList<String>> getItems(){
        return data;
    }

    public Contents(Context context){
        this.context = context;

        parseFile();
    }

    private void parseFile(){

        String dataStr = getData();

        String[] newArr = dataStr.split("\n");

        int itm = 0;
        while (itm < newArr.length){
            String curString = newArr[itm];
            data.put(curString, new ArrayList<>());
            itm++;
            while (itm < newArr.length && newArr[itm].charAt(0) == ' '){
                data.get(curString).add(newArr[itm].substring(1));
                itm++;
            }
        }
    }

    private String getData(){
        try(InputStream inputStream = context.getResources().openRawResource(R.raw.contents_seeder)){
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int num;
            while ((num = inputStream.read(buff)) != -1){
                result.write(buff, 0, num);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}

