package com.example.vuelin_go_front;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NewPlayerActivity extends Activity {

    private static final String SERVER_IP = "192.168.43.127";
    private static final int SERVER_PORT = 8080;

    private Socket socket;
    private OutputStream outputStream;

    private EditText input;
    private Button readyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.home);

        input = findViewById(R.id.player_name_input);
        readyButton = findViewById(R.id.button3);

        // Set OnClickListener for the ready button
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start background thread to connect to server
                new ConnectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                System.out.println("connect");
                // Create socket connection to server
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = socket.getOutputStream();
                System.out.println("socket created");

                // Get name from input text
                String playerName = input.getText().toString();

                // Send name to server
                byte[] type = ("new,"+playerName).getBytes();
                outputStream.write(type);
                outputStream.flush();

                // Close the connection
                socket.close();

                // Switch to lobby activity
                Intent intent = new Intent(NewPlayerActivity.this, LobbyActivity.class);
                startActivity(intent);
                finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
