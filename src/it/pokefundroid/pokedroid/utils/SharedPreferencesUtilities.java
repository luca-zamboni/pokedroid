package it.pokefundroid.pokedroid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;

public class SharedPreferencesUtilities {

	private static final String USER_HEIGHT_KEY = "userheight";
	private static final String FIRST_START = "firststart";
	private static final String USER_IS_BAD = "isbad";
	private static final float DEFAULT_USER_HEIGHT = 1.60f;
	private static String USER_HOME_SET_DATE = "homesetdate";
	private static String USER_HOME_LATITUDE = "userhomelat";
	private static String USER_HOME_LONGITUDE = "userhomelon";
	private static String USER_HOME_ACCURACY = "userhomeacc";
	private static final long DAY_MIN_TO_CHANGE = 1;
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	private static SharedPreferences getSp(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("POKEDROID",
				Context.MODE_PRIVATE);
		return sp;
	}

	private static Editor getEditor(Context ctx) {
		return getSp(ctx).edit();
	}

	public static double getUserHeight(Context ctx) {
		return getSp(ctx).getFloat(USER_HEIGHT_KEY, DEFAULT_USER_HEIGHT);
	}

	public static void setUserHeight(Context ctx, float height) {
		Editor editor = getEditor(ctx);
		editor.putFloat(USER_HEIGHT_KEY, height);
		editor.commit();
	}

	public static boolean isFirstStart(Context ctx) {
		return getSp(ctx).getBoolean(FIRST_START, true);
	}

	public static void setFirstStartCompleted(Context ctx) {
		Editor editor = getEditor(ctx);
		editor.putBoolean(FIRST_START, false);
		editor.commit();
	}

	public static boolean isBadGuy(Context ctx) {
		return getSp(ctx).getBoolean(USER_IS_BAD, false);
	}

	public static void setBad(Context ctx) {
		Editor editor = getEditor(ctx);
		editor.putBoolean(USER_IS_BAD, true);
		editor.commit();
	}

	public static Location getHomeLocation(Context ctx) {
		Location out = new Location("network");
		SharedPreferences sp = getSp(ctx);
		out.setLatitude(sp.getFloat(USER_HOME_LATITUDE, -1));
		if (out.getLatitude() == -1)
			return null;
		out.setLongitude(sp.getFloat(USER_HOME_LONGITUDE, -1));
		out.setAccuracy(sp.getFloat(USER_HOME_ACCURACY, -1));
		return out;
	}

	public static void setHome(Context ctx, Location l) {
		Date today = new Date();
		Editor editor = getEditor(ctx);
		editor.putFloat(USER_HOME_LATITUDE, (float) l.getLatitude());
		editor.putFloat(USER_HOME_LONGITUDE, (float) l.getLongitude());
		editor.putFloat(USER_HOME_ACCURACY, (float) l.getAccuracy());
		editor.putString(USER_HOME_SET_DATE, formatter.format(today));
		editor.commit();
		return;
	}

	public static boolean isAtHome(Context ctx, Location l) {
		Location home = getHomeLocation(ctx);
		float[] results = new float[1];
		if (home != null) {
			Location.distanceBetween(l.getLatitude(), l.getLongitude(),
					home.getLatitude(), home.getLongitude(), results);
			return results[0] <= home.getAccuracy();
		}
		return false;

	}

	public static boolean canSetHome(Context ctx) {
		String saveDate = getSp(ctx).getString(USER_HOME_SET_DATE, null);
		Date today = new Date();
		Date saved = null;
		if (saveDate != null)
			try {
				saved = formatter.parse(saveDate);
			} catch (ParseException e) {
				return true;
			}
		if (saved != null && today.after(saved))
			return daysBetween(saved, today) >= DAY_MIN_TO_CHANGE;
		return true;
	}

	private static long daysBetween(Date startDate, Date endDate) {
		Calendar sDate = getDatePart(startDate);
		Calendar eDate = getDatePart(endDate);

		long daysBetween = 0;
		while (sDate.before(eDate)) {
			sDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	private static Calendar getDatePart(Date date) {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0); // set millisecond in second

		return cal; // return the date part
	}
}
