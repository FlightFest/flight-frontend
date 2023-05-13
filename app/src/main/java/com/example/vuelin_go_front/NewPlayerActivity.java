package com.example.vuelin_go_front;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.example.vuelin_go_front.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NewPlayerActivity extends Activity {

    private static final String SERVER_IP = "192.168.0.100";
    private static final int SERVER_PORT = 8080;

    private Socket socket;
    private OutputStream outputStream;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.home);

        input = (EditText) findViewById(R.id.player_name_input);

        // Connect to server and send player name
        connectToServer();
    }

    public void connectToServer() {
        setContentView(R.layout.home);

        input = (EditText) findViewById(R.id.player_name_input);

        // start background thread to connect to server
        new ConnectTask().execute();
    }


    private class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Create socket connection to server
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = socket.getOutputStream();

                // get name from input text
                String playerName = input.getText().toString(); // input text value

                // send name to server
                byte[] playerNameBytes = playerName.getBytes();
                outputStream.write(playerNameBytes);
                outputStream.flush();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
