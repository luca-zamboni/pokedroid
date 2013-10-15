package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon.PokemonSex;
import it.pokefundroid.pokedroid.utils.FindingUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter.IPokemonSelection;
import it.pokefundroid.pokedroid.viewUtils.ParcelableMonster;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.Toast;

import com.beyondar.android.opengl.views.BeyondarGLSurfaceView;
import com.beyondar.android.opengl.views.OnARTouchListener;
import com.beyondar.android.views.CameraView;
import com.beyondar.android.views.CameraView.IPictureCallback;
import com.beyondar.android.world.World;
import com.beyondar.android.world.objects.BeyondarObject;
import com.beyondar.android.world.objects.GeoObject;

public class AugmentedRealityActivity extends FragmentActivity implements
		OnARTouchListener, ILocation, IPictureCallback, IPokemonSelection {

	public static final String RESULTS = "Results";

	private static final int FIGHT_PROXIMITY = 10;

	private BeyondarGLSurfaceView mBeyondarGLSurfaceView;
	private CameraView mCameraView;
	private World mWorld;
	private Location mWorldCenter;
	private LocationUtils mLocationUtils;

	private int CAPTURE_CODE = 1;

	private ParcelableMonster mSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);

		mBeyondarGLSurfaceView = (BeyondarGLSurfaceView) findViewById(R.id.customGLSurface);
		mCameraView = (CameraView) findViewById(R.id.camera);

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
		mLocationUtils = new LocationUtils(this, this, LocationType.GPS);
		// This is needed, sometimes pokemons are behind the camera...
		mCameraView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBeyondarGLSurfaceView.onPause();
		mLocationUtils.close();
	}

	private void createWorld() {
		mWorld = new World(this);

		double[] loc = getIntent().getExtras().getDoubleArray("loc");

		mWorldCenter = new Location("world");
		mWorldCenter.setLatitude(loc[0]);
		mWorldCenter.setLongitude(loc[1]);

		// TODO DEBUG
		Log.i(AugmentedRealityActivity.class.getName(), "accuracy: " + loc[3]);
		mWorldCenter.setAccuracy((float) loc[3]);

		setWorldAltitude(loc[2]);

		mWorld.setLocation(mWorldCenter);
		fillPkmn(mWorld, mWorldCenter.getLatitude(),
				mWorldCenter.getLongitude(), mWorldCenter.getAltitude(),
				mWorldCenter.getAccuracy());
		mWorld.setDefaultBitmap(R.drawable.creature_6);
	}

	private void fillPkmn(World w, double... loc) {

		int many = FindingUtilities.generateHowManyPokemonInRange(loc[3]);

		for (int i = 0; i < many; i++) {

			Location tmp = FindingUtilities.getRandomLocation(loc[0], loc[1],
					loc[3]);
			// tmp.setAltitude(loc[2]);
			int id = FindingUtilities.findInPosition(tmp.getLatitude(),
					tmp.getLongitude());
			if (id != -1) {
				GeoObject go = new GeoObject(i);
				fillObj(go, id, tmp);
				w.addBeyondarObject(go);
			}
		}
	}

	private void setWorldAltitude(double d) {
		// TODO DEBUG
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

	public static String getImagUri(int id) {
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}

	private void showChoosePokemonDialog(
			ArrayList<ParcelableMonster> monstersIDs) {
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_choosepokemon, null, false);
		GridView gv = (GridView) v.findViewById(R.id.dialog_pokemongridview);
		gv.setAdapter(new ImageAdapter(this, this, monstersIDs));

		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				R.string.choose_pokemon_title).setView(v);
		builder.create().show();
	}

	private void doAction(List<BeyondarObject> geoObjects) {
		Iterator<BeyondarObject> iterator = geoObjects.iterator();
		ArrayList<ParcelableMonster> outMonster = new ArrayList<ParcelableMonster>();
		while (iterator.hasNext()) {
			GeoObject geoObject = (GeoObject) iterator.next();
			float[] results = new float[2];
			Location.distanceBetween(mWorldCenter.getLatitude(),
					mWorldCenter.getLongitude(), geoObject.getLatitude(),
					geoObject.getLongitude(), results);
			// TODO DEBUG
			// if (results[0] <= FIGHT_PROXIMITY) {
			// outPokemon.add(geoObject.getName());
			// }
			if (results[0] <= FIGHT_PROXIMITY
					* (mWorldCenter.getAccuracy() / 2)) {
				ParcelableMonster pm = new ParcelableMonster(
						geoObject.getName(), "", geoObject.getLatitude(),
						geoObject.getLongitude(), PokemonSex.FEMALE, 20);
				outMonster.add(pm);
			}
		}
		if (outMonster.size() == 0)
			Toast.makeText(this, getString(R.string.pokemon_too_far),
					Toast.LENGTH_SHORT).show();
		else if (outMonster.size() > 1)
			showChoosePokemonDialog(outMonster);
		else {
			mSelected = outMonster.get(0);
			mCameraView.tackePicture(this);
		}
	}

	@Override
	public void onTouchARView(MotionEvent event,
			BeyondarGLSurfaceView beyondarView) {
		float x = event.getX();
		float y = event.getY();

		ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

		beyondarView.getARObjectOnScreenCoordinates(x, y, geoObjects);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			doAction(geoObjects);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			break;
		}

	}

	@Override
	public void onLocationChaged(Location location) {
		// TODO spawn new pokemon
		mWorldCenter = location;
		mWorld.setLocation(mWorldCenter);
	}

	private void exitWithError(ErrorType type) {
		Intent result = new Intent();
		result.putExtra(RESULTS, type);
		this.setResult(RESULT_OK, result);
		this.finish();
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO chiedere all'utente di attivare il gps ecc.
		exitWithError(ex);

	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO a seconda dello stato riavviare
	}

	@Override
	public void onPictureTaken(Bitmap picture) {
		if (!mSelected.equals("")) {
			Intent i = new Intent(this, CaptureActivity.class);
			i.putExtra(CaptureActivity.PASSED_WILD_MONSTER_KEY, mSelected);
			byte[] b = StaticClass.compressBitmap(StaticClass.fastBlur(picture,
					3));
			i.putExtra(CaptureActivity.PASSED_BACKGROUND_KEY, b);
			startActivityForResult(i, CAPTURE_CODE);
			mSelected = null;
		}
	}

	@Override
	public void onPokemonSelected(ParcelableMonster pm) {
		mSelected = pm;
		mCameraView.tackePicture(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == CAPTURE_CODE) {
				String response = data
						.getStringExtra(CaptureActivity.RESPONSE_KEY);
				if (response != null && response.trim().length() > 0) {
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}