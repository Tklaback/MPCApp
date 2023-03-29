package com.example.mpcandroidapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataInputFragment} factory method to
 * create an instance of this fragment.
 */
public class DataInputFragment extends Fragment {
    private String site, fs, contents, feature_nums, easting, northing, level, depth, mbd, date,
            excavator, comments;

    ImageView qrImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_input, container, false);

        qrImage = view.findViewById(R.id.qrImage);

        EditText siteInput = view.findViewById(R.id.site);
        EditText contentsInput = view.findViewById(R.id.contents);
//        EditText fs = view.findViewById(R.id.);
        EditText featureNumsInput = view.findViewById(R.id.feature_nums);
        EditText eastInput = view.findViewById(R.id.east);
        EditText northInput = view.findViewById(R.id.north);
        EditText levelInput = view.findViewById(R.id.level);
        EditText depthInput = view.findViewById(R.id.depth);
        EditText mbdInput = view.findViewById(R.id.mbd);
        EditText dateInput = view.findViewById(R.id.date);
        EditText excavatorInput = view.findViewById(R.id.excavator);
        EditText commentsInput = view.findViewById(R.id.comments);


        Button submitButton = view.findViewById(R.id.submit);

        submitButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                site = siteInput.getText().toString();
                contents = contentsInput.getText().toString();
                feature_nums = featureNumsInput.getText().toString();
                easting = eastInput.getText().toString();
                northing = northInput.getText().toString();
                level = levelInput.getText().toString();
                depth = depthInput.getText().toString();
                mbd = mbdInput.getText().toString();
                date = dateInput.getText().toString();
                excavator = excavatorInput.getText().toString();
                comments = commentsInput.getText().toString();

                submitButton.setEnabled(!site.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                if (bundle != null && !bundle.isEmpty()){
                    JSONObject json = new JSONObject();
                    String uuid = bundle.getString("_id");
                    Bitmap myReceivedBitmap = (Bitmap) message.obj;
                    try {
                        json.put("_id", uuid);
                        json.put("site", site);
                        json.put("contents", contents);
                        json.put("feature_nums", feature_nums);
                        json.put("easting", easting);
                        json.put("northing", northing);
                        json.put("level", level);
                        json.put("depth", depth);
                        json.put("mbd", mbd);
                        json.put("date", date);
                        json.put("excavator", excavator);
                        json.put("comments", comments);

                        qrImage.setImageBitmap(myReceivedBitmap);

                        Log.d("SUCCESS", bundle.getString("_id"));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        siteInput.addTextChangedListener(textWatcher);
        contentsInput.addTextChangedListener(textWatcher);
        featureNumsInput.addTextChangedListener(textWatcher);
        eastInput.addTextChangedListener(textWatcher);
        northInput.addTextChangedListener(textWatcher);
        levelInput.addTextChangedListener(textWatcher);
        depthInput.addTextChangedListener(textWatcher);
        mbdInput.addTextChangedListener(textWatcher);
        dateInput.addTextChangedListener(textWatcher);
        excavatorInput.addTextChangedListener(textWatcher);
        commentsInput.addTextChangedListener(textWatcher);

        submitButton.setOnClickListener(v -> {
            UUID uuid = UUID.randomUUID();

            CreateQRCode createQRCode = new CreateQRCode(handler, qrImage, uuid.toString());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(createQRCode);

        });

        return view;
    }

    private static class CreateQRCode implements Runnable {

        private final Handler messageHandler;

        String uuid;

        ImageView qrImage;

        public CreateQRCode(Handler handler, ImageView view, String uuid){

            this.uuid = uuid;

            this.messageHandler = handler;

            this.qrImage = view;

        }

        @Override
        public void run(){
            Message message = new Message();
            Bundle bundle = new Bundle();

            JSONObject json_data = new JSONObject();
            try {
                json_data.put("_id", uuid);

                Log.d("json_data", json_data.toString());
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    Log.d("json data", String.valueOf(json_data));
                    BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(json_data), BarcodeFormat.QR_CODE, 400, 400);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                    saveQR(bitmap);
//                    qrImage.setImageBitmap(bitmap);


                    bundle.putString("_id", uuid);
                    message.obj = bitmap;
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
