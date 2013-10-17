package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.ExchangeActivity;
import it.pokefundroid.pokedroid.Menu_Activity;
import it.pokefundroid.pokedroid.R;
import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.StaticClass;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
	private ArrayList<Monster> mMonsters;

	public PersonalMonsterAdapter(Context context,
			ArrayList<Monster> monsters) {
		super(context, R.layout.row_team_pokemon, monsters);
		this.context = context;
		this.mMonsters = monsters;
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

		final Monster mPoke = mMonsters.get(position);

		// // set text of pokemon
		monsterName.setText(mPoke.getName());
		monsterName.setTextColor(Color.BLACK);

		// // set level of pokemon
		monsterLevel.setText("lv. " + mPoke.getLevel());
		monsterLevel.setTextColor(Color.BLACK);

		// // set image of pokemon
		int id = mPoke.getId();
		Bitmap pictures = Monster.getImagBitmap(context, id);
		monster_pictures.setImageBitmap(pictures);

		// / set sex of pokemon
		String sexChar = Monster.getSexAsci(mPoke.getSex());
		monsterSex.setText(sexChar);
		monsterSex.setTextColor(StaticClass.getColorFromSexAsci(sexChar));

		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflate = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View m = inflate.inflate(R.layout.dialog_stat_viewer, null,
						false);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				TextView thp = (TextView) m.findViewById(R.id.poke_hp);
				TextView tatt = (TextView) m.findViewById(R.id.poke_attak);
				TextView tdef = (TextView) m.findViewById(R.id.poke_defense);
				TextView tspatt = (TextView) m.findViewById(R.id.poke_spattak);
				TextView tspdef = (TextView) m
						.findViewById(R.id.poke_spdefense);
				TextView tspeed = (TextView) m.findViewById(R.id.poke_speed);

				thp.setText("" + mPoke.getHp());
				tatt.setText("" + mPoke.getAttack());
				tdef.setText("" + mPoke.getDefence());
				tspatt.setText("" + mPoke.getSpecialAttack());
				tspdef.setText("" + mPoke.getSpecialDefence());
				tspeed.setText("" + mPoke.getSpeed());

				builder.setTitle(R.string.title_dialog_viewstatpoke);
				builder.setView(m);
				builder.create().show();
			}
		});
		rowView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				 Intent i = new Intent(getContext(), ExchangeActivity.class);
				 i.putExtra(ExchangeActivity.PASSED_MONSTER_KEY,mPoke);
				 getContext().startActivity(i);
				return true;
			}
		});
		return rowView;
	}
}
