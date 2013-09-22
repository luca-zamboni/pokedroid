package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.FindingUtilities;

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
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class Sprite_Activity extends Activity implements OnARTouchListener {

	private BeyondarGLSurfaceView mBeyondarGLSurfaceView;
	private CameraView mCameraView;
	private World mWorld;
	private Location mWorldCenter; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);

		mBeyondarGLSurfaceView = (BeyondarGLSurfaceView) findViewById(R.id.customGLSurface);
		mCameraView = (CameraView) findViewById(R.id.camera);

		// We create the world and set it in to the view
		mWorld = createWorld();
		mBeyondarGLSurfaceView.setWorld(mWorld);
		// set listener for the geoObjects
		mBeyondarGLSurfaceView.setOnARTouchListener(this);
		
	}

	private World createWorld() {
		World w = new World(this);

		double[] loc = getIntent().getExtras().getDoubleArray("loc");
		mWorldCenter = new Location("gps");
		mWorldCenter.setLatitude(loc[0]);
		mWorldCenter.setLongitude(loc[1]);
		mWorldCenter.setAltitude(loc[2]);
		mWorldCenter.setAccuracy((float) loc[3]);
		w.setLatitude(loc[0]);
		w.setLongitude(loc[1]);
		w.setAltitude(loc[2]); 
		
		fillPkmn(w, mWorldCenter.getLatitude(),mWorldCenter.getLongitude(),mWorldCenter.getAltitude(),mWorldCenter.getAccuracy());
		
		Log.i("pkmn", "you are in lat: " + loc[0] + " lon:" + loc[1]);

		w.setDefaultBitmap(R.drawable.creature_6);

		return w;
	}

	private void fillPkmn(World w, double... loc) {
		
		//TODO do it in proportion!
		int many = (int) (Math.random() * 10*loc[3]);
		
		for (int i = 0; i < many; i++) {
			Location tmp = FindingUtilities.getLocation(loc[0], loc[1], loc[3]);
			tmp.setAltitude(loc[2]);
//			DEBUG
//			int id = FindingUtilities.findInPosition(tmp.getLatitude(),
//					tmp.getLongitude());
			int id = (int )(Math.random()*151);
			if (id != -1) {
				GeoObject go =new GeoObject(i);
				fillObj(go,id,tmp);
				w.addBeyondarObject(go);
				Log.i("pkmn", "pokemon id: " + id + " lat:" + tmp.getLatitude()
						+ " lon:" + tmp.getLongitude());
			}
			
		}
	}
	
	private void fillObj(GeoObject go1,int id,Location loc) {
		go1.setLatitude(loc.getLatitude());
		go1.setLongitude(loc.getLongitude());
		go1.setAltitude(loc.getAltitude());
		go1.setImageUri(getImagUri(id));
		go1.setName(id+"");
	}

	private String getImagUri(int id){
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBeyondarGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBeyondarGLSurfaceView.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sprite_, menu);
		return true;
	}

	@Override
	public void onTouchARView(MotionEvent event,
			BeyondarGLSurfaceView beyondarView) {

		float x = event.getX();
		float y = event.getY();

		ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

		beyondarView.getARObjectOnScreenCoordinates(x, y, geoObjects);

		String textEvent = "";

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			textEvent = "Event type ACTION_DOWN: ";
			break;
		case MotionEvent.ACTION_UP:
			textEvent = "Event type ACTION_UP: ";
			break;
		case MotionEvent.ACTION_MOVE:
			textEvent = "Event type ACTION_MOVE: ";
			break;
		default:
			break;
		}

		Iterator<BeyondarObject> iterator = geoObjects.iterator();
		while (iterator.hasNext()) {
			BeyondarObject geoObject = iterator.next();
			textEvent = textEvent + " " + geoObject.getName();
		}
		Toast.makeText(Sprite_Activity.this, textEvent, Toast.LENGTH_SHORT)
				.show();
	}

}
