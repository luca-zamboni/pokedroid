package it.pokefundroid.pokedroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Exchange_activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exchange_activity, menu);
		return true;
	}

}
