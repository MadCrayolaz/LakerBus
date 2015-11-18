package csc380.lakerbus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FullSchedule2 extends AppCompatActivity {
    String[] timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_schedule);

        final Context c = this;
        Gson gson = new Gson();
        String trimmed = StopTimeViewer2.JSONTIMES.trim();
        ArrayList<String> al = gson.fromJson(trimmed, ArrayList.class);

        timeList = al.toArray(new String[al.size()]);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, android.R.id.text1, timeList);
        ListView lv2 = (ListView) findViewById(R.id.lview2);
        lv2.setAdapter(adapter2);

        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText("Stop Times For " + SearchActivity.NAME1);
    }
}