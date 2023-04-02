package com.example.mpcandroidapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.model.QRCode;

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
//            try {
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

            Button editButton = view.findViewById(R.id.editButton);
            Button activityButton = view.findViewById(R.id.QRCodePage);

            QRCode curQR = DataCache.getInstance().getCurQRCode();

            displaySite = curQR.getSite();
            displayContents = curQR.getContents();
            displayFeatureNums = curQR.getFeature_nums();
            displayEasting = curQR.getEasting();
            displayNorthing = curQR.getNorthing();
            displayLevel = curQR.getLevel();
            displayDepth = curQR.getDepth();
            displayMBD = curQR.getMbd();
            displayDate = curQR.getDate();
            displayExcavator = curQR.getExcavator();
            displayComments = curQR.getComments();

//                JSONObject jsonObject = new JSONObject(bundle.getString("jsonObject"));

//                displaySite = jsonObject.getString("site");
//                displayContents = jsonObject.getString("contents");
//                displayFeatureNums = jsonObject.getString("feature_nums");
//                displayEasting = jsonObject.getString("easting");
//                displayNorthing = jsonObject.getString("northing");
//                displayLevel = jsonObject.getString("level");
//                displayDepth = jsonObject.getString("depth");
//                displayMBD = jsonObject.getString("mbd");
//                displayDate = jsonObject.getString("date");
//                displayExcavator = jsonObject.getString("excavator");
//                displayComments = jsonObject.getString("comments");

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

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onBackPressed() to handle the back button press
                    requireActivity().onBackPressed();
                }
            });


            /*
            * TODO: Make it so that second button
            *   goes back to QRCodeActivity from printFragment.
            * */

            activityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getActivity().onBackPressed();

                }
            });

//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
        }

        return view;
    }

}