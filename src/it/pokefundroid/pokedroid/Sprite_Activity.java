package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Pokemon;
import it.pokefundroid.pokedroid.utils.FindingUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;

import java.util.ArrayList;
import java.util.Iterator;

import com.beyondar.android.opengl.util.FpsUpdatable;
import com.beyondar.android.opengl.views.BeyondarGLSurfaceView;
import com.beyondar.android.opengl.views.OnARTouchListener;
import com.beyondar.android.views.CameraView;
import com.beyondar.android.world.World;
import com.beyondar.android.world.objects.GeoObject;
import com.beyondar.android.world.objects.BeyondarObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class Sprite_Activity extends Activity implements OnARTouchListener,
		ILocation {

	public enum AugmentedRealityErrors{
		NOT_ENOUGH_ACCURACY
	}
	
	public static final String RESULTS = "Results";
	
	private static final int FIGHT_PROXIMITY = 2;
	
	private BeyondarGLSurfaceView mBeyondarGLSurfaceView;
	private CameraView mCameraView;
	private World mWorld;
	private Location mWorldCenter;
	private LocationUtils mLocationUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);

		mBeyondarGLSurfaceView = (BeyondarGLSurfaceView) findViewById(R.id.customGLSurface);
		mCameraView = (CameraView) findViewById(R.id.camera);
		mLocationUtils = new LocationUtils(this, this, LocationType.NETWORK);
		
		// We create the world and set it in to the view
		createWorld();
		mBeyondarGLSurfaceView.setWorld(mWorld);

		// set listener for the geoObjects
		mBeyondarGLSurfaceView.setOnARTouchListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mBeyondarGLSurfaceView.onResume();
		
		// This is needed, sometimes pokemons are behind the camera...
		mCameraView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBeyondarGLSurfaceView.onPause();
		mLocationUtils.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sprite_, menu);
		return true;
	}

	private void createWorld() {
		mWorld = new World(this);

		double[] loc = getIntent().getExtras().getDoubleArray("loc");

		mWorldCenter = new Location("world");
		mWorldCenter.setLatitude(loc[0]);
		mWorldCenter.setLongitude(loc[1]);
		mWorldCenter.setAccuracy((float) loc[3]);
		setWorldAltitude(loc[2]);

		mWorld.setLocation(mWorldCenter);
		if (mWorldCenter.getAccuracy() < ((mLocationUtils.getLocationType()==LocationType.NETWORK)?40:15))
			fillPkmn(mWorld, mWorldCenter.getLatitude(),
					mWorldCenter.getLongitude(), mWorldCenter.getAltitude(),
					mWorldCenter.getAccuracy());
		else{
			exitWithError(AugmentedRealityErrors.NOT_ENOUGH_ACCURACY);
		}
		mWorld.setDefaultBitmap(R.drawable.creature_6);
	}

	private void fillPkmn(World w, double... loc) {

		int many = FindingUtilities.generateHowManyPokemonInRange(loc[3]);

		for (int i = 0; i < many; i++) { 

			Location tmp = FindingUtilities.getLocation(loc[0], loc[1], loc[3]);
			tmp.setAltitude(loc[2]);
			Pokemon id = FindingUtilities.findInPosition(tmp.getLatitude(),
					tmp.getLongitude());

		}
	}

	private void setWorldAltitude(double d) {
		// DEBUG
		// double teoalt= d-SharedPreferencesUtilities.getUserHeight(this)*2;
		double teoalt = d;
		mWorldCenter.setAltitude((teoalt > 0) ? teoalt : d);
	}

	private void fillObj(GeoObject go1, int id, Location loc) {
		go1.setLatitude(loc.getLatitude());
		go1.setLongitude(loc.getLongitude());
		go1.setAltitude(loc.getAltitude());
		go1.setImageUri(getImagUri(id));
		go1.setName(id + "");
	}

	private String getImagUri(int id) {
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}
	
	private void exitWithError(AugmentedRealityErrors notEnoughAccuracy) {
		Intent result = new Intent();
		result.putExtra(RESULTS, AugmentedRealityErrors.NOT_ENOUGH_ACCURACY);
		this.setResult(RESULT_OK,result);
		this.finish();
	}

	@Override
	public void onTouchARView(MotionEvent event,
			BeyondarGLSurfaceView beyondarView) {
		float x = event.getX();
		float y = event.getY();

		ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

		beyondarView.getARObjectOnScreenCoordinates(x, y, geoObjects);

		String textEvent = "";

		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// break;
		// case MotionEvent.ACTION_UP:
		// textEvent = "Event type ACTION_UP: ";
		// break;
		// case MotionEvent.ACTION_MOVE:
		// break;
		// default:
		// break;
		// }

		Iterator<BeyondarObject> iterator = geoObjects.iterator();
		while (iterator.hasNext()) {
			GeoObject geoObject = (GeoObject) iterator.next();
			textEvent = textEvent + " " + geoObject.getName();
			float[] results = new float[2];
			Location.distanceBetween(mWorldCenter.getLatitude(),
					mWorldCenter.getLongitude(), geoObject.getLatitude(),
					geoObject.getLongitude(), results);
			if (results[0] <= FIGHT_PROXIMITY) {
				textEvent += ": captured";
			}
		}
		Toast.makeText(Sprite_Activity.this, textEvent, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onLocationChaged(Location location) {
		// TODO spawn new pokemon
		mWorldCenter = location;
		// DEBUG
		// setWorldAltitude(location.getAltitude());
		mWorld.setLocation(mWorldCenter);
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO aviare un activity di errore. oppure
		// chiedere all'utente di attivare il gpx ecc.

	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO a seconda dello stato riavviare
	}

}
