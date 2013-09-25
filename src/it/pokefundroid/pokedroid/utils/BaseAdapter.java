package it.pokefundroid.pokedroid.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseAdapter {
	
	private Context context;
	public SQLiteDatabase database;
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
		
	}
}
