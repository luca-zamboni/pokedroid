package it.pokefundroid.pokedroid;


import java.io.Serializable;

import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.Toast;

public class Menu_Activity extends Activity implements ILocation {

	private final int AUGMENTED_REALITY_CODE = 1;
	private final int MAX_WAIT= 15;

	private Button mViewPokemon;
	private Button mExplore;
	private LocationUtils mLocationUtils;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		mViewPokemon = (Button) findViewById(R.id.button_pokemon);

		mViewPokemon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						View_team_activity.class));
			}
		});

		mExplore = (Button) findViewById(R.id.button_explore);

		mExplore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createProgressDialog();
				
//				TODO DEBUG
//				mLocationUtils = new LocationUtils(Menu_Activity.this,
//						Menu_Activity.this, LocationType.NETWORK);
				
				mLocationUtils = new LocationUtils(Menu_Activity.this,
						Menu_Activity.this, LocationType.NETWORK);
				mLocationUtils.setTimer(MAX_WAIT);
				// TODO DEBUG PURPOSE
				// SharedPreferencesUtilities.setUserHeight(this, 1.75f);
				// Location location = new Location("network");
				// // MY house
				// // location.setLatitude(45.4103907616809d);
				// // location.setLongitude(10.985591523349285d);
				// // julia's place
				// location.setLatitude(45.33497882075608d);
				// location.setLongitude(11.242532143369317d);
				// //trento
				// location.setLatitude(46.04688826482743);
				// location.setLongitude(11.134816808626056);
				// location.setAccuracy(10.0f);

			}
		});

		// righe per bombo x provare
		// se va il database

		/*
		 * String name = null; BaseAdapter a = null;
		 * Pokemon.fillDatabasePokemon(Menu_Activity.this); a = new
		 * BaseAdapter(Menu_Activity.this); a.open(); Cursor c =
		 * a.basePokemonById(new String[] {BaseHelper.NAME}, 1);
		 * c.moveToFirst(); name =
		 * c.getString(c.getColumnIndex(BaseHelper.NAME));
		 * Toast.makeText(getApplicationContext(), name ,2000).show();
		 * //a.close();
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_, menu);
		return true;
	}

	private void createProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(R.string.locating_progress_title);
		mProgressDialog.setMessage(getString(R.string.locating_progress_msg));
		mProgressDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						mLocationUtils.close();
					}
				});
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mLocationUtils.close();
					}
				});
		mProgressDialog.show();
	}

	private void displayErrors(int titleRes, int descRes) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(titleRes)
				.setMessage(descRes)
				.setCancelable(true)
				.setNeutralButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		builder.create().show();
	}

	@Override
	public void onLocationChaged(Location location) {
		mProgressDialog.dismiss();
		mLocationUtils.close();
		Intent newActivity = new Intent(Menu_Activity.this,
				Sprite_Activity.class);
		newActivity.putExtra("loc",
				new double[] { location.getLatitude(), location.getLongitude(),
						location.getAltitude(), location.getAccuracy() });
		startActivityForResult(newActivity, AUGMENTED_REALITY_CODE);
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO aviare un activity di errore. oppure
		// chiedere all'utente di attivare il gpx ecc.
		if (ex == ErrorType.TIME_FINISHED || ex == ErrorType.NOT_ENOUGH_ACCURACY) {
			mProgressDialog.dismiss();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					displayErrors(R.string.not_enough_accuracy_title,
							R.string.not_enough_accuracy);
				}
			});
		} else {
			Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO a seconda dello stato riavviare
		//Toast.makeText(this, provider + isActive, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AUGMENTED_REALITY_CODE) {
				Serializable results = data.getSerializableExtra(Sprite_Activity.RESULTS);
				if (results.equals(ErrorType.NOT_ENOUGH_ACCURACY)) {
					displayErrors(R.string.not_enough_accuracy_title,
							R.string.not_enough_accuracy);
				}
				else if( results.equals(ErrorType.TIME_FINISHED)){
					displayErrors(R.string.time_finished_title,
							R.string.time_finished_msg);
				}
			}
		}
	}

}
