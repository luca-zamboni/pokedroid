package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.BaseAdapter;
import it.pokefundroid.pokedroid.utils.BaseHelper;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class Menu_Activity extends Activity {
	
	private Button view_pokemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		view_pokemon = (Button) findViewById(R.id.button_pokemon);
		
		
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
