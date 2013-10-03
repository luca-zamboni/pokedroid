package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.R;
import it.pokefundroid.pokedroid.R.id;
import it.pokefundroid.pokedroid.R.layout;
import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.models.Pokemon;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
		super(context, R.layout.one_pokemon, pPokemon);
		this.context = context;
		this.pPokemon = pPokemon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.one_pokemon, parent,
				false);
		
		//// find view
		TextView pokemonName = (TextView) rowView.findViewById(R.id.pokemon_name);
		ImageView pokemon_pictures = (ImageView) rowView
				.findViewById(R.id.pokemon_picture);
		TextView pokemonSex = (TextView) rowView
				.findViewById(R.id.pokemon_sex);
		TextView pokemonLevel = (TextView) rowView
				.findViewById(R.id.pokemon_level);
		

		//// set text of pokemon
		pokemonName.setText(pPokemon.get(position).getName());
		pokemonName.setTextColor(Color.BLACK);
		
		//// set level of pokemon
		pokemonLevel.setText("lv. "+pPokemon.get(position).getLevel());
		pokemonLevel.setTextColor(Color.BLACK); 
		
		//// set image of pokemon
		int id = pPokemon.get(position).getId();
		Bitmap pictures  = Pokemon.getImagBitmap(context, id);
		pokemon_pictures.setImageBitmap(pictures);
		
		/// set sex of pokemon
		String sexChar = PersonalPokemon.getSexAsci(pPokemon.get(position).getSex());
		pokemonSex.setText(sexChar);
		if(sexChar.equals("♀")){
			pokemonSex.setTextColor(Color.MAGENTA);
		}else{
			if(sexChar.equals("♂")){
				pokemonSex.setTextColor(Color.BLUE);
			}
		}
		

		return rowView;
	}
}




