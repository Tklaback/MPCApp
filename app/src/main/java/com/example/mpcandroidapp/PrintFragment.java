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


    String displaySite, displayContents, displayFeatureNums, displayEasting, displayNorthing,
            displayLevel, displayDepth, displayMBD, displayDate, displayExcavator, displayComments;

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
                TextView contentView = view.findViewById(R.id.displayContent);
                TextView featureNumView = view.findViewById(R.id.displayFeatureNums);
                TextView eastView = view.findViewById(R.id.displayEast);
                TextView northView = view.findViewById(R.id.displayNorth);
                TextView levelView = view.findViewById(R.id.displayLevel);
                TextView depthView = view.findViewById(R.id.displayDepth);
                TextView mbdView = view.findViewById(R.id.displayMBD);
                TextView dateView = view.findViewById(R.id.displayDate);
                TextView excavatorView = view.findViewById(R.id.displayExcavator);
                TextView commentsView = view.findViewById(R.id.displayComments);

                JSONObject jsonObject = new JSONObject(bundle.getString("jsonObject"));


                displaySite = jsonObject.getString("site");
                displayContents = jsonObject.getString("contents");
                displayFeatureNums = jsonObject.getString("feature_nums");
                displayEasting = jsonObject.getString("easting");
                displayNorthing = jsonObject.getString("northing");
                displayLevel = jsonObject.getString("level");
                displayDepth = jsonObject.getString("depth");
                displayMBD = jsonObject.getString("mbd");
                displayDate = jsonObject.getString("date");
                displayExcavator = jsonObject.getString("excavator");
                displayComments = jsonObject.getString("comments");

                qrCode = bundle.getParcelable("bitmap");

                qrImage.setImageBitmap(qrCode);
                siteView.setText(displaySite);
                contentView.setText(displayContents);
                featureNumView.setText(displayFeatureNums);
                eastView.setText(displayEasting);
                northView.setText(displayNorthing);
                depthView.setText(displayDepth);
                levelView.setText(displayLevel);
                mbdView.setText(displayMBD);
                dateView.setText(displayDate);
                excavatorView.setText(displayExcavator);
                commentsView.setText(displayComments);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return view;
    }

}