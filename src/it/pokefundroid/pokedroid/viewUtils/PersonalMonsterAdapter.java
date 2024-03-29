package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroidAlpha.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalMonsterAdapter extends ArrayAdapter<Monster> {

	private Context context;
	private List mMonsters;

	public PersonalMonsterAdapter(Context context, ArrayList<Monster> monsters) {
		super(context, R.layout.row_team_pokemon, monsters);
		this.context = context;
		mMonsters = monsters;
	}

	public void remove(int p) {
		mMonsters.remove(p);
		notifyDataSetChanged();
	}

	@Override
	public Monster getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_team_pokemon, parent,
				false);

		// // find view
		TextView monsterName = (TextView) rowView
				.findViewById(R.id.pokemon_name);
		ImageView monster_pictures = (ImageView) rowView
				.findViewById(R.id.pokemon_picture);
		TextView monsterSex = (TextView) rowView.findViewById(R.id.pokemon_sex);
		TextView monsterLevel = (TextView) rowView
				.findViewById(R.id.pokemon_level);

		final Monster mPoke = getItem(position);

		// // set text of pokemon
		monsterName.setText(mPoke.getName());
		monsterName.setTextColor(Color.BLACK);

		// // set level of pokemon
		String lvl = String.format(
				getContext().getString(R.string.pokemon_level),
				mPoke.getLevel());
		monsterLevel.setText(lvl);
		monsterLevel.setTextColor(Color.BLACK);

		// // set image of pokemon
		int id = mPoke.getId();
		Bitmap pictures = Monster.getImagBitmap(context, id);
		monster_pictures.setImageBitmap(pictures);

		// / set sex of pokemon
		String sexChar = Monster.getSexAsci(mPoke.getSex());
		monsterSex.setText(sexChar);
		monsterSex.setTextColor(StaticClass.getColorFromSexAsci(sexChar));

		return rowView;
	}
}
