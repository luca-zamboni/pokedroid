package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import it.pokefundroid.pokedroid.viewUtils.ParcelableMonster;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Exchange_activity extends Activity {
	
	public static final String PASSED_MONSTER_KEY = "passedMonster";

	private ParcelableMonster pm;
	private TextView mMyPokemonName;
	private ImageView mMyPokemonPicture;
	private TextView mMyPokemonSex;
	private TextView mMyPokemonLevel;
	private TextView mOpponentPokemonName;
	private ImageView mOpponentPokemonPicture;
	private TextView mOpponentPokemonSex;
	private TextView mOpponentPokemonLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_activity);
		
		Bundle extras = getIntent().getExtras();
		setupViews();
		setMyPokemon(extras);
	}
	
	private void setupViews() {
		View myRow = findViewById(R.id.exchange_myPokemon);
		
		mMyPokemonName = (TextView) myRow.findViewById(R.id.pokemon_name);
		mMyPokemonPicture = (ImageView) myRow.findViewById(R.id.pokemon_picture);
		mMyPokemonSex = (TextView) myRow.findViewById(R.id.pokemon_sex);
		mMyPokemonLevel = (TextView) myRow.findViewById(R.id.pokemon_level);
		
		View opponentRow = findViewById(R.id.exchange_opponentPokemon);
		
		mOpponentPokemonName = (TextView) opponentRow.findViewById(R.id.pokemon_name);
		mOpponentPokemonPicture = (ImageView) opponentRow.findViewById(R.id.pokemon_picture);
		mOpponentPokemonSex = (TextView) opponentRow.findViewById(R.id.pokemon_sex);
		mOpponentPokemonLevel = (TextView) opponentRow.findViewById(R.id.pokemon_level);
	}

	private void setMyPokemon(Bundle extras) {
		this.pm = (ParcelableMonster)extras.getParcelable(PASSED_MONSTER_KEY);
		try {
			mMyPokemonName.setText(pm.getName());
			mMyPokemonLevel.setText(pm.getLevel());
			mMyPokemonSex.setText(PersonalPokemon.getSexAsci(pm.getSex()));
			mMyPokemonPicture.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(ImageAdapter.getPokemonFilename(pm.getId()))));
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

}
