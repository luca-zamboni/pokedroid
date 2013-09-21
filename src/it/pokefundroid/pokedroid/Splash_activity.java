package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.FindingUtilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_activity extends Activity {

	public TextView text;
	int ratata = 0;
	private LocationManager locationManager;
	private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(Location location) {
				
				locationManager.removeUpdates(locationListener);
				Splash_activity.this.finish();
			}
		};
		/*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);*/
		Intent newActivity = new Intent(Splash_activity.this,
				Sprite_Activity.class);
		//newActivity.putExtra("loc", new double[]{location.getLatitude(),location.getLongitude(),location.getAltitude()});
		startActivity(newActivity);
		Splash_activity.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_activity, menu);
		return true;
	}

	public void setText(String m) {
		text = (TextView) findViewById(R.id.text);
		text.setText(m);
	}

}
