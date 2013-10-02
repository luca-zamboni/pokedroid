package it.pokefundroid.pokedroid.models;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

public class PokeBattle extends Activity {
	
	public static final int SINGLE = 0;
	public static final int DOUBLE = 1;
	private Parcelable porco;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		porco = i.getParcelableExtra("screenshot");
	}
}
