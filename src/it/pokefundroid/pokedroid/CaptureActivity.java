package it.pokefundroid.pokedroid;

import java.io.IOException;

import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class CaptureActivity extends Activity {

	public static final String PASSED_BACKGROUND_KEY = "background";
	public static final String PASSED_WILD_ID_KEY = "wildID";

	private Bitmap mBitmapBackground;
	private ImageView mBackground;
	private ImageView mWildPokemon;
	private ImageView mMyPokemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fight);
		
		mBackground = (ImageView) findViewById(R.id.fight_background);
		mWildPokemon = (ImageView) findViewById(R.id.fight_wildPkmn);
		mMyPokemon = (ImageView) findViewById(R.id.fight_myPkmn);

		Bundle extras = getIntent().getExtras();
		setBackground(extras);
		setWildPokemon(extras);
		setMyPokemon(extras);
	}

	private void setMyPokemon(Bundle extras) {
		//TODO get him from the team class
		int id = 7;
		try {
			mMyPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(getRearPokemonFilename("1"))));
		} catch (IOException e) {
			//TODO nothing for now
		}
	}

	private String getRearPokemonFilename(String string) {
		int id = Integer.parseInt(string);
		return "pkm/bpkmn" + id + ".png";
	}

	private void setWildPokemon(Bundle extras) {
		String id = extras.getString(PASSED_WILD_ID_KEY);
		try {
			mWildPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(ImageAdapter.getPokemonFilename(id))));
		} catch (IOException e) {
			//TODO nothing for now
		}
	}

	private void setBackground(Bundle extras) {
		byte[] byteArray = extras.getByteArray(PASSED_BACKGROUND_KEY);
		mBitmapBackground = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);
		mBackground.setImageBitmap(mBitmapBackground);
	}

}
