
package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.FindingUtilities;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
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
import android.widget.GridView;
import android.widget.Toast;

import com.beyondar.android.opengl.views.BeyondarGLSurfaceView;
import com.beyondar.android.opengl.views.OnARTouchListener;
import com.beyondar.android.views.CameraView;
import com.beyondar.android.views.CameraView.IPictureCallback;
import com.beyondar.android.world.World;
import com.beyondar.android.world.objects.BeyondarObject;
import com.beyondar.android.world.objects.GeoObject;

public class AugmentedReality_Activity extends FragmentActivity implements
		OnARTouchListener, ILocation, IPictureCallback, IPokemonSelection {

	public static final String RESULTS = "Results";

	private static final int FIGHT_PROXIMITY = 10;

	private BeyondarGLSurfaceView mBeyondarGLSurfaceView;
	private CameraView mCameraView;
	private World mWorld;
	private Location mWorldCenter;
	private LocationUtils mLocationUtils;
	private LocationType mLocationType;

	private int CAPTURE_CODE = 1;

	private ParcelableMonster mSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);

		mBeyondarGLSurfaceView = (BeyondarGLSurfaceView) findViewById(R.id.customGLSurface);
		mCameraView = (CameraView) findViewById(R.id.camera);
		// TODO DEBUG
		mLocationType = LocationType.NETWORK;
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
		mLocationUtils = new LocationUtils(this, this, mLocationType);
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
		
		//TODO DEBUG
		Log.i(AugmentedReality_Activity.class.getName(), "accuracy: "+ loc[3]);
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

			Location tmp = FindingUtilities.getLocation(loc[0], loc[1], loc[3]);
			//tmp.setAltitude(loc[2]);
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

	public static String getImagUri(int id) {
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}
	
	private void showChoosePokemonDialog(ArrayList<ParcelableMonster> monstersIDs) {
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
		ArrayList<ParcelableMonster> outMonster= new ArrayList<ParcelableMonster>();
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
				ParcelableMonster pm = new ParcelableMonster();
				pm.setId( geoObject.getName() );
				pm.setLocation(new double[]{geoObject.getLatitude(),geoObject.getLongitude()});
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
	

	public static byte[] compressBitmap(Bitmap b) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 50, bs);
		return bs.toByteArray();
	}

	public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
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
			byte[] b =compressBitmap(fastblur(picture, 3));
			i.putExtra(CaptureActivity.PASSED_BACKGROUND_KEY,b);
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
		super.onActivityResult(requestCode, resultCode,data);
		if(resultCode == RESULT_OK){
			if(requestCode == CAPTURE_CODE){
				String response = data.getStringExtra(CaptureActivity.RESPONSE_KEY);
				if(response!=null && response.trim().length()>0){
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	

	
}
