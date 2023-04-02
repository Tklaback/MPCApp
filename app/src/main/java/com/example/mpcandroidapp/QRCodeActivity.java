package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

import java.util.List;
import java.util.UUID;

public class QRCodeActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_qrcode);

        Button addQRCodeButton = findViewById(R.id.addQRCode);

        QRCodeDao qrCodeDao = Database.getInstance(this).qrCodeDao();

        new Thread(() -> {

            DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(DataCache.getInstance().getCurSession().get_id()));
            RecyclerView recyclerView = findViewById(R.id.recycler_qr_code);

            if (recyclerView != null){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                QRCodeAdapter adapter = new QRCodeAdapter(DataCache.getInstance().getSessionQRCodes(), this);
                recyclerView.setAdapter(adapter);
            }

        }).start();

        addQRCodeButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataCache.getInstance().setCurQRCode(null); //Needed so curQRcode does not stay the same

        setContentView(R.layout.activity_qrcode);

        Button addQRCodeButton = findViewById(R.id.addQRCode);

        QRCodeDao qrCodeDao = Database.getInstance(this).qrCodeDao();

        new Thread(() -> {
            DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(DataCache.getInstance().getCurSession().get_id()));
            RecyclerView recyclerView = findViewById(R.id.recycler_qr_code);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            QRCodeAdapter adapter = new QRCodeAdapter(DataCache.getInstance().getSessionQRCodes(), this);
            recyclerView.setAdapter(adapter);
        }).start();

        addQRCodeButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        });
    }

    public static class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {

        private final List<QRCode> localDataSet;
        private final Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;
            private final Context mContext;
            private QRCode curQRCode;

            public ViewHolder(View view, Context context) {
                super(view);
                // Define click listener for the ViewHolder's View
                view.setOnClickListener(this);
                this.mContext = context;
                textView = view.findViewById(R.id.textView);
            }

            private void bind(QRCode qrCode){
                this.curQRCode = qrCode;
                getTextView().setText(qrCode.getSessionID());
                //For debugging to make sure each qrCode is of same session
            }

            public TextView getTextView() {
                return textView;
            }

            @Override
            public void onClick(View v) {
                new Thread(()->{
                    DataCache.getInstance().setCurQRCode(curQRCode);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);

                }).start();
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public QRCodeAdapter(List<QRCode> dataSet, Context context) {
            localDataSet = dataSet;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_layout,
                    viewGroup, false);
            return new ViewHolder(itemView, context);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.bind(localDataSet.get(position));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}