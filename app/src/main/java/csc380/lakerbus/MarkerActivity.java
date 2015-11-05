package csc380.lakerbus;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double[] coordInfo = intent.getDoubleArrayExtra(StopTimeViewer.EXTRA_COORDS);
        String name = intent.getStringExtra(StopTimeViewer.EXTRA_NAMES);
        LatLng ccenter = new LatLng(coordInfo[0],coordInfo[1]);
        mMap.addMarker(new MarkerOptions().position(ccenter).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ccenter, (float) 16.0));
    }
}