package it.pokefundroid.pokedroid.models;


import java.io.IOException;
import java.io.InputStream;

import it.pokefundroid.pokedroid.utils.BaseHelper;
import it.pokefundroid.pokedroid.utils.StaticClass;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class PersonalPokemon extends Pokemon {

	public String my_name;
	private int id;
	private int sex;
	private int found_x;
	private int found_y;
	

	public PersonalPokemon(int id,String my_name, int sex, int found_x, int found_y) {
		super(id);
		this.id = id;
		this.my_name = my_name;
		this.sex = sex;
		this.found_x = found_x;
		this.found_y = found_y;
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
				my_name +" ); "  ;
		
		Log.e("asd", insertPersonalPokemon);
		
		StaticClass.dbpoke.executeSQL(insertPersonalPokemon);
		
	}

	public String getMy_name() {
		return my_name;
	}


	public int getId() {
		return id;
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
	
}
