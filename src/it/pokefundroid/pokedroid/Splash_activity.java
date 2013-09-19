package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.Utilities;
import android.app.Activity;
import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        
        LocationManager locationManager = (LocationManager) 
        		getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			}
			
			@Override
			public void onLocationChanged(Location location) {
				
				setText(location.getLatitude() + " " + location.getLongitude());
				if(Utilities.findByRarity(0)){
					ratata++;
					Toast.makeText(getApplicationContext(), "Hai trovato "+ratata+" ratata", 2000).show();
				}
			}
		};
        locationManager.requestLocationUpdates(  
        LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        
        //Intent newActivity = new Intent(Splash_activity.this, Sprite_Activity.class);
        //startActivity(newActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_activity, menu);
        return true;
    }
    
    public void setText(String m){ 
		text = (TextView) findViewById(R.id.text);
		text.setText(m);
    }
    
}
