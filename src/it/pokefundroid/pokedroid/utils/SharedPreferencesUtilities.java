package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtilities {
	private SharedPreferences sp;
	
	private static final String USER_HEIGHT_KEY = "userheight";
	private static final float DEFAULT_USER_HEIGHT = 1.60f;
	
	private static SharedPreferences getSp(Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("POKEDROID", Context.MODE_PRIVATE);
		return sp;
	}
	private static Editor getEditor(Context ctx){
		return getSp(ctx).edit();
	}
	public static double getUserHeight(Context ctx){
		return getSp(ctx).getFloat(USER_HEIGHT_KEY, DEFAULT_USER_HEIGHT);
	}
	public static void setUserHeight(Context ctx,float height){
		Editor editor = getEditor(ctx);
		editor.putFloat(USER_HEIGHT_KEY, height);
		editor.commit();
	}
}
