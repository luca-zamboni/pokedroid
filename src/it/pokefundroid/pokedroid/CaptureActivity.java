package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CaptureActivity extends Activity {

	public static final String PASSED_BACKGROUND_KEY = "background";
	public static final String PASSED_WILD_MONSTER_KEY = "wildMonster";

	public static final String RESPONSE_KEY = "captureResponse";

	private Bitmap mBitmapBackground;
	private ImageView mBackground;
	private ImageView mWildPokemon;
	private ImageView mMyPokemon;
	private Button mCapture;
	private Monster pm;

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
				pm.saveOnDatabase();
				// TODO no string!
				exit("Captured!");
			}
		});
		Bundle extras = getIntent().getExtras();
		setBackground(extras);
		setWildPokemon(extras);
		setMyPokemon(extras);
	}

	private void setMyPokemon(Bundle extras) {
		// TODO get him from the team classe
		Monster m = StaticClass.sTeam.get((int) (Math.random() * StaticClass.sTeam.size()));
		try {
			mMyPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(getRearPokemonFilename(m.getId() + ""))));
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

	private String getRearPokemonFilename(String string) {
		int id = Integer.parseInt(string);
		return "pkm/bpkmn" + id + ".png";
	}

	private void setWildPokemon(Bundle extras) {
		this.pm = (Monster) extras
				.getSerializable(PASSED_WILD_MONSTER_KEY);
		try {
			mWildPokemon.setImageBitmap(BitmapFactory.decodeStream(getAssets()
					.open(ImageAdapter.getMonsterFilename(pm.getId()+""))));
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

	private void exit(String response) {
		Intent i = new Intent(this, CaptureActivity.class);
		i.putExtra(RESPONSE_KEY, response);
		setResult(RESULT_OK, i);
		this.finish();
	}

}
