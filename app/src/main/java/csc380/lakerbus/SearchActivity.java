package csc380.lakerbus;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    LakerBusDB lbdb;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        c = this;
    }

    public void doSearch(View view) {
        TextView tv = (TextView) findViewById(R.id.editText);
        CharSequence searchVal = tv.getText();
        lbdb = new LakerBusDB(this);
        String selectQuery = "SELECT * FROM route";
        Cursor cursor = lbdb.getReadableDatabase().rawQuery(selectQuery, null);
        ArrayList<String> al = new ArrayList<String>();
        while(cursor.moveToNext()) {
            String check = cursor.getString(cursor.getColumnIndex("stop"));
            if(check.contains(searchVal)) al.add("Route: " + cursor.getString(cursor.getColumnIndex("_id")) + "\nStop: " + check);
        }
        cursor.close();
        String[] searchList = al.toArray(new String[al.size()]);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, android.R.id.text1, searchList);
        ListView lv2 = (ListView) findViewById(R.id.listView2);
        lv2.setAdapter(adapter2);
    }
}
