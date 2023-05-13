package app.apiGoogleMaps.mapas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

public class CargarMapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_mapa);

        TmpMapFrag mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Establecer estilo personalizado del mapa si lo deseas
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        LatLngBounds parisBounds = new LatLngBounds(
                new LatLng(48.815573, 2.224199), // Suroeste de París
                new LatLng(48.902145, 2.469920) // Noreste de París
        );
        mMap.setLatLngBoundsForCameraTarget(parisBounds);

        // Establecer París como el centro del mapa con un nivel de zoom
        LatLng paris = new LatLng(48.8566, 2.3522);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 12));
    }
}
