package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.database.Cursor;

public class StaticClass {
	public static BaseHelper dbpoke;
	
	
	public static void openBatabaseConection(Context c){
		dbpoke = new BaseHelper(c);
	}
}
