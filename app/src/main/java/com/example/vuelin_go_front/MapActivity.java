package com.example.vuelin_go_front;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.vuelin_go_front.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String SERVER_IP = "192.168.43.127";
    private static final int SERVER_PORT = 8080;
    private Socket socket;
    private OutputStream outputStream;

    private TextView countdownTimer;

    private TextView points;

    private TextView distance;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        distance = findViewById(R.id.distance_text);
        double dist = calculateDistance(new LatLng(48.879304, 2.357054), new LatLng(48.85828788733616, 2.2945136136500732));
        distance.setText(String.format("Distance: %.1f km", dist));

        points = findViewById(R.id.points_text);
        points.setText(String.format("Points: 5"));

        countdownTimer = findViewById(R.id.time_text);

        // Set the countdown timer for 5 minutes (300,000 milliseconds)
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Update the countdown timer every second
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdownTimer.setText(String.format("Time: %02d:%02d", minutes, seconds));
                points.setText(String.format("Points: %d", minutes+1));
            }

            public void onFinish() {
                Intent intent = new Intent(MapActivity.this, WinActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    public double calculateDistance(LatLng point1, LatLng point2) {
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(point2.latitude - point1.latitude);
        double dLon = Math.toRadians(point2.longitude - point1.longitude);
        double lat1 = Math.toRadians(point1.latitude);
        double lat2 = Math.toRadians(point2.latitude);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        return distance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Disable zoom and scroll gestures on the map
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);

        // Disable tilt and rotate gestures on the map if needed
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        // Set a click listener on the map
        mMap.setOnMapClickListener(this);

        LatLngBounds parisBounds = new LatLngBounds(
                new LatLng(48.815573, 2.224199), // Suroeste de París
                new LatLng(48.902145, 2.469920) // Noreste de París
        );
        mMap.setLatLngBoundsForCameraTarget(parisBounds);

        // Add a marker in Paris and move the camera
        LatLng paris = new LatLng(48.879304, 2.357054); // Coordinates of Paris main train station
        mMap.addMarker(new MarkerOptions().position(paris).title("Paris"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(paris).zoom(18.0f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //if (isLatLngOnRoad(latLng))
        //{
            new SendLocationTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, latLng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(18)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            double dist = calculateDistance(latLng, new LatLng(48.85828788733616, 2.2945136136500732));
            distance.setText(String.format("Distance: %.1f km", dist));
            if (dist < 0.5f) {
                Intent intent = new Intent(MapActivity.this, WinActivity.class);
                startActivity(intent);
                finish();
            }
        //};
    }

    private class SendLocationTask extends AsyncTask<LatLng, Void, Void> {

        @Override
        protected Void doInBackground(LatLng... latLngs) {
            LatLng latLng = latLngs[0];

            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = socket.getOutputStream();
                byte[] type = ("move,"+latLng.latitude+","+latLng.longitude).getBytes();
                outputStream.write(type);
                outputStream.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private boolean isLatLngOnRoad(LatLng latLng) {
        // Create a Geocoder object
        Geocoder geocoder = new Geocoder(this);
        try {
            // Perform reverse geocoding to obtain the nearest address to the LatLng
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses.size() > 0) {
                // Check if the address has a street name component
                Address address = addresses.get(0);
                if (address.getThoroughfare() != null) {
                    // Check if the LatLng is on a road by comparing the returned address type to known road types
                    boolean onRoad = false;
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        String addressLine = address.getAddressLine(i);
                        if (addressLine.contains("Street") || addressLine.contains("Road") || addressLine.contains("Avenue") || addressLine.contains("Boulevard") || addressLine.contains("Drive")) {
                            onRoad = true;
                            break;
                        }
                    }
                    return onRoad;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

