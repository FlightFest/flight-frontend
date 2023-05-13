import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import com.example.vuelin_go_front.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class NewPlayerActivity extends Activity {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // replace with your UUID

    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 1;

    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private EditText input;

    public void connectToServer(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
            } else {
                // Permission has already been granted, proceed with Bluetooth connection
                connectToServer(address);
            }
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
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

