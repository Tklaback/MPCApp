package com.example.mpcandroidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.model.QRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataInputFragment} factory method to
 * create an instance of this fragment.
 */
public class DataInputFragment extends Fragment {
    Contents contentsOptions;
    private String site, fs, contents, feature_nums, easting, northing, level, depth, mbd, date,
            excavator, comments, secondaryContents;

    Database db;

    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setEqual(String ...strings){
        site = strings[0];
        contents = strings[1];
        feature_nums = strings[2];
        easting = strings[3];
        northing = strings[4];
        level = strings[5];
        depth = strings[6];
        mbd = strings[7];
        date = strings[8];
        excavator = strings[9];
        comments = strings[10];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentsOptions = new Contents(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_input, container, false);

        fragmentManager = requireActivity().getSupportFragmentManager();

        EditText siteInput = view.findViewById(R.id.site);
        Spinner contentsInput = view.findViewById(R.id.contents);
        Spinner secondaryContentsInput = view.findViewById(R.id.secondaryContents);
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

        Button submitButton = view.findViewById(R.id.print);

        Button backToQR = view.findViewById(R.id.qrCodeBack);

        submitButton.setEnabled(false);

        ArrayList<String> primaryContents = new ArrayList<>(contentsOptions.getItems().keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, primaryContents);
        contentsInput.setAdapter(adapter);


        if (DataCache.getInstance().getCurQRCode() != null){
            QRCode qrCode = DataCache.getInstance().getCurQRCode();
            siteInput.setText(qrCode.getSite());
            int pos = primaryContents.indexOf(qrCode.getContents());
            contentsInput.setSelection(pos, true);
            featureNumsInput.setText(qrCode.getFeature_nums());
            eastInput.setText(qrCode.getEasting());
            northInput.setText(qrCode.getNorthing());
            levelInput.setText(qrCode.getLevel());
            depthInput.setText(qrCode.getDepth());
            mbdInput.setText(qrCode.getMbd());
            dateInput.setText(qrCode.getDate());
            excavatorInput.setText(qrCode.getExcavator());
            commentsInput.setText(qrCode.getComments());

            setEqual(siteInput.getText().toString(), qrCode.getContents(), qrCode.getSecondaryContents(), featureNumsInput.getText().toString()
                    ,eastInput.getText().toString(), northInput.getText().toString(), levelInput.getText().toString(), depthInput.getText().toString()
                    , mbdInput.getText().toString(), dateInput.getText().toString(), excavatorInput.getText().toString()
                    ,commentsInput.getText().toString());

            submitButton.setEnabled(true);

            submitButton.setEnabled(!site.isEmpty() && contents != null && secondaryContents != null && !feature_nums.isEmpty() &&
                    !easting.isEmpty() && !northing.isEmpty() && !level.isEmpty() && !depth.isEmpty() &&
                    !mbd.isEmpty() && !date.isEmpty() && !excavator.isEmpty());
        }

        contentsInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contents = parent.getItemAtPosition(position).toString();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, contentsOptions.getItems().get(contents));
                secondaryContentsInput.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        secondaryContentsInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondaryContents = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setEqual(siteInput.getText().toString(), (String) contentsInput.getSelectedItem(), (String) secondaryContentsInput.getSelectedItem(), featureNumsInput.getText().toString()
                        ,eastInput.getText().toString(), northInput.getText().toString(), levelInput.getText().toString(), depthInput.getText().toString()
                        , mbdInput.getText().toString(), dateInput.getText().toString(), excavatorInput.getText().toString()
                        ,commentsInput.getText().toString());

                submitButton.setEnabled(!site.isEmpty() && contents != null && secondaryContents != null && !feature_nums.isEmpty() &&
                        !easting.isEmpty() && !northing.isEmpty() && !level.isEmpty() && !depth.isEmpty() &&
                        !mbd.isEmpty() && !date.isEmpty() && !excavator.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        db = Database.getInstance(requireActivity().getApplicationContext());

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                if (bundle != null && !bundle.isEmpty()){
                    Bitmap myReceivedBitmap = (Bitmap) message.obj;

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PrintFragment printFragment = new PrintFragment();

                    Bundle newBundle = new Bundle();

                    newBundle.putParcelable("bitmap", myReceivedBitmap);

                    printFragment.setArguments(newBundle);
                    fragmentTransaction.replace(R.id.fragmentFrameLayout, printFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        };

        siteInput.addTextChangedListener(textWatcher);
        featureNumsInput.addTextChangedListener(textWatcher);
        eastInput.addTextChangedListener(textWatcher);
        northInput.addTextChangedListener(textWatcher);
        levelInput.addTextChangedListener(textWatcher);
        depthInput.addTextChangedListener(textWatcher);
        mbdInput.addTextChangedListener(textWatcher);
        dateInput.addTextChangedListener(textWatcher);
        excavatorInput.addTextChangedListener(textWatcher);
        commentsInput.addTextChangedListener(textWatcher);

        backToQR.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), QRCodeActivity.class);

            startActivity(intent);

        });

        submitButton.setOnClickListener(v -> {
            if (DataCache.getInstance().getCurQRCode() == null){
                UUID uuid = UUID.randomUUID();

                QRCode qrCode = new QRCode(uuid.toString(), site, null, contents, secondaryContents, feature_nums, easting, northing,
                        level, depth, mbd, date, excavator, comments, DataCache.getInstance().getCurSession().get_id());

                DataCache.getInstance().setCurQRCode(qrCode);

                CreateQRCode createQRCode = new CreateQRCode(handler, db);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(createQRCode);
            }else{
                Bitmap myReceivedBitmap;
                try {
                    myReceivedBitmap = submitHandler();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PrintFragment printFragment = new PrintFragment();

                    Bundle newBundle = new Bundle();

                    new Thread(() -> {
                        QRCodeDao qrCodeDao = db.qrCodeDao();
                        qrCodeDao.updateQRCodeSite(site, contents, secondaryContents, feature_nums, easting,
                                northing, level, depth, mbd, date, excavator, comments,
                                DataCache.getInstance().getCurQRCode().get_id());

                        QRCode curQRCode = DataCache.getInstance().getCurQRCode();
                        QRCode newQRCode = qrCodeDao.getQRCode(curQRCode.get_id());
                        DataCache.getInstance().setCurQRCode(newQRCode);
                        newBundle.putParcelable("bitmap", myReceivedBitmap);

                        printFragment.setArguments(newBundle);
                        fragmentTransaction.replace(R.id.fragmentFrameLayout, printFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }).start();

                } catch (JSONException | WriterException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        return view;
    }

    private static class CreateQRCode implements Runnable {

        private final Handler messageHandler;

        QRCodeDao qrCodeDao;

        Database db;


        public CreateQRCode(Handler handler, Database db){

            this.messageHandler = handler;

            qrCodeDao = db.qrCodeDao();

            this.db = db;

        }

        @Override
        public void run(){
            Message message = new Message();
            Bundle bundle = new Bundle();

            try {
                qrCodeDao.addQRCode(DataCache.getInstance().getCurQRCode());

                bundle.putString("success", "true");
                message.obj = DataInputFragment.submitHandler();
                message.setData(bundle);
                messageHandler.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap submitHandler() throws JSONException, WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        JSONObject json_data = new JSONObject();
        json_data.put("_id", DataCache.getInstance().getCurQRCode().get_id());
        BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(json_data), BarcodeFormat.QR_CODE, 400, 400);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        Log.d("json_data", json_data.toString());
        return bitmap;
    }
//
//    public class MyAdapter extends ArrayAdapter<MyEnum> {
//
//        public MyAdapter (Context context) {
//            super(context, 0, MyEnum.values());
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            CheckedTextView text= (CheckedTextView) convertView;
//
//            if (text== null) {
//                text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,  parent, false);
//            }
//
//            text.setText(getItem(position).getDescriptionResourceId());
//
//            return text;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            CheckedTextView text = (CheckedTextView) convertView;
//
//            if (text == null) {
//                text = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item,  parent, false);
//            }
//
//            text.setText(getItem(position).getTitle());
//
//            return text;
//        }
//    }

}
