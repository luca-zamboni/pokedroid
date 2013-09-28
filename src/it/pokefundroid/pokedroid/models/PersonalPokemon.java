package it.pokefundroid.pokedroid.models;


import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

public class PersonalPokemon extends Pokemon {
	
	private int id;
	public String my_name;
	public int sex;
	public int found_x;
	public int found_y;
	
	private BaseAdapter a;
	
	public PersonalPokemon(Context context,int id,String my_name, int sex, int found_x, int found_y) {
		super(context,id);
		this.id = id;
		this.my_name = my_name;
		this.sex = sex;
		this.found_x = found_x;
		this.found_y = found_y;
	}
	
	public static ArrayList<PersonalPokemon> getAllPersonaPokemon(Context con){
		/*int id;
		String my_name;
		int sex;
		int found_x;
		int found_y;
		BaseAdapter a = new BaseAdapter(con);
		a.open();
		Cursor c = a.database.query(BaseHelper.TABLE_PERSONAL_POKEMON,new String[] {BaseHelper.BASE_POKEMON_ID,
				BaseHelper.MY_NAME, 
				BaseHelper.SEX, 
				BaseHelper.FOUND_X, 
				BaseHelper.FOUND_Y
				},null,null,null,null,null);
		c.moveToFirst();
		ArrayList<PersonalPokemon> per = new ArrayList<PersonalPokemon>();
		while(c.moveToNext()){
			id = c.getInt(0);
			my_name = c.getString(1);
			sex = c.getInt(2);
			found_x = c.getInt(3);
			found_y = c.getInt(4);
			per.add(new PersonalPokemon(con, id, my_name, sex, found_x, found_y));
		}
		a.close();
		return per;*/
		return null;
	}
	
}
