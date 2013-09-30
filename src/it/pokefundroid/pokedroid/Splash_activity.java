package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.StaticClass;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_activity extends Activity{

	public TextView text;
	int ratata = 0;
	private LocationUtils mLocationUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		// Connessionae database all' inizio del progrmma nn cancellare
		StaticClass.openBatabaseConection(getApplicationContext());

		text = (TextView) findViewById(R.id.text);
		
<<<<<<< HEAD
		 mLocationUtils = new LocationUtils(this, this, LocationType.NETWORK);
		// DEBUG PURPOSE

//		SharedPreferencesUtilities.setUserHeight(this, 1.75f);
//		Location location = new Location("network");
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
//
		startActivity(new Intent(Splash_activity.this,Menu_Activity.class));
//		Intent newActivity = new Intent(Splash_activity.this,
//				Sprite_Activity.class);
//		 newActivity.putExtra("loc", new
//		 double[]{location.getLatitude(),location.getLongitude(),location.getAltitude()});
//		startActivity(newActivity);
		//Splash_activity.this.finish();
=======
		startActivity(new Intent(Splash_activity.this,Menu_Activity.class));
		this.finish();
>>>>>>> degio
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
