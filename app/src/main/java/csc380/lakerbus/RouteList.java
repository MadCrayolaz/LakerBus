package csc380.lakerbus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;

public class RouteList extends AppCompatActivity {
	LakerBusDB lbdb;
	String[] namesArray;
	Spinner spin;
	String modify;
	ListView lv;
    public final static String EXTRA_MESSAGE = "csc380.lakerbus.MESSAGE";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		lbdb = new LakerBusDB(this);
		lv = (ListView) findViewById(R.id.listView);

		ArrayList<String> routeNames = new ArrayList<String>();
		String selectQuery = "SELECT * FROM route;";
		Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {
			if (!routeNames.contains(cursor.getString(cursor.getColumnIndex("_id"))))
				routeNames.add(cursor.getString(cursor.getColumnIndex("_id")));
		}
		namesArray = routeNames.toArray(new String[routeNames.size()]);

		spin = (Spinner) findViewById(R.id.route_spinner);
		ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, namesArray);
		cursor.close();
		spin.setAdapter(stringArrayAdapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String routeChosen = (String) spin.getSelectedItem();
				String selectQuery = "SELECT * FROM route WHERE _id = '" + routeChosen + "' AND stop = '" + lv.getItemAtPosition(arg2) + "'";
				Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
				ArrayList<Double> doubles = new ArrayList<Double>();
                cursor.moveToNext();
                modify = cursor.getString(cursor.getColumnIndex("timearrive"));
                Intent intent = new Intent(RouteList.this, StopTimeViewer.class);
                intent.putExtra(EXTRA_MESSAGE, modify);
                startActivity(intent);
			}
		});
	}

	public void selectRoute(View view) {
		String routeChosen = (String) spin.getSelectedItem();
		ListView lv = (ListView) findViewById(R.id.listView);
		lv.setVisibility(ListView.VISIBLE);
		String selectQuery = "SELECT * FROM route WHERE _id = '" + routeChosen + "'";
		Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
		namesArray = new String[cursor.getCount()];
		int i = 0;
		while(cursor.moveToNext()){
			namesArray[i] = cursor.getString(cursor.getColumnIndex("stop"));
			i++;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, namesArray);
		cursor.close();
		lv.setAdapter(adapter);
	}
}