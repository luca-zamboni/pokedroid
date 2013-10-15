package it.pokefundroid.pokedroid;

import java.io.IOException;
import java.io.InputStream;

import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.models.PersonalPokemon.PokemonSex;
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ChoosePokemonActivity extends Activity {

	private static final int[] BAD_POKEMON_IDS = { 69, 37, 60 };
	private static final int[] GOOD_POKEMON_IDS = { 1, 4, 7 };

	private int mCont;
	private boolean isBadGuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pokemon);
		isBadGuy = SharedPreferencesUtilities.isBadGuy(this);

		loadPokemonImages();
	}

	private void loadPokemonImages() {
		View[] imageviews = {findViewById(R.id.choose_grass),
				findViewById(R.id.choose_fire),
				findViewById(R.id.choose_water)};
		if (isBadGuy) {
			loadAsset(BAD_POKEMON_IDS, imageviews);
		} else {
			loadAsset(GOOD_POKEMON_IDS, imageviews);
		}
	}

	private void loadAsset(int[] pkmnid, View... imgView) {
		try {
			for (int i = 0; i < pkmnid.length; i++) {
				// get input stream
				InputStream ims = getAssets().open(
						ImageAdapter.getPokemonFilename(pkmnid[i] + ""));
				// load image as Drawable
				Drawable d = Drawable.createFromStream(ims, null);
				// set image to ImageView
				((ImageView) imgView[i]).setImageDrawable(d);
			}
		} catch (IOException ex) {
			return;
		}
	}

	public void choosePokemon(View v) {
		PersonalPokemon p = null;
		PokemonSex ps = PersonalPokemon.intToGender((int)((Math.random()*2)+1));
		switch (v.getId()) {
		case R.id.choose_grass:
			if(isBadGuy)
				p = new PersonalPokemon(BAD_POKEMON_IDS[0], "Bellsprout", ps, -1, -1, 5);
			else
				p = new PersonalPokemon(GOOD_POKEMON_IDS[0], "Bulbasaur", ps, -1, -1, 5);
			break;
		case R.id.choose_fire:
			if(isBadGuy)
				p = new PersonalPokemon(BAD_POKEMON_IDS[1], "Vulpix", ps, -1, -1, 5);
			else
			p = new PersonalPokemon(GOOD_POKEMON_IDS[1], "Charmender", ps, -1, -1, 5);
			break;
		case R.id.choose_water:
			if(isBadGuy)
				p = new PersonalPokemon(BAD_POKEMON_IDS[2], "Poliwag", ps, -1, -1, 5);
			else
			p = new PersonalPokemon(GOOD_POKEMON_IDS[2], "Squirtle", ps, -1, -1, 5);
			break;
			//TODO EASTER EGG to choose Eevee if your are team rocket or
			// pickachu otherwise
		case R.id.choose_msg:
			mCont++;
			if (mCont >= 5) {
				String msg="";
				if(isBadGuy){
					p = new PersonalPokemon(133, "Eve", ps, -1, -1, 5);
					msg = "Eevee the best!";
				}
				else{
					p = new PersonalPokemon(25, "Pika", ps, -1, -1, 5);
					msg = "Pika-Pika!";
				}
				p.saveOnDatabase();
				Toast.makeText(getApplicationContext(), msg,
						Toast.LENGTH_SHORT).show();
				saveAndExit();
				break;
			}
		default:
			return;
		}
		if (p != null) {
			showConfirmDialog(p);
		}
	}

	private void saveAndExit(){
		SharedPreferencesUtilities.setFirstStartCompleted(ChoosePokemonActivity.this);
		ChoosePokemonActivity.this.finish();
	}
	
	private void showConfirmDialog(final PersonalPokemon p) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_confirm)
				.setMessage(R.string.dialog_sure)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								p.saveOnDatabase();
								dialog.dismiss();
								saveAndExit();
							}
						})
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								mCont = 0;
							}
						});
		builder.create().show();
	}

}
