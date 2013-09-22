package it.pokefundroid.pokedroid;
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
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class Sprite_Activity extends Activity implements OnARTouchListener {

	private BeyondarGLSurfaceView mBeyondarGLSurfaceView;
	private CameraView mCameraView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sprite);
		
		mBeyondarGLSurfaceView = (BeyondarGLSurfaceView) findViewById(R.id.customGLSurface);
		mCameraView = (CameraView) findViewById(R.id.camera);

		// We create the world and set it in to the view
		World world = createWorld();
		mBeyondarGLSurfaceView.setWorld(world);
		// set listener for the geoObjects
		mBeyondarGLSurfaceView.setOnARTouchListener(this);
	}

	private World createWorld() {
		World w = new World(this);
		
//		double[] loc = getIntent().getExtras().getDoubleArray("loc");
//		w.setLatitude(loc[0]);
//		w.setLongitude(loc[1]);
//		w.setAltitude(loc[2]);
//		
//		go1.setLatitude(loc[0]);
//		go1.setLongitude(loc[1]);
//		go1.setAltitude(loc[2]);
		w.setLongitude(1.925848038959814d);
		w.setLatitude(41.26533734214473d);
		
		GeoObject go1 = new GeoObject(4l);
		go1.setLongitude(1.925662767707665d);
		go1.setLatitude(41.26518862002349d);
		go1.setImageUri("assets://charmender.png");
		go1.setName("Image from assets");
		
		w.addBeyondarObject(go1);
		w.setDefaultBitmap(R.drawable.creature_6);

		return w;
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
	}

}
