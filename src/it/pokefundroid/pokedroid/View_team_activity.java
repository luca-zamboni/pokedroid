package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class View_team_activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team_activity);
		setTitle("La tua scuadra");
		ArrayList<PersonalPokemon> personals = PersonalPokemon.getAllPersonaPokemon(getApplicationContext());
		Iterator<PersonalPokemon> i = personals.iterator();
		//LinearLayout l = (LinearLayout) findViewById(R.id.layout_scuadra);
		int y = 0;
		//while(i.hasNext() && y < 3){
			TextView t = (TextView) findViewById(R.id.my_name1);
			t.setText(i.next().my_name);
			//l.addView(t);
			y++;
		//}*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_team_activity, menu);
		return true;
	}

}
