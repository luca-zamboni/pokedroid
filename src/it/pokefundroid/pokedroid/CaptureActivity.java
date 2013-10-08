package it.pokefundroid.pokedroid;

import java.io.IOException;
import java.util.List;

import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.models.PersonalPokemon.PokemonSex;
import it.pokefundroid.pokedroid.models.Pokemon;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import it.pokefundroid.pokedroid.viewUtils.ParcelableMonster;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureActivity extends Activity {

	public static final String PASSED_BACKGROUND_KEY = "background";
	public static final String PASSED_WILD_MONSTER_KEY = "wildMonster";
	
	public static final String RESPONSE_KEY= "captureResponse";

	private Bitmap mBitmapBackground;
	private ImageView mBackground;
	private ImageView mWildPokemon;
	private ImageView mMyPokemon;
	private Button mCapture;
	private ParcelableMonster pm;
	private List<PersonalPokemon> mTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fight);

		mBackground = (ImageView) findViewById(R.id.fight_background);
		mWildPokemon = (ImageView) findViewById(R.id.fight_wildPkmn);
		mMyPokemon = (ImageView) findViewById(R.id.fight_myPkmn);
		mCapture = (Button) findViewById(R.id.capture_captureBtn);

		mCapture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int sex = (int) ((Math.random()*2)+1);
				PokemonSex realSex =PersonalPokemon.intToGender(sex);
				//TODO DEBUG
				PersonalPokemon pkmn = new PersonalPokemon(Integer
						.parseInt(pm.getId()), "Lol", realSex, pm.getFoundX(),pm.getFoundY(), 13);
				pkmn.saveOnDatabase();
				
				//TODO no string!
				exit("Captured!");
			}
		});
		mTeam = PersonalPokemon.getAllPersonaPokemon(this);
		Bundle extras = getIntent().getExtras();
		setBackground(extras);
		setWildPokemon(extras);
		setMyPokemon(extras);
	}

	private void setMyPokemon(Bundle extras) {
		// TODO get him from the team classe
		if(mTeam.isEmpty()){
			//TODO DEBUG
	    	PersonalPokemon first = new PersonalPokemon(4, "Charmy", PokemonSex.MALE, 45, 46, 5);
	    	first.saveOnDatabase();
	    	mTeam = PersonalPokemon.getAllPersonaPokemon(this);
		}
		PersonalPokemon pp = mTeam.get((int)(Math.random()*mTeam.size()));
		try {
			mMyPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(getRearPokemonFilename(pp.getId()+""))));
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

	private String getRearPokemonFilename(String string) {
		int id = Integer.parseInt(string);
		return "pkm/bpkmn" + id + ".png";
	}

	private void setWildPokemon(Bundle extras) {
		this.pm = (ParcelableMonster)extras.getParcelable(PASSED_WILD_MONSTER_KEY);
		try {
			mWildPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(ImageAdapter.getPokemonFilename(pm.getId()))));
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

	private void setBackground(Bundle extras) {
		byte[] byteArray = extras.getByteArray(PASSED_BACKGROUND_KEY);
		mBitmapBackground = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);
		mBackground.setImageBitmap(mBitmapBackground);
	}
	
	private void exit(String response){
		Intent i = new Intent(this, CaptureActivity.class);
		i.putExtra(RESPONSE_KEY,response);
		setResult(RESULT_OK, i);
		this.finish();
	}

}
