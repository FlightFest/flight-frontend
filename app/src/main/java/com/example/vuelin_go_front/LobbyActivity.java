package com.example.vuelin_go_front;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vuelin_go_front.R;

public class LobbyActivity extends AppCompatActivity {

    private TextView countdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        countdownTimer = findViewById(R.id.countdown_timer);

        // Set the countdown timer for 15 minutes (900,000 milliseconds)
        new CountDownTimer(900000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Update the countdown timer every second
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdownTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                // Do something when the countdown timer finishes
            }

        }.start();
    }
}

