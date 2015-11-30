package csc380.lakerbus;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        LatLng ccenter = null;
        if(StopTimeViewer.destOrStart == 0) {
            ccenter = new LatLng(RouteList.COORDX1, RouteList.COORDY1);
        }
        else {
            ccenter = new LatLng(RouteList.COORDX2, RouteList.COORDY2);
        }
        mMap.addMarker(new MarkerOptions().position(ccenter).title(RouteList.NAME1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ccenter, (float) 16.0));
    }
}