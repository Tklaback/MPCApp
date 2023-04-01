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

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SessionDao sessionDao = db.sessionDao();

        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());
            RecyclerView recyclerView = findViewById(R.id.recycler_session);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            SessionAdapter adapter = new SessionAdapter(DataCache.getInstance().getAllSessions(), this);
            recyclerView.setAdapter(adapter);
        }).start();

        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, QRCodeActivity.class);

            Session session = new Session(UUID.randomUUID().toString(), java.time.LocalDate.now().toString());

            executorService.submit(() -> {
                sessionDao.insert(session);
                DataCache.getInstance().setCurSession(session);
            });
            executorService.shutdown();

            startActivity(intent);
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

        new Thread(() -> {
            DataCache.getInstance().setAllSessions(sessionDao.getAll());
            RecyclerView recyclerView = findViewById(R.id.recycler_session);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            SessionAdapter adapter = new SessionAdapter(DataCache.getInstance().getAllSessions(), this);
            recyclerView.setAdapter(adapter);
        }).start();


        addButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, QRCodeActivity.class);

            Session session = new Session(UUID.randomUUID().toString(), java.time.LocalDate.now().toString());

            executorService.submit(() -> {
                sessionDao.insert(session);
                DataCache.getInstance().setCurSession(session);
            });
            executorService.shutdown();

            startActivity(intent);
        });

    }
    public static class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

        private List<Session> localDataSet;
        private Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;
            private Session curSession;
            private Context mContext;

            public ViewHolder(View view, Context context) {
                super(view);
                // Define click listener for the ViewHolder's View
                this.mContext = context;
                view.setOnClickListener(this);

                textView = view.findViewById(R.id.textView);
            }

            private void bind(Session session){
                curSession = session;
                getTextView().setText(session.get_id());
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

                Toast.makeText(getTextView().getContext(), "HELLO THERE", Toast.LENGTH_LONG).show();
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