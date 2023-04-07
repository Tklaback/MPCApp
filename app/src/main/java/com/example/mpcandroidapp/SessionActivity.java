package com.example.mpcandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.dao.SessionDao;
import com.example.mpcandroidapp.model.Session;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionActivity extends AppCompatActivity {

    Database db;

    @Override
    public void onResume() {
        super.onResume();

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_session);

        Button addButton = findViewById(R.id.addSession);

        SessionDao sessionDao = db.sessionDao();

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_session);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (recyclerView != null){
                    recyclerView.setLayoutManager(layoutManager);

                    SessionAdapter adapter = new SessionAdapter(DataCache.getInstance().getAllSessions(), SessionActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        };
        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());
            mHandler.sendMessage(Message.obtain());
        }).start();

        addButton.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();

            SessionDialogFragment dialogFragment = new SessionDialogFragment();

            dialogFragment.show(manager, "NEW SESSION");

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance(getApplicationContext());

        setContentView(R.layout.activity_session);

        Button addButton = findViewById(R.id.addSession);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SessionDao sessionDao = db.sessionDao();

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                RecyclerView recyclerView = findViewById(R.id.recycler_session);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                if (recyclerView != null){
                    recyclerView.setLayoutManager(layoutManager);

                    SessionAdapter adapter = new SessionAdapter(DataCache.getInstance().getAllSessions(), SessionActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        };
        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());

            mHandler.sendMessage(Message.obtain());
        }).start();


        addButton.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();

            SessionDialogFragment dialogFragment = new SessionDialogFragment();

            dialogFragment.show(manager, "NEW SESSION");

        });

    }
    public static class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

        private final List<Session> localDataSet;
        private final Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;
            private Session curSession;
            private final Context mContext;

            public ViewHolder(View view, Context context) {
                super(view);
                // Define click listener for the ViewHolder's View
                this.mContext = context;
                view.setOnClickListener(this);

                textView = view.findViewById(R.id.textView);
            }

            private void bind(Session session){
                curSession = session;
                getTextView().setText(session.getName());
            }

            public TextView getTextView() {
                return textView;
            }

            @Override
            public void onClick(View v) {
                new Thread(()->{

                    DataCache.getInstance().setCurSession(curSession);
                    QRCodeDao qrCodeDao = Database.getInstance(mContext).qrCodeDao();
                    DataCache.getInstance().setSessionQRCodes(qrCodeDao.loadAllInSession(curSession.get_id()));
                    Intent intent = new Intent(mContext, QRCodeActivity.class);
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
        public SessionAdapter(List<Session> dataSet, Context context) {
            this.context = context;
            localDataSet = dataSet;

        }

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
        public void onBindViewHolder(ViewHolder viewHolder, final int position){
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