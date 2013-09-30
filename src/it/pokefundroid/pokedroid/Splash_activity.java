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
		
		startActivity(new Intent(Splash_activity.this,Menu_Activity.class));
		this.finish();
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
