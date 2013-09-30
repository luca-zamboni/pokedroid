package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon;

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
		
		ArrayList<PersonalPokemon> poke = new ArrayList<PersonalPokemon>();
		
		poke.add(new PersonalPokemon(1, "ww", 1, 123, 123));
		poke.add(new PersonalPokemon(1, "345asd", 1, 123, 123));

		PersonalPokemonAdapter adapter = new PersonalPokemonAdapter(getApplicationContext(),poke);
		poke_list.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_team_activity, menu);
		return true;
	}

}
