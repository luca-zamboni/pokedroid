package it.pokefundroid.pokedroid.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StaticClass {
	public static BaseHelper dbpoke;
	
	
	public static void openBatabaseConection(Context c){
		dbpoke = new BaseHelper(c);
	}
	
	public static Bitmap getBitmapFromAssats(Context c,String path){
		AssetManager assetManager = c.getAssets();
	    InputStream istr;
	    Bitmap bitmap = null; 
	    try {
	        istr = assetManager.open(path);
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (IOException e) {
	    }

	    return bitmap;
	}
	
}
