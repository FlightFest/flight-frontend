package com.example.vuelin_go_front;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vuelin_go_front.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    private static final String SERVER_IP = "192.168.43.127";
    private static final int SERVER_PORT = 8080;
    private Socket socket;
    private OutputStream outputStream;
    private TextView countdownTimer;
    private LinearLayout playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        countdownTimer = findViewById(R.id.countdown_timer);
        playerList = findViewById(R.id.player_list);

        // Set the countdown timer for 10 minutes (600,000 milliseconds)
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Update the countdown timer every second
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdownTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                new SendTimeTask().execute();
                Intent intent = new Intent(LobbyActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

        new GetActivePlayersTask().execute();
    }

    private class GetActivePlayersTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> usernames = new ArrayList<>();
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = socket.getOutputStream();

                byte[] type = "active".getBytes();
                outputStream.write(type);
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();

                byte[] responseBuffer = new byte[1024];

                int bytesRead = inputStream.read(responseBuffer);

                String response = new String(responseBuffer, 0, bytesRead);

                usernames = Arrays.asList(response.split(","));

                socket.close();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            return usernames;
        }

        @Override
        protected void onPostExecute(List<String> usernames) {
            // Clear the existing list of usernames
            playerList.removeAllViews();

            // Add each username to the player list
            for (String username : usernames) {
                TextView usernameView = new TextView(LobbyActivity.this);
                usernameView.setText(username);
                playerList.addView(usernameView);
            }
        }
    }

    private class SendTimeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = socket.getOutputStream();

                byte[] type = "time".getBytes();
                outputStream.write(type);
                outputStream.flush();
                socket.close();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


