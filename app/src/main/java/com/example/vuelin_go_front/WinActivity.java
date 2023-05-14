package com.example.vuelin_go_front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vuelin_go_front.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions mMarkerOptions;
    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
        supportMapFragment.getMapAsync(this);

        // Create the marker for the Eiffel Tower
        mMarkerOptions = new MarkerOptions().position(new LatLng(48.8584, 2.2945)).title("Eiffel Tower");

        // Set the text for the score
        TextView pointsNumberTextView = findViewById(R.id.points_number);
        pointsNumberTextView.setText("100");

        // Set the click listener for the scoreboard button
        Button scoreboardButton = findViewById(R.id.scoreboard_button);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement scoreboard button click logic
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Add the marker to the map
        mMap.addMarker(mMarkerOptions);

        // Move the camera to the Eiffel Tower position
        mLatLng = mMarkerOptions.getPosition();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(15.0f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
