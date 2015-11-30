package csc380.lakerbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LandingPage extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing_page);
	}

	public void goToView(View view) {
		Intent intent = new Intent(this, RouteList.class);
		startActivity(intent);
	}

	public void goToMap(View view) {
		Intent intent = new Intent(this, MapsActivity.class);
		startActivity(intent);
	}

    public void goToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
