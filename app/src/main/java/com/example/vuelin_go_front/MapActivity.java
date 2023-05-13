package com.example.vuelin_go_front;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds parisBounds = new LatLngBounds(
                new LatLng(48.815573, 2.224199), // Suroeste de París
                new LatLng(48.902145, 2.469920) // Noreste de París
        );
        mMap.setLatLngBoundsForCameraTarget(parisBounds);

        // Add a marker in Paris and move the camera
        LatLng paris = new LatLng(48.8809, 2.3556); // Coordinates of Paris main train station
        mMap.addMarker(new MarkerOptions().position(paris).title("Paris"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(paris).zoom(12.0f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}

