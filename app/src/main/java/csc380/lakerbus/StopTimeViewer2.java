package csc380.lakerbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class StopTimeViewer2 extends AppCompatActivity {
    ListView lv;
    String[] timeList;
    static int destOrStart = 0;
    static String JSONTIMES;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_time_viewer);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;

        JSONTIMES = intent.getStringExtra(SearchActivity.EXTRA_MESSAGE);
        Gson gson = new Gson();
        String trimmed = JSONTIMES.trim();
        ArrayList<String> al = gson.fromJson(trimmed, ArrayList.class);
        al = Utilities.sort(al);
        al.add(0, "Selected Stop: " + SearchActivity.NAME1);
        al.add(1, "Nearest Bus Stop: " + SearchActivity.NAME2);
        al.set(2, "Next Time For Bus: " + al.get(2));
        al.set(3, "Can't Make This?\nNext Time: " + al.get(3));
        timeList = al.toArray(new String[al.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, timeList);
        lv = (ListView) findViewById(R.id.lview);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(StopTimeViewer2.this, MarkerActivity2.class);
                if(arg2 == 0) {
                    destOrStart = 0;
                    startActivity(intent);
                }
                else if(arg2 == 1) {
                    destOrStart = 1;
                    startActivity(intent);
                }
            }
        });
    }

    public void showFullSchedule(View view) {
        Intent intent = new Intent(c, FullSchedule2.class);
        startActivity(intent);
    }
}