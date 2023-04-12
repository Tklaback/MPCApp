package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_qr_code);

                if (recyclerView != null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);

                    QRCodeAdapter adapter = new QRCodeAdapter(recyclerView, DataCache.getInstance().getSessionQRCodes(), QRCodeActivity.this);
                    recyclerView.setAdapter(adapter);

                    QRCodeSwipe swipeHandler = new QRCodeSwipe(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            }
        };
        new Thread(() -> {
            Message message = Message.obtain();
            QRCodeDao qrCodeDao = Database.getInstance(this).qrCodeDao();

            DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(DataCache.getInstance().getCurSession().get_id()));

            mHandler.sendMessage(message);
        }).start();

        // TODO: BUILD DIALOG TO ENTER IP AND PORT


        addQRCodeButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        });

        sessionButton.setOnClickListener(v -> {

            Intent intent = new Intent(QRCodeActivity.this, SessionActivity.class);

            startActivity(intent);

            finish();
        });

        exportButton.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();

            ServerDialog dialogFragment = new ServerDialog();

            dialogFragment.show(manager, "NEW SESSION");
        });
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


        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_qr_code);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (recyclerView != null) {
                    recyclerView.setLayoutManager(layoutManager);

                    QRCodeAdapter adapter = new QRCodeAdapter(recyclerView, DataCache.getInstance().getSessionQRCodes(), QRCodeActivity.this);
                    recyclerView.setAdapter(adapter);

                    QRCodeSwipe swipeHandler = new QRCodeSwipe(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }

            }
        };
        new Thread(() -> {
            DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(DataCache.getInstance().getCurSession().get_id()));

            mHandler.sendMessage(Message.obtain());
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

        exportButton.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();

            ServerDialog dialogFragment = new ServerDialog();

            dialogFragment.show(manager, "NEW SESSION");

        });
    }

    public static class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {

        private final List<QRCode> localDataSet;
        private final Context context;
        RecyclerView recyclerView;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;
            private final Context mContext;
            private QRCode curQRCode;
            private final QRCodeAdapter adapter;

            public ViewHolder(QRCodeAdapter adapter, View view, Context context) {
                super(view);
                // Define click listener for the ViewHolder's View
                view.setOnClickListener(this);
                this.mContext = context;
                this.adapter = adapter;
                textView = view.findViewById(R.id.textView);
            }

            private void bind(QRCode qrCode) {
                this.curQRCode = qrCode;
                String displayString = qrCode.getSite() + "_" + qrCode.getContents().substring(0, 4)
                        + "_" + qrCode.getDate();
                getTextView().setText(displayString);
            }

            public TextView getTextView() {
                return textView;
            }

            @Override
            public void onClick(View v) {
                new Thread(() -> {
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
         *                by RecyclerView
         */
        public QRCodeAdapter(RecyclerView recyclerView, List<QRCode> dataSet, Context context) {
            localDataSet = dataSet;
            this.context = context;
            this.recyclerView = recyclerView;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_layout,
                    viewGroup, false);
            return new ViewHolder(this, itemView, context);
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

        public void removeAt(int position) {
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    localDataSet.remove(position);
                    recyclerView.removeViewAt(position);
                    notifyItemRemoved(position);
                }
            };
            new Thread(() -> {
                Database.getInstance(context).qrCodeDao().delete(localDataSet.get(position));

                mHandler.sendMessage(Message.obtain());
            }).start();

        }
    }

    private static class QRCodeSwipe extends ItemTouchHelper.SimpleCallback {
        private final QRCodeAdapter mAdapter;

        public QRCodeSwipe(QRCodeAdapter adapter) {
            super(0, ItemTouchHelper.RIGHT);
            mAdapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.removeAt(position);
        }
    }
}
