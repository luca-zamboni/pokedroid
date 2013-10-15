package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtilities {
	private SharedPreferences sp;
	
	private static final String USER_HEIGHT_KEY = "userheight";
	private static final String FIRST_START = "firststart";
	private static final String USER_IS_BAD = "isbad";
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
	public static boolean isFirstStart(Context ctx){
		return getSp(ctx).getBoolean(FIRST_START, true);
	}
	public static void setFirstStartCompleted(Context ctx){
		Editor editor = getEditor(ctx);
		editor.putBoolean(FIRST_START, false);
		editor.commit();
	}
	public static boolean isBadGuy(Context ctx){
		return getSp(ctx).getBoolean(USER_IS_BAD, false);
	}
	public static void setBad(Context ctx){
		Editor editor = getEditor(ctx);
		editor.putBoolean(USER_IS_BAD, true);
		editor.commit();
	}
}
