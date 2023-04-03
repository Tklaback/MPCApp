package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class QRCodeActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_qrcode);

        Button addQRCodeButton = findViewById(R.id.addQRCode);

        Button sessionButton = findViewById(R.id.sessionBack);

        Button exportButton = findViewById(R.id.export);

        new Thread(() -> {

            QRCodeDao qrCodeDao = Database.getInstance(this).qrCodeDao();

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

        sessionButton.setOnClickListener(v -> {

            Intent intent = new Intent(QRCodeActivity.this, SessionActivity.class);

            startActivity(intent);

            finish();
        });

        exportButton.setOnClickListener(v -> sendData());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataCache.getInstance().setCurQRCode(null); //Needed so curQRcode does not stay the same

        setContentView(R.layout.activity_qrcode);

        Button addQRCodeButton = findViewById(R.id.addQRCode);

        Button sessionButton = findViewById(R.id.sessionBack);

        QRCodeDao qrCodeDao = Database.getInstance(this).qrCodeDao();

        Button exportButton = findViewById(R.id.export);

        new Thread(() -> {
            DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(DataCache.getInstance().getCurSession().get_id()));
            RecyclerView recyclerView = findViewById(R.id.recycler_qr_code);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            if (recyclerView != null) {
                recyclerView.setLayoutManager(layoutManager);

                QRCodeAdapter adapter = new QRCodeAdapter(DataCache.getInstance().getSessionQRCodes(), this);
                recyclerView.setAdapter(adapter);
            }
        }).start();

        addQRCodeButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        });

        sessionButton.setOnClickListener(v -> {

            Intent intent = new Intent(QRCodeActivity.this, SessionActivity.class);

            startActivity(intent);

            finish();
        });

        exportButton.setOnClickListener(v -> sendData());
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

    private void sendData(){
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Bundle bundle = message.getData();
                boolean success = bundle.getBoolean("SUCCESS");
                if (success)
                    Toast.makeText(getApplicationContext(), "Data Successfully Sent To Server",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Error: Failure Connecting to Server",
                        Toast.LENGTH_SHORT).show();
            }
        };
        new Thread(() -> {
            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            String serverAddress = "192.168.1.13"; // Replace with the IP address or hostname of your server
            int serverPort = 65432; // Replace with the port number of your server
            try {
                Socket socket = new Socket(serverAddress, serverPort);

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("Hello, server!".getBytes());

                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String response = new String(buffer, 0, bytesRead);
                Log.d("RECEIVED DATA", "Received response from server: " + response);

                bundle.putBoolean("SUCCESS", true);
                message.setData(bundle);
                mHandler.sendMessage(message);

                socket.close();

            } catch (IOException e) {
                bundle.putBoolean("SUCCESS", false);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }).start();

    }
}