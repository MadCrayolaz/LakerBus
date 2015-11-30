package csc380.lakerbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "LakerBus";

    private GoogleApiClient mGoogleApiClient = SearchActivity.mGoogleApiClient;
    private BroadcastReceiver receiver;
    private int gotToDest = 0;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, (float) 14.0));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("WRITE WRITE WRITE");
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMyLocationEnabled(true);
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng ccenter = new LatLng(l.getLatitude(), l.getLongitude());
        mMap.addMarker(new MarkerOptions().position(new LatLng(SearchActivity.COORDX1, SearchActivity.COORDY1)).title(SearchActivity.NAME1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ccenter, (float) 16.0));

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkDistance();
            }
        };
        registerReceiver(receiver, filter);
    }

    public void checkDistance() {
        Location currLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location dest = new Location("");
        dest.setLatitude(SearchActivity.COORDX1);
        dest.setLongitude(SearchActivity.COORDY1);
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng ccenter = new LatLng(l.getLatitude(), l.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ccenter, (float) 16.0));
        if(currLoc.distanceTo(dest) < 35) {
            gotToDest = 1;
            finish();
        }
    }

    protected void onDestroy() {
        if(gotToDest == 1) Toast.makeText(this, "You Have Reached Your Destination", Toast.LENGTH_LONG).show();
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}