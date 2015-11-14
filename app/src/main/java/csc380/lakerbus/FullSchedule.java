package csc380.lakerbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FullSchedule extends AppCompatActivity {
    ListView lv;
    String[] timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule);

        Gson gson = new Gson();
        ArrayList<String> al = gson.fromJson(StopTimeViewer.JSONTIMES, ArrayList.class);

        timeList = al.toArray(new String[al.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, timeList);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(adapter);

        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText("Stop Times For " + RouteList.NAME1);
    }
}