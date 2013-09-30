package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.PersonalPokemon;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalPokemonAdapter extends ArrayAdapter<PersonalPokemon> {
  private Context context;
  private PersonalPokemon per;

  public PersonalPokemonAdapter(Context context,ArrayList<PersonalPokemon> p) {
    super(context, R.layout.one_pokemon_activity,p);
    this.context = context;
    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.one_pokemon_activity, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.pokemon_picture);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.pokemon_name);
    textView.setText("Merda");

	return rowView;
  }
} 
