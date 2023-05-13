import android.app.Activity;
import android.widget.EditText;

import com.example.vuelin_go_front.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NewPlayerActivity extends Activity {

    private static final String SERVER_IP = "192.168.0.100"; // replace with your server IP address
    private static final int SERVER_PORT = 8080; // replace with your server port number

    private Socket socket;
    private OutputStream outputStream;

    private EditText input;

    public void connectToServer() {
        try {
            // Create socket connection to server
            socket = new Socket(SERVER_IP, SERVER_PORT);
            outputStream = socket.getOutputStream();

            input = (EditText) findViewById(R.id.player_name_input);

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
    }
}


