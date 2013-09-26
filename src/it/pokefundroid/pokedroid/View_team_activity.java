package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Pokemon;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class View_team_activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_team_activity);
		setTitle("La tua sjuadra");
		Pokemon e = new Pokemon(54, getApplicationContext());
		Log.e("",e.getName());
		/*ArrayList<PersonalPokemon> personals = PersonalPokemon.getAllPersonaPokemon(getApplicationContext());
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
