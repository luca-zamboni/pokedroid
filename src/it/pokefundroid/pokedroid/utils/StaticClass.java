package it.pokefundroid.pokedroid.utils;

import android.content.Context;

public class StaticClass {
	public static BaseHelper dbpoke;
	
	
	public static void openBatabaseConection(Context c){
		dbpoke = new BaseHelper(c);
		dbpoke.openDataBase();
	}
}
