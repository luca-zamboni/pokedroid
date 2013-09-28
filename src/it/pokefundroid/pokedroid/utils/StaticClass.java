package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.database.Cursor;

public class StaticClass {
	public static BaseHelper dbpoke;
	
	
	public static void openBatabaseConection(Context c){
		dbpoke = new BaseHelper(c);
	}
	
	public static Cursor query(String tableName,String[] culomns,String where){
		dbpoke.openDataBase();
		Cursor ret = dbpoke.query(tableName, culomns, where);
		dbpoke.close();
		return ret;
	}
}
