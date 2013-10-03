package it.pokefundroid.pokedroid.models;


import it.pokefundroid.pokedroid.utils.BaseHelper;
import it.pokefundroid.pokedroid.utils.StaticClass;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PersonalPokemon extends Pokemon {

	public String my_name;
	private int id;
	private int sex;
	private int found_x;
	private int found_y;
	private int level;
	

	public PersonalPokemon(int id,String my_name, int sex, int found_x, int found_y, int level) {
		super(id);
		this.id = id;
		this.my_name = my_name;
		this.sex = sex;
		this.found_x = found_x;
		this.found_y = found_y;
		this.level = 20;
	}

	public void saveOnDatabase() {
		String insertPersonalPokemon = "INSERT INTO "+ BaseHelper.TABLE_PERSONAL_POKEMON +" ";
		insertPersonalPokemon += " ( " +
				BaseHelper.BASE_POKEMON_ID+"," +
				BaseHelper.SEX+"," +
				BaseHelper.FOUND_X+"," +
				BaseHelper.FOUND_Y+"," +
				BaseHelper.MY_NAME+" ) ";
		insertPersonalPokemon += " VALUES ( " +
				id +"," +
				sex +"," +
				found_x +"," +
				found_y +"," +
				"'"+my_name +"'); "  ;
		
		Log.e("asd", insertPersonalPokemon);
		
		StaticClass.dbpoke.executeSQL(insertPersonalPokemon);
		
	}

	public String getMy_name() {
		return my_name;
	}


	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getSex() {
		return sex;
	}


	public int getFound_x() {
		return found_x;
	}


	public int getFound_y() {
		return found_y;
	}
	
	///// static metod

	public static String getSexAsci(int sex) {
		String s = " ";
		if(sex == 1){
			s = "♀";
		}else{
			if(sex == 2)
				s = "♂";
		}
		return s;
	}
	
	public static ArrayList<PersonalPokemon> getAllPersonaPokemon(Context ctx){
		if(StaticClass.dbpoke==null)
			StaticClass.openBatabaseConection(ctx.getApplicationContext());
		StaticClass.dbpoke.openDataBase();
		Cursor c = StaticClass.dbpoke.dbpoke.rawQuery("SELECT * FROM "+BaseHelper.TABLE_PERSONAL_POKEMON, null);
		int id,sex,found_x,found_y;
		String my_name;
		ArrayList<PersonalPokemon> mPokemon = new ArrayList<PersonalPokemon>();
		while(c.moveToNext()){
			id = c.getInt(c.getColumnIndex(BaseHelper.BASE_POKEMON_ID));

			Log.e("",id+"");
			my_name = c.getString(c.getColumnIndex(BaseHelper.MY_NAME));
			sex = c.getInt(c.getColumnIndex(BaseHelper.SEX));
			found_x = c.getInt(c.getColumnIndex(BaseHelper.FOUND_X));
			found_y = c.getInt(c.getColumnIndex(BaseHelper.FOUND_Y));
			mPokemon.add(new PersonalPokemon(id, my_name, sex, found_x, found_y, 20));
		}
		
		StaticClass.dbpoke.close();
		
		return mPokemon;
	}
	
}
