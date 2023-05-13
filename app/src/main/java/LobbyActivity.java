import android.app.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;


public class LobbyActivity extends Activity {

    private static final long COUNTDOWN_TIME = 30000; // 30 seconds in milliseconds

    private TextView countdownTimerTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        countdownTimerTextView = findViewById(R.id.countdown_timer);

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                countdownTimerTextView.setText(String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
            }

            @Override
            public void onFinish() {
                // TODO: handle countdown timer finish
            }
        };

        countDownTimer.start();
    }
}
