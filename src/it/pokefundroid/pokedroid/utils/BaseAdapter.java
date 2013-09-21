package it.pokefundroid.pokedroid.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	
	public Cursor basePokemonById(String[] columns, int id ){
		Cursor cursor = database.query("pokemon",columns,"id = " + id,null,null,null,null);
		return cursor;
	}
	
	public void insertMyPokemon(){
		
	}
	

}
