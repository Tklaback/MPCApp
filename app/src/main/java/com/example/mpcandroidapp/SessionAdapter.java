package com.example.mpcandroidapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.dao.QRCodeDao;
import com.example.mpcandroidapp.model.Session;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private final List<Session> localDataSet;
    private final Context context;
    RecyclerView recyclerView;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;
        private final Button deleteButton;
        private Session curSession;
        private final Context mContext;
        private final SessionAdapter adapter;

        public ViewHolder(SessionAdapter adapter, View view, Context context) {
            super(view);

            this.adapter= adapter;
            this.mContext = context;
            view.setOnClickListener(this);

            textView = view.findViewById(R.id.textView);
            deleteButton = view.findViewById(R.id.delete);
            deleteButton.setOnClickListener(v -> {
                adapter.removeAt(getAdapterPosition());
            });
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
    public SessionAdapter(RecyclerView recyclerView, List<Session> dataSet, Context context) {
        this.context = context;
        localDataSet = dataSet;
        this.recyclerView = recyclerView;
    }

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
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
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

//                recyclerView.getLayoutManager().requestLayout();
                notifyItemRemoved(position);
            }
        };
        new Thread(() -> {
            Database.getInstance(context).sessionDao().delete(localDataSet.get(position));

            mHandler.sendMessage(Message.obtain());
        }).start();

    }
}