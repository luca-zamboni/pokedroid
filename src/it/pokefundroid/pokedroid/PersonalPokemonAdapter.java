package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalPokemonAdapter extends ArrayAdapter<PersonalPokemon> {
	private Context context;
	private ArrayList<PersonalPokemon> pPokemon;

	public PersonalPokemonAdapter(Context context,
			ArrayList<PersonalPokemon> pPokemon) {
		super(context, R.layout.one_pokemon_activity, pPokemon);
		this.context = context;
		this.pPokemon = pPokemon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.one_pokemon_activity, parent,
				false);
		TextView textView = (TextView) rowView.findViewById(R.id.pokemon_name);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.pokemon_picture);
		textView.setText(pPokemon.get(position).getName());
		int id = pPokemon.get(position).getId();
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeStream(context.getAssets().open(
					"pkm/pkfrlg" + (id < 10 ? "00" : id < 100 ? "0" : "") + id
							+ ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("PersonalPokemonAdapter", "CANNATO!! CANNATO!!!");
		}
		imageView.setImageBitmap(bm);

		return rowView;
	}
}
