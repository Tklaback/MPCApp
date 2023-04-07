package com.example.mpcandroidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.model.Session;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionDialogFragment extends DialogFragment {

    Database db;
    String smithTri;

    private EditText smithTriInput;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_session_dialog, null);

        smithTriInput = view.findViewById(R.id.smithTri);

        Context context = getContext();
        db = Database.getInstance(context);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Smithsonian Tri.")
                .setView(R.layout.fragment_session_dialog)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), QRCodeActivity.class);

                    String myStr = smithTriInput.getText().toString();

                    Session session = new Session(UUID.randomUUID().toString(), java.time.LocalDate.now().toString(), myStr);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(() -> {
                        db.sessionDao().insert(session);
                        DataCache.getInstance().setCurSession(session);
                    });
                    executorService.shutdown();

                    startActivity(intent);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
                })
                .create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            // Find the EditText view in the dialog
            smithTriInput = dialog.findViewById(R.id.smithTri);
        }
    }
}