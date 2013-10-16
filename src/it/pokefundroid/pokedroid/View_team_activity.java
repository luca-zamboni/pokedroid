package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.viewUtils.PersonalMonsterAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class View_team_activity extends Activity {
	
	private ListView poke_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team_activity);
		setTitle("Your Team");
		
		poke_list = (ListView) findViewById(R.id.pokemon_list_view);
		ArrayList<Monster> poke = Monster.getAllPersonaPokemon(this);
		PersonalMonsterAdapter adapter = new PersonalMonsterAdapter(View_team_activity.this,poke);
		poke_list.setAdapter(adapter);
		
	}
	

}
