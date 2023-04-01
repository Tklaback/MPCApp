package com.example.mpcandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpcandroidapp.model.QRCode;
import com.example.mpcandroidapp.model.Session;

import java.util.List;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
    }

    public static class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {

        private List<Session> localDataSet;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                view.setOnClickListener(this);

                textView = view.findViewById(R.id.textView);
            }

            private void bind(Session session){
                getTextView().setText(session.get_id());
            }

            public TextView getTextView() {
                return textView;
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(getTextView().getContext(), "HELLO THERE", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public QRCodeAdapter(List<Session> dataSet) {
            localDataSet = dataSet;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_layout,
                    viewGroup, false);
            return new ViewHolder(itemView);
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