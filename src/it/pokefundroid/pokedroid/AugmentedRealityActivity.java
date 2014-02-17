package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.models.Monster.PokemonSex;
import it.pokefundroid.pokedroid.utils.FindingUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter.IPokemonSelection;
import it.pokefundroid.pokedroidAlpha.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.CameraView;
import com.beyondar.android.view.CameraView.BeyondarPictureCallback;
import com.beyondar.android.view.OnClickBeyondarObjectListener;

import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

public class AugmentedRealityActivity extends FragmentActivity implements
		OnClickBeyondarObjectListener, ILocation, BeyondarPictureCallback,
		IPokemonSelection {

	public static final String RESULTS = "Results";

	private BeyondarFragmentSupport mBeyondarFragment;
	private CameraView mCameraView;
	private World mWorld;
	private Location mWorldCenter;
	private LocationUtils mLocationUtils;

	private int CAPTURE_CODE = 1;

	private Monster mSelected;

	private Dialog mDialog;

	private ArrayList<BeyondarObject> bo;

	private BeyondarObject mSelectedBo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);

		// We create the world and set it in to the view
		mWorld = new World(this);

		mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager()
				.findFragmentById(R.id.beyondarFragment);

		createWorld();

		mBeyondarFragment.setWorld(mWorld);

		mCameraView = mBeyondarFragment.getCameraView();
		// set listener for the geoObjects
		mBeyondarFragment.setOnClickBeyondarObjectListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocationUtils = new LocationUtils(this, this, LocationType.NETWORK);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				fillPkmn(mWorldCenter.getLatitude(),
						mWorldCenter.getLongitude(),
						mWorldCenter.getAltitude(), mWorldCenter.getAccuracy());
				return null;
			}
		}.execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationUtils.close();
	}

	private void createWorld() {

		double[] loc = getIntent().getExtras().getDoubleArray("loc");

		mWorldCenter = new Location("world");
		mWorldCenter.setLatitude(loc[0]);
		mWorldCenter.setLongitude(loc[1]);

		mWorldCenter.setAccuracy((float) loc[3]);

		setWorldAltitude(loc[2]);

		mWorld.setLocation(mWorldCenter);

		mWorld.setDefaultBitmap(R.drawable.creature_6);
	}

	private void fillPkmn(double... loc) {

		int many = FindingUtilities.generateHowManyPokemonInRange(loc[3]);

		// tmp.setAltitude(loc[2]);
		Monster[] id = FindingUtilities.findInPosition(loc[0], loc[1], loc[3],
				many);

		for (int i = 0; i < many; i++) {
			if (id[i] != null) {
				Location tmp = FindingUtilities.getRandomLocation(loc[0],
						loc[1], loc[3]);
				GeoObject go = new GeoObject(i);
				fillObj(go, id[i].getId(), tmp);
				mWorld.addBeyondarObject(go);
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
		go1.setLocation(loc);
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

	private void showChoosePokemonDialog(ArrayList<Monster> monstersIDs) {
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_choosepokemon, null, false);
		GridView gv = (GridView) v.findViewById(R.id.dialog_pokemongridview);
		gv.setAdapter(new ImageAdapter(this, this, monstersIDs));

		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				R.string.choose_pokemon_title).setView(v);
		mDialog = builder.create();
		mDialog.show();
	}

	private void doAction(List<BeyondarObject> geoObjects) {
		Iterator<BeyondarObject> iterator = geoObjects.iterator();
		ArrayList<Monster> outMonster = new ArrayList<Monster>();
		bo = new ArrayList<BeyondarObject>();
		while (iterator.hasNext()) {
			GeoObject geoObject = (GeoObject) iterator.next();
			float[] results = new float[2];
			Location.distanceBetween(mWorldCenter.getLatitude(),
					mWorldCenter.getLongitude(), geoObject.getLatitude(),
					geoObject.getLongitude(), results);

			int level = (int) ((Math.random() * 95) + 5);
			PokemonSex sex = Monster
					.intToGender((int) ((Math.random() * 2) + 1));
			Monster pm = new Monster(Integer.parseInt(geoObject.getName()), "",
					sex, geoObject.getLatitude(), geoObject.getLongitude(),
					level);

			if (StaticClass.DEBUG) {
				if (results[0] <= FindingUtilities.FIGHT_PROXIMITY
						* (mWorldCenter.getAccuracy() / 2)) {

					outMonster.add(pm);
					bo.add(geoObject);
				}
			} else {
				if (results[0] <= FindingUtilities.FIGHT_PROXIMITY) {
					outMonster.add(pm);
					bo.add(geoObject);
				}
			}
		}
		if (outMonster.size() == 0)
			Toast.makeText(this, getString(R.string.pokemon_too_far),
					Toast.LENGTH_SHORT).show();
		else if (outMonster.size() > 1)
			showChoosePokemonDialog(outMonster);
		else {
			mSelected = outMonster.get(0);
			mSelectedBo = bo.get(0);
			try {
				mCameraView.takePicture(this);
			} catch (Exception e) {
				Log.e(AugmentedRealityActivity.class.getName(), e.toString());
			}
		}
	}

	@Override
	public void onClickBeyondarObject(ArrayList<BeyondarObject> geoObjects) {
		doAction(geoObjects);
	}

	@Override
	public void onLocationChaged(Location location) {
		// TODO spawn new pokemon
		float[] results = new float[2];
		Location.distanceBetween(mWorldCenter.getLatitude(),
				mWorldCenter.getLongitude(), location.getLatitude(),
				location.getLongitude(), results);
		if (results[0] > mWorldCenter.getAccuracy() / 2)
			fillPkmn(location.getLatitude(), location.getLongitude(),
					location.getAltitude(), location.getAccuracy());
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
	public void onPokemonSelected(Monster pm, int i) {
		if (mDialog != null)
			mDialog.dismiss();
		if (bo != null) {
			mSelectedBo = bo.get(i);
			bo = null;
		}
		mSelected = pm;
		mCameraView.takePicture(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == CAPTURE_CODE) {
				String response = data
						.getStringExtra(CaptureActivity.RESPONSE_KEY);
				if (response != null && response.trim().length() > 0) {
					if (mSelectedBo != null)
						mWorld.remove(mSelectedBo);
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
