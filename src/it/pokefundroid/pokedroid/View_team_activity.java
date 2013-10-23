package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.PersonalMonsterAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView.OnActionClickListener;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

public class View_team_activity extends Activity implements
		OnItemLongClickListener, OnItemClickListener {

	private ActionSlideExpandableListView mMonstersListView;
	protected ActionMode mActionMode;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team_activity);
		setTitle("Your Team");

		mMonstersListView = (ActionSlideExpandableListView) findViewById(R.id.pokemon_list_view);
		context = View_team_activity.this;
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if(StaticClass.sTeam==null){
			startActivity(new Intent(this,Splash_activity.class));
			this.finish();
		}
		PersonalMonsterAdapter adapter = new PersonalMonsterAdapter(this,
				StaticClass.sTeam);
		mMonstersListView.setAdapter(adapter);
		mMonstersListView.setAdapter(
	            new SlideExpandableListAdapter(
	                adapter,
	                R.id.expandable_toggle_button,
	                R.id.expandable
	            )
	        );
		mMonstersListView.setOnItemClickListener(this);
		mMonstersListView.setOnItemLongClickListener(this);
		
		mMonstersListView.setItemActionListener(new OnActionClickListener() {
			@Override
			public void onClick(View itemView, View clickedView, final int position) {
				
				if(clickedView.getId() == R.id.pokemon_free){
					/////addare animazioni fighe stupide
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(R.string.title_confirm)
							.setMessage(R.string.dialog_sure)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											Monster temp = StaticClass.sTeam.get(position);
											temp.removeFromDatabase();
											StaticClass.sTeam = Monster.getTeamMonsters(context);
											Intent intent = getIntent();
											finish();
											startActivity(intent);
										}
									})
							.setNeutralButton(android.R.string.cancel,null);
					builder.create().show();
				}
				
				if(clickedView.getId() == R.id.pokemon_exchange){
					Log.d("position", position + "");
					Monster m = StaticClass.sTeam.get(position);
					Intent i = new Intent(context, ExchangeActivity.class);
					i.putExtra(ExchangeActivity.PASSED_MONSTER_KEY, m);
					startActivity(i);
				}
				
				if(clickedView.getId() == R.id.pokemon_stats){
					Monster m = StaticClass.sTeam.get(position);
					LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v1 = inflate.inflate(R.layout.dialog_stat_viewer, null, false);
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					TextView thp = (TextView) v1.findViewById(R.id.poke_hp);
					TextView tatt = (TextView) v1.findViewById(R.id.poke_attak);
					TextView tdef = (TextView) v1.findViewById(R.id.poke_defense);
					TextView tspatt = (TextView) v1.findViewById(R.id.poke_spattak);
					TextView tspdef = (TextView) v1.findViewById(R.id.poke_spdefense);
					TextView tspeed = (TextView) v1.findViewById(R.id.poke_speed);
					thp.setText("" + m.getHp());
					tatt.setText("" + m.getAttack());
					tdef.setText("" + m.getDefence());
					tspatt.setText("" + m.getSpecialAttack());
					tspdef.setText("" + m.getSpecialDefence());
					tspeed.setText("" + m.getSpeed());
					builder.setTitle(R.string.title_dialog_viewstatpoke);
					builder.setView(v1);
					builder.create().show();
				}
			}
			
		},R.id.pokemon_exchange, R.id.pokemon_free, R.id.pokemon_stats);
		
		
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.team_context_menu, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}



}
