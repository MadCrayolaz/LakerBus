package csc380.lakerbus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RouteList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	LakerBusDB lbdb;
	String[] namesArray;
	Spinner spin;
	String modify;
	ListView lv;
    public final static String EXTRA_MESSAGE = "csc380.lakerbus.MESSAGE";
	static double COORDX1, COORDY1, COORDX2, COORDY2;
	static String NAME1, NAME2;
	private GoogleApiClient mGoogleApiClient;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final Context c = this;
		lbdb = new LakerBusDB(this);
		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		mGoogleApiClient.connect();
		lv = (ListView) findViewById(R.id.listView);

		ArrayList<String> routeNames = new ArrayList<String>();
		String selectQuery = "SELECT * FROM route;";
		Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {
			if (!routeNames.contains(cursor.getString(cursor.getColumnIndex("_id"))))
				routeNames.add(cursor.getString(cursor.getColumnIndex("_id")));
		}
		routeNames.add(0, "SELECT A ROUTE");
		namesArray = routeNames.toArray(new String[routeNames.size()]);

		spin = (Spinner) findViewById(R.id.route_spinner);
		ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, namesArray);
		cursor.close();
		spin.setAdapter(stringArrayAdapter);
        
		spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String NAME1 = (String) spin.getSelectedItem();
				ListView lv = (ListView) findViewById(R.id.listView);
				lv.setVisibility(ListView.VISIBLE);
				String selectQuery = "SELECT * FROM route WHERE _id = '" + NAME1 + "'";
				Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
				namesArray = new String[cursor.getCount()];
				int i = 0;
				while(cursor.moveToNext()){
					namesArray[i] = cursor.getString(cursor.getColumnIndex("stop"));
					i++;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, android.R.id.text1, namesArray);
				cursor.close();
				lv.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String routeSelected = (String) spin.getSelectedItem();
				Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				LatLng latlong = new LatLng(l.getLatitude(), l.getLongitude());
				String selectQuery = "SELECT * FROM route WHERE _id = '" + routeSelected + "'";
				Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
				ArrayList<LatLng> ll = new ArrayList<LatLng>();
				ArrayList<String> names = new ArrayList<String>();
				while(cursor.moveToNext()) {
					ll.add(new LatLng(cursor.getDouble(cursor.getColumnIndex("xcoord")), cursor.getDouble(cursor.getColumnIndex("ycoord"))));
					names.add(cursor.getString(cursor.getColumnIndex("stop")));
				}
				LatLng llong = Utilities.findNearest(ll, latlong);
				COORDX2 = llong.latitude;
				COORDY2 = llong.longitude;
				NAME2 = names.get(ll.indexOf(llong));
				cursor.close();

				selectQuery = "SELECT * FROM route WHERE _id = '" + routeSelected + "' AND stop = '" + lv.getItemAtPosition(arg2) + "'";
				cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
				cursor.moveToNext();
				modify = cursor.getString(cursor.getColumnIndex("timearrive"));
				COORDX1 = cursor.getDouble(cursor.getColumnIndex("xcoord"));
				COORDY1 = cursor.getDouble(cursor.getColumnIndex("ycoord"));
				NAME1 = cursor.getString(cursor.getColumnIndex("stop"));
				Intent intent = new Intent(RouteList.this, StopTimeViewer.class);
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
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}