package it.pokefundroid.pokedroid;

import java.util.ArrayList;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.LocationUtils.ErrorType;
import it.pokefundroid.pokedroid.utils.LocationUtils.LocationType;
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.utils.LocationUtils.ILocation;
import it.pokefundroid.pokedroid.viewUtils.PersonalMonsterAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

public class View_team_activity extends ActionBarActivity implements
		OnItemLongClickListener, OnItemClickListener, ILocation {

	private enum VIEW_STATUS {
		BOX, TEAM
	}

	private ListView mMonstersListView;
	private VIEW_STATUS mViewing;
	private AsyncTask<Object, Void, ArrayList<Monster>> mLoadTask;
	private ProgressDialog mProgressDialog;
	private LocationUtils mLocationUtils;
	private Location mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team_activity);
		setTitle("Your Team");
		mViewing = VIEW_STATUS.TEAM;
		mMonstersListView = (ListView) findViewById(R.id.pokemon_list_view);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (StaticClass.sTeam == null) {
			startActivity(new Intent(this, Splash_activity.class));
			this.finish();
		}
		setTeamAdapter();
	}

	private void createProgressDialog(int msg) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);

			mProgressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							if (mLocationUtils != null) {
								mLocationUtils.close();
							}
							if (mLoadTask != null) {
								mLoadTask.cancel(true);
							}
							dialog.dismiss();
						}
					});
		}
		mProgressDialog.setMessage(getString(msg));
	}

	private void setTeamAdapter() {
		PersonalMonsterAdapter adapter = new PersonalMonsterAdapter(this,
				StaticClass.sTeam);
		mMonstersListView.setAdapter(adapter);
		mMonstersListView.setAdapter(new SlideExpandableListAdapter(adapter,
				R.id.expandable_toggle_button, R.id.expandable));
		mMonstersListView.setOnItemClickListener(this);
		mMonstersListView.setOnItemLongClickListener(this);
	}

	private void setBoxAdapter() {

		mLoadTask = new AsyncTask<Object, Void, ArrayList<Monster>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				createProgressDialog(R.string.progress_msg_box);
				if (mProgressDialog != null && !mProgressDialog.isShowing())
					mProgressDialog.show();
			}

			@Override
			protected ArrayList<Monster> doInBackground(Object... objects) {

				return Monster.getBoxMonsters(View_team_activity.this);
			}

			@Override
			protected void onPostExecute(ArrayList<Monster> result) {
				super.onPostExecute(result);
				if (!isCancelled()) {
					PersonalMonsterAdapter adapter = new PersonalMonsterAdapter(
							View_team_activity.this, result);
					mMonstersListView.setAdapter(adapter);
					mMonstersListView
							.setAdapter(new SlideExpandableListAdapter(adapter,
									R.id.expandable_toggle_button,
									R.id.expandable));
					mViewing = VIEW_STATUS.BOX;
				}
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

			}

		};
		if (mLocation == null) {
			createProgressDialog(R.string.locating_progress_msg);
			if (!mProgressDialog.isShowing())
				mProgressDialog.show();
			mLocationUtils = new LocationUtils(this, this, LocationType.NETWORK);
		} else if (mLocation != null
				&& SharedPreferencesUtilities.isAtHome(this, mLocation)) {
			mLoadTask.execute();
		}
		// TODO set actions on box pokemons =)
		// mMonstersListView.setOnItemClickListener(this);
		// mMonstersListView.setOnItemLongClickListener(this);
	}

	private void toggleListAdapter() {

		if (mViewing == VIEW_STATUS.BOX) {
			mViewing = VIEW_STATUS.TEAM;
			setTeamAdapter();
		} else {

			setBoxAdapter();
		}
		supportInvalidateOptionsMenu();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mViewing == VIEW_STATUS.BOX) {
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(true);
		} else if (mViewing == VIEW_STATUS.TEAM) {
			menu.getItem(0).setVisible(true);
			menu.getItem(1).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.team_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.team_menu_box
				|| item.getItemId() == R.id.team_menu_team) {
			toggleListAdapter();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View clicked, int position,
			long id) {
		Monster m = StaticClass.sTeam.get(position);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.dialog_stat_viewer, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		TextView thp = (TextView) v.findViewById(R.id.poke_hp);
		TextView tatt = (TextView) v.findViewById(R.id.poke_attak);
		TextView tdef = (TextView) v.findViewById(R.id.poke_defense);
		TextView tspatt = (TextView) v.findViewById(R.id.poke_spattak);
		TextView tspdef = (TextView) v.findViewById(R.id.poke_spdefense);
		TextView tspeed = (TextView) v.findViewById(R.id.poke_speed);

		thp.setText("" + m.getHp());
		tatt.setText("" + m.getAttack());
		tdef.setText("" + m.getDefence());
		tspatt.setText("" + m.getSpecialAttack());
		tspdef.setText("" + m.getSpecialDefence());
		tspeed.setText("" + m.getSpeed());

		builder.setTitle(R.string.title_dialog_viewstatpoke);
		builder.setView(v);
		builder.create().show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View clicked,
			int position, long id) {
		Monster m = StaticClass.sTeam.get(position);
		Intent i = new Intent(this, ExchangeActivity.class);
		i.putExtra(ExchangeActivity.PASSED_MONSTER_KEY, m);
		startActivity(i);
		return true;
	}

	@Override
	protected void onPause() {
		if (mLoadTask != null)
			mLoadTask.cancel(true);
		super.onPause();
	}

	@Override
	public void onLocationChaged(Location l) {
		mLocation = l;
		mLocationUtils.close();
		if (mLoadTask != null
				&& SharedPreferencesUtilities.isAtHome(View_team_activity.this,
						l)) {
			mLoadTask.execute();
		}
		else if(mProgressDialog!=null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		mLocationUtils.close();
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		Toast.makeText(View_team_activity.this, getString(R.string.error),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		Toast.makeText(View_team_activity.this, getString(R.string.gps_off),
				Toast.LENGTH_SHORT).show();
	}

}
