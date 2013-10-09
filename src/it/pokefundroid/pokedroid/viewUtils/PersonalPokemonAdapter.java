package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.R;
import it.pokefundroid.pokedroid.models.Monster;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalPokemonAdapter extends ArrayAdapter<Monster> {
	private Context context;
	private ArrayList<Monster> pPokemon;

	public PersonalPokemonAdapter(Context context,
			ArrayList<Monster> pPokemon) {
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
		
		final Monster mPoke = pPokemon.get(position);
		

		//// set text of pokemon
		pokemonName.setText(mPoke.getName());
		pokemonName.setTextColor(Color.BLACK);
		
		//// set level of pokemon
		pokemonLevel.setText("lv. "+mPoke.getLevel());
		pokemonLevel.setTextColor(Color.BLACK); 
		
		//// set image of pokemon
		int id = mPoke.getId();
		Bitmap pictures  = Monster.getImagBitmap(context, id);
		pokemon_pictures.setImageBitmap(pictures);
		
		/// set sex of pokemon
		String sexChar = Monster.getSexAsci(mPoke.getSex());
		pokemonSex.setText(sexChar);
		if(sexChar.equals("♀")){
			pokemonSex.setTextColor(Color.MAGENTA);
		}else{
			if(sexChar.equals("♂")){
				pokemonSex.setTextColor(Color.BLUE);
			}
		}
		
		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View m = inflate.inflate(R.layout.dialog_stat_viewer, null, false);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				TextView thp = (TextView) m.findViewById(R.id.poke_hp);
				TextView tatt = (TextView) m.findViewById(R.id.poke_attak);
				TextView tdef = (TextView) m.findViewById(R.id.poke_defense);
				TextView tspatt = (TextView) m.findViewById(R.id.poke_spattak);
				TextView tspdef = (TextView) m.findViewById(R.id.poke_spdefense);
				TextView tspeed = (TextView) m.findViewById(R.id.poke_speed);
				
				thp.setText(""+mPoke.getHp());
				tatt.setText(""+mPoke.getAttack());
				tdef.setText(""+mPoke.getDefence());
				tspatt.setText(""+mPoke.getAtkYield());
				tspdef.setText(""+mPoke.getSpDefYield());
				tspeed.setText(""+mPoke.getSpeed());
				
				builder.setTitle(R.string.title_dialog_viewstatpoke);
				builder.setView(m);
				builder.create().show();
			}
		});

		return rowView;
	}
}




