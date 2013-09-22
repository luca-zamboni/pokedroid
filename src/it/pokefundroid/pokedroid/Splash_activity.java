package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Pokemon;
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

		text = (TextView) findViewById(R.id.text);
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
				Intent newActivity = new Intent(Splash_activity.this,
						Sprite_Activity.class);
				newActivity
						.putExtra(
								"loc",
								new double[] { location.getLatitude(),
										location.getLongitude(),
										location.getAltitude(),
										location.getAccuracy() });
				startActivity(newActivity);
				locationManager.removeUpdates(locationListener);
				setText("acc: " + location.getAccuracy() + " lat:"
						+ location.getLatitude() + " lon:"
						+ location.getLongitude());
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, locationListener);

		Location location = new Location("network");

		location.setLatitude(45.4103907616809d);
		location.setLongitude(10.985591523349285d);
		location.setAccuracy(10.0f);
		Intent newActivity = new Intent(Splash_activity.this,
				Sprite_Activity.class);
		newActivity.putExtra("loc",
				new double[] { location.getLatitude(), location.getLongitude(),
						location.getAltitude(), location.getAccuracy() });
		startActivity(newActivity);
		locationManager.removeUpdates(locationListener);
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
