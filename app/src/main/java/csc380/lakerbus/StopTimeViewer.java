package csc380.lakerbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class StopTimeViewer extends AppCompatActivity {
    ListView lv;
    String[] timeList;
    String modify;
    public final static String EXTRA_COORDS = "csc380.lakerbus.COORDS";
    public final static String EXTRA_NAMES = "csc380.lakerbus.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_time_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String toParse = intent.getStringExtra(RouteList.EXTRA_MESSAGE);
        Gson gson = new Gson();
        ArrayList<String> al = gson.fromJson(toParse, ArrayList.class);
        DateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        DateFormat displayFormat = new SimpleDateFormat("HH.mm");
        ArrayList<Double> times = new ArrayList<Double>();
        for(String bbb:al) {
            try {
                Date date = inputFormat.parse(bbb);
                times.add(Double.parseDouble(displayFormat.format(date)));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Double timeNow = new Double(displayFormat.format(Calendar.getInstance().getTime()));
        times.add(timeNow);
        Collections.sort(times);
        al.clear();
        ArrayList<Double> timeHolder = new ArrayList<Double>();
        for(Double k:times) timeHolder.add(k);
        for(Double k:times) timeHolder.add(k);
        Date date;
        try {
            date = displayFormat.parse(timeHolder.get(timeHolder.indexOf(timeNow) + 1).toString());
            al.add(date.toString());
            date = displayFormat.parse(timeHolder.get(timeHolder.indexOf(timeNow) + 2).toString());
            al.add(date.toString());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(al);
        al.set(0, "Next Time For Bus: " + al.get(0));
        al.set(1, "Can't Make This?\nNext Time: " + al.get(1));
        al.add("Click Here To Visualize The Stop");
        timeList = al.toArray(new String[al.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, timeList);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg2 == 2) {
                    Intent intent = new Intent(StopTimeViewer.this, MarkerActivity.class);
                    intent.putExtra(EXTRA_COORDS, intent.getDoubleArrayExtra(RouteList.EXTRA_COORDS));
                    intent.putExtra(EXTRA_NAMES, intent.getStringExtra(RouteList.EXTRA_NAMES));
                    startActivity(intent);
                }
            }
        });
    }
}