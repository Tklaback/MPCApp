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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mpcandroidapp.dao.Database;
import com.example.mpcandroidapp.model.Session;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServerDialog} factory method to
 * create an instance of this fragment.
 */
public class ServerDialog extends DialogFragment {

    Database db;

    EditText ipAddress, port;

    public ServerDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_server_dialog, null);

        Context context = getContext();
        db = Database.getInstance(context);

        ipAddress = view.findViewById(R.id.ipAddress);
        port = view.findViewById(R.id.port);

        return new AlertDialog.Builder(getActivity())
                .setTitle("SERVER INFO")
                .setView(R.layout.fragment_server_dialog)
                .setPositiveButton("OK", (dialog, which) -> {
                    sendData(ipAddress.getText().toString(), port.getText().toString(), view);
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
        return inflater.inflate(R.layout.fragment_server_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            // Find the EditText view in the dialog
            ipAddress = dialog.findViewById(R.id.ipAddress);
            port = dialog.findViewById(R.id.port);
        }
    }

    private void sendData(String serverAddress, String serverPort, View v){
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Bundle bundle = message.getData();
                boolean success = bundle.getBoolean("SUCCESS");
                if (success)
                    Toast.makeText(v.getContext(), "Data Successfully Sent To Server",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(v.getContext(), "Error: Failure Connecting to Server",
                            Toast.LENGTH_SHORT).show();
            }
        };
        new Thread(() -> {
            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            try {
                if (serverPort != null){
                    Socket socket = new Socket(serverAddress, Integer.parseInt(serverPort));

                    Gson gson = new Gson();

                    String json = gson.toJson(DataCache.getInstance().getSessionQRCodes());

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(json.getBytes());

                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String response = new String(buffer, 0, bytesRead);
                    Log.d("RECEIVED DATA", "Received response from server: " + response);

                    bundle.putBoolean("SUCCESS", true);
                    message.setData(bundle);
                    mHandler.sendMessage(message);

                    socket.close();
                }

            } catch (Exception e) {
                bundle.putBoolean("SUCCESS", false);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }).start();

    }
}
