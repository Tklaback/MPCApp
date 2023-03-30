package com.example.mpcandroidapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrintFragment} factory method to
 * create an instance of this fragment.
 */
public class PrintFragment extends Fragment {

    String displaySite;

    Bitmap qrCode;

    ImageView qrImage;


    public PrintFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_print, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                qrImage = view.findViewById(R.id.qrImage);
                TextView siteView = view.findViewById(R.id.displaySite);
                JSONObject jsonObject = new JSONObject(bundle.getString("jsonObject"));
                displaySite = jsonObject.getString("site");
                qrCode = bundle.getParcelable("bitmap");


                qrImage.setImageBitmap(qrCode);
                siteView.setText(displaySite);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return view;
    }

}