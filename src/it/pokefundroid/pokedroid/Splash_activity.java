package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Pokemon;
import it.pokefundroid.pokedroid.utils.FindingUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_activity extends Activity implements ILocation {

	public TextView text;
	int ratata = 0;
	private LocationUtils mLocationUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		text = (TextView) findViewById(R.id.text);

		
		 mLocationUtils = new LocationUtils(this, this);
		// DEBUG PURPOSE
		SharedPreferencesUtilities.setUserHeight(this, 1.75f);
		Location location = new Location("network");
//		// MY house
//		// location.setLatitude(45.4103907616809d);
//		// location.setLongitude(10.985591523349285d);
//		// julia's place
//		location.setLatitude(45.33497882075608d);
//		location.setLongitude(11.242532143369317d);
//		 //trento
//		 location.setLatitude(46.04688826482743);
//		 location.setLongitude(11.134816808626056);
//		location.setAccuracy(10.0f);
//		Intent newActivity = new Intent(Splash_activity.this,
//				Sprite_Activity.class);
//		newActivity.putExtra("loc",
//				new double[] { location.getLatitude(), location.getLongitude(),
//						location.getAltitude(), location.getAccuracy() });
//		startActivity(newActivity);
//		// Splash_activity.this.finish();

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

	@Override
	public void onLocationChaged(Location location) {
		Intent newActivity = new Intent(Splash_activity.this,
				Sprite_Activity.class);
		newActivity.putExtra("loc",
				new double[] { location.getLatitude(), location.getLongitude(),
						location.getAltitude(), location.getAccuracy() });
		startActivity(newActivity);
		mLocationUtils.close();
		setText("acc: " + location.getAccuracy() + " lat:"
				+ location.getLatitude() + " lon:" + location.getLongitude());
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO aviare un activity di errore. oppure
		// chiedere all'utente di attivare il gpx ecc.
		Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO a seconda dello stato riavviare
		Toast.makeText(this, provider+isActive, Toast.LENGTH_SHORT).show();
	}

}
