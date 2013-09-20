package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.FindingUtilities;
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
        final int[] pokemon = new int[151];
        
        for(int i = 0; i < 151; i++) {
        	pokemon[i]=i;
        }
       
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
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				
				int condition = FindingUtilities.findInPosition(latitude, longitude);
				
				String str = location.getLatitude() + " " + location.getLongitude() + " " 
						+ condition+" "+Pokemon.getRarityFromId(Math.abs(condition))+ "\n";
				for (int i = 0; i<FindingUtilities.currentPkmnSet.length; i++)
					str = str + " " + FindingUtilities.currentPkmnSet[i];
				
				setText(str);
				if(condition >= 0){
					ratata++;
					Toast.makeText(Splash_activity.this, "Hai trovato "+ratata+" pokemon, id: " 
					+condition, 1000).show();
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
