package it.pokefundroid.pokedroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu_Activity extends Activity {
	
	private Button view_pokemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		view_pokemon = (Button) findViewById(R.id.button_pokemon);
		
		view_pokemon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),View_team_activity.class));
			}
		});
		
		
		// righe per  bombo x provare 
		// se va il database
		
		/*String name = null;
		BaseAdapter a = null;
		Pokemon.fillDatabasePokemon(Menu_Activity.this);
		a = new BaseAdapter(Menu_Activity.this);
		a.open();
		Cursor c = a.basePokemonById(new String[] {BaseHelper.NAME}, 1);
		c.moveToFirst();
		name = c.getString(c.getColumnIndex(BaseHelper.NAME));
		Toast.makeText(getApplicationContext(), name ,2000).show();
		//a.close();*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_, menu);
		return true;
	}

}
