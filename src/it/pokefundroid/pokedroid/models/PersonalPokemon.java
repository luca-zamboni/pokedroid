package it.pokefundroid.pokedroid.models;


import it.pokefundroid.pokedroid.utils.BaseHelper;
import it.pokefundroid.pokedroid.utils.StaticClass;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PersonalPokemon extends Pokemon {
	
	public enum PokemonSex{
		MALE,
		FEMALE, GENDERLESS
	}

	public String my_name;
	private int id;
	private PokemonSex sex;
	private double found_x;
	private double found_y;
	private int level;
	

	public PersonalPokemon(int id,String my_name, PokemonSex sex, double found_x, double found_y, int level) {
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
		int sex;
		if(this.sex == PokemonSex.MALE)
			sex=1;
		else if(this.sex == PokemonSex.FEMALE)
			sex=2;
		else
			sex=3;
		insertPersonalPokemon += " VALUES ( " +
				id +"," +
				sex +"," +
				found_x +"," +
				found_y +"," +
				"'"+my_name +"'); "  ;
		
		//Log.e("asd", insertPersonalPokemon);
		
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

	public PokemonSex getSex() {
		return sex;
	}


	public double getFound_x() {
		return found_x;
	}


	public double getFound_y() {
		return found_y;
	}
	
	///// static metod

	public static String getSexAsci(PokemonSex sex) {
		String s = " ";
		if(sex == PokemonSex.MALE){
			s = "♀";
		}else{
			if(sex == PokemonSex.FEMALE)
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
		PokemonSex realSex;
		String my_name;
		c.moveToFirst();
		ArrayList<PersonalPokemon> mPokemon = new ArrayList<PersonalPokemon>();
		while(c.moveToNext()){
			id = c.getInt(c.getColumnIndex(BaseHelper.BASE_POKEMON_ID));

			Log.e("",id+"");
			my_name = c.getString(c.getColumnIndex(BaseHelper.MY_NAME));
			sex = c.getInt(c.getColumnIndex(BaseHelper.SEX));
			if(sex ==1)
				realSex = PokemonSex.MALE;
			else if(sex == 2)
				realSex = PokemonSex.FEMALE;
			else
				realSex = PokemonSex.GENDERLESS;
			found_x = c.getInt(c.getColumnIndex(BaseHelper.FOUND_X));
			found_y = c.getInt(c.getColumnIndex(BaseHelper.FOUND_Y));
			//TODO remove hardcoded
			mPokemon.add(new PersonalPokemon(id, my_name, realSex, found_x, found_y, 20));
		}
		
		StaticClass.dbpoke.close();
		
		return mPokemon;
	}
	
}
