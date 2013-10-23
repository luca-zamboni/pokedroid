package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import it.pokefundroid.pokedroid.utils.StaticClass;

import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Menu_Activity extends Activity implements ILocation {

	private final int AUGMENTED_REALITY_CODE = 1;
	private final int MAX_WAIT = 20;

	private Button mViewPokemon;
	private Button mExplore;
	private LocationUtils mLocationUtils;
	private ProgressDialog mProgressDialog;
	private boolean setHome = false;
	private boolean isHomeDialogShowing = false;
	private LocationType mLocationType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		mLocationType = LocationType.GPS;

		if (StaticClass.sTeam == null || StaticClass.dbpoke == null) {
			startActivity(new Intent(this, Splash_activity.class));
			this.finish();
		}

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
				if (setHome) {
					mLocationType = LocationType.NETWORK;
				}
				mLocationUtils = new LocationUtils(Menu_Activity.this,
						Menu_Activity.this, mLocationType);
				Location lastKonwn = mLocationUtils.getLastKnownLocation();
				if (lastKonwn != null && !setHome) {
					mLocationUtils.close();
					onLocationChaged(lastKonwn);
				} else {
					createProgressDialog();
					mLocationUtils.setTimer(MAX_WAIT);
				}
			}
		});

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (SharedPreferencesUtilities.isFirstStart(this)) {
			Intent i = new Intent(this, ChooseSide.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(new Intent(i));
			finish();
		}
		Location l = SharedPreferencesUtilities.getHomeLocation(this);
		if (l == null && !setHome && !isHomeDialogShowing) {
			displaySetHome();
			isHomeDialogShowing = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.reset_home) {
			if (SharedPreferencesUtilities.canSetHome(Menu_Activity.this)) {
				setHome = true;
				Toast.makeText(Menu_Activity.this,
						getString(R.string.toast_set_home), Toast.LENGTH_SHORT)
						.show();
				return true;
			} else
				displayErrors(R.string.title_dialog_reset_home_err,
						R.string.title_dialog_reset_home_msg);
		}
		return super.onOptionsItemSelected(item);
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

	private void displaySetHome() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.title_dialog_set_home)
				.setMessage(R.string.title_dialog_choose_set_home)
				.setCancelable(true)
				.setPositiveButton(getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								setHome = true;
								isHomeDialogShowing = false;
								dialog.dismiss();
							}
						}).setNeutralButton(getString(android.R.string.cancel),

				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						isHomeDialogShowing = false;
					}
				});
		builder.create().show();
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
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		if (mLocationUtils != null)
			mLocationUtils.close();
		Intent newActivity = new Intent(Menu_Activity.this,
				AugmentedRealityActivity.class);
		newActivity.putExtra("loc",
				new double[] { location.getLatitude(), location.getLongitude(),
						location.getAltitude(), location.getAccuracy() });
		if (setHome) {
			SharedPreferencesUtilities.setHome(this, location);
			setHome = false;
		}
		startActivityForResult(newActivity, AUGMENTED_REALITY_CODE);
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO aviare un activity di errore. oppure
		// chiedere all'utente di attivare il gpx ecc.
		if (ex == ErrorType.TIME_FINISHED
				|| ex == ErrorType.NOT_ENOUGH_ACCURACY) {

			if (mLocationType == LocationType.GPS) {
				mLocationType = LocationType.NETWORK;
				mLocationUtils.close();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mLocationUtils = new LocationUtils(Menu_Activity.this,
								Menu_Activity.this, mLocationType);
					}
				});

			} else {
				mProgressDialog.dismiss();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						displayErrors(R.string.not_enough_accuracy_title,
								R.string.not_enough_accuracy);
					}
				});

			}
		} else {
			Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO a seconda dello stato riavviare
		// Toast.makeText(this, provider + isActive, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AUGMENTED_REALITY_CODE) {
				Serializable results = data
						.getSerializableExtra(AugmentedRealityActivity.RESULTS);
				if (results.equals(ErrorType.NOT_ENOUGH_ACCURACY)) {
					displayErrors(R.string.not_enough_accuracy_title,
							R.string.not_enough_accuracy);
				} else if (results.equals(ErrorType.TIME_FINISHED)) {
					displayErrors(R.string.time_finished_title,
							R.string.time_finished_msg);
				}
			}
		}
	}

}
