package csc380.lakerbus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    LakerBusDB lbdb;
    Context c;
    CharSequence searchVal;
    MenuItem searchMenuItem = null;
    public final static String EXTRA_MESSAGE = "csc380.lakerbus.MESSAGE";
    static double COORDX1, COORDY1, COORDX2, COORDY2;
    public static String NAME1, NAME2, modify;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        c = this;
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        final SearchView sv = (SearchView) findViewById(R.id.searchView);
        final ListView lv = (ListView) findViewById(R.id.listView2);

        sv.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {}
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchVal = sv.getQuery();
                lbdb = new LakerBusDB(c);
                String selectQuery = "SELECT * FROM route";
                Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
                ArrayList<String> al = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String check = cursor.getString(cursor.getColumnIndex("stop"));
                    if (check.contains(searchVal))
                        al.add("Route: " + cursor.getString(cursor.getColumnIndex("_id")) + "\nStop: " + check);
                }
                cursor.close();
                String[] searchList = al.toArray(new String[al.size()]);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, android.R.id.text1, searchList);
                ListView lv2 = (ListView) findViewById(R.id.listView2);
                lv2.setAdapter(adapter2);
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String route = (String) lv.getItemAtPosition(arg2);
                route = route.substring(7, route.indexOf("Stop"));
                route = route.trim();
                Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LatLng latlong = new LatLng(l.getLatitude(), l.getLongitude());
                String selectQuery = "SELECT * FROM route WHERE _id = '" + route + "'";
                Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
                ArrayList<LatLng> ll = new ArrayList<LatLng>();
                ArrayList<String> names = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    ll.add(new LatLng(cursor.getDouble(cursor.getColumnIndex("xcoord")), cursor.getDouble(cursor.getColumnIndex("ycoord"))));
                    names.add(cursor.getString(cursor.getColumnIndex("stop")));
                }
                LatLng llong = Utilities.findNearest(ll, latlong);
                COORDX2 = llong.latitude;
                COORDY2 = llong.longitude;
                NAME2 = names.get(ll.indexOf(llong));
                cursor.close();

                String stop = (String) lv.getItemAtPosition(arg2);
                stop = stop.substring(stop.indexOf("Stop") + 6);
                selectQuery = "SELECT * FROM route WHERE _id = '" + route + "' AND stop = '" + stop + "'";
                cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
                cursor.moveToNext();
                modify = cursor.getString(cursor.getColumnIndex("timearrive"));
                COORDX1 = cursor.getDouble(cursor.getColumnIndex("xcoord"));
                COORDY1 = cursor.getDouble(cursor.getColumnIndex("ycoord"));
                NAME1 = cursor.getString(cursor.getColumnIndex("stop"));
                Intent intent = new Intent(SearchActivity.this, StopTimeViewer2.class);
                intent.putExtra(EXTRA_MESSAGE, modify);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
