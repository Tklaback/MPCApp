package com.example.mpcandroidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SessionDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("The Dialog")
                .setMessage("PRESS OK OR CANCEL")
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(getActivity(), "YOU PRESSED OK", Toast.LENGTH_SHORT).show();
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
}