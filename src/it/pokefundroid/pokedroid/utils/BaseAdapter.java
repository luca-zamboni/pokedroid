package it.pokefundroid.pokedroid.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private BaseHelper dbHelper;
	
	public BaseAdapter(Context context) {
	    this.context = context;
	  }
	 
	  public BaseAdapter open() throws SQLException {
	    dbHelper = new BaseHelper(context);
	    database = dbHelper.getWritableDatabase();
	    return this;
	  }
	  
	  public void close() {
		    dbHelper.close();
		  }
	
	
	public void insertPokemonBase(int id,String name,String type,int rarity){
		ContentValues pokemon = new ContentValues();
		//pokemon.put( BaseHelper.ID, id );
		pokemon.put( BaseHelper.NAME, name );
		pokemon.put( BaseHelper.TYPE, type );
		pokemon.put( BaseHelper.RARITY, rarity );
		database.insert("pokemon", null, pokemon);
	}
	
	public void insertPersonalPokemon(int id,String my_name,int sex,int found_x,int found_y){
		ContentValues pokemon = new ContentValues();
		pokemon.put( BaseHelper.BASE_POKEMON_ID, id );
		pokemon.put( BaseHelper.MY_NAME, my_name );
		pokemon.put( BaseHelper.FOUND_Y, found_y );
		pokemon.put( BaseHelper.FOUND_X, found_x );
		pokemon.put( BaseHelper.SEX, sex );
		
		database.insert(BaseHelper.TABLE_PERSONAL_POKEMON, null, pokemon);
	}
	
	public Cursor getBasePokemonById(String[] columns, int id ){
		Cursor cursor = database.query(BaseHelper.TABLE_GENERAL_POKEMON,columns,"id = " + id,null,null,null,null);
		return cursor;
	}
	
	public Cursor getAllPersonalPokemon(String[] columns){
		Cursor cursor = database.query(BaseHelper.TABLE_PERSONAL_POKEMON,columns,null,null,null,null,null);
		return cursor;
	}
	

}
