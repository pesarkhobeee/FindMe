package info.pathseeker.findme;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BearingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bearing);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private Location getLocation(double lat, double lon) {
		Location location = new Location("provider");

		location.setLatitude(lat);
		location.setLongitude(lon);
		return location;
	}

	private double getValue(int id) {
		EditText et = (EditText) findViewById(id);
		return Double.parseDouble(et.getText().toString());
	}

	public void show(View view) {
		Location here = getLocation(getValue(R.id.et_lat_here), getValue(R.id.et_long_here));
		Location dest = getLocation(getValue(R.id.et_lat_dest), getValue(R.id.et_long_dest));
		
		String text = "Distance: " + here.distanceTo(dest) + "\n";
		text += "Bearing: " + here.bearingTo(dest);
		
		TextView tv = (TextView) findViewById(R.id.tv_bearing);
		tv.setText(text);
	}
}
