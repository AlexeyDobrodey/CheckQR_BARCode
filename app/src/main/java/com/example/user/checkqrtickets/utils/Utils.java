package com.example.user.checkqrtickets.utils;

import java.text.ParseException;
import java.util.Calendar;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.example.user.checkqrtickets.entities.Client;

public class Utils {
	
	//private static String FILE_NAME = "httpconf.txt";
	private final static String VFUSION_HTTP = "http://vfusion.com.ua";
	
	private static final String SAVED_TEXT = "http_save";
	
	public static Calendar getDate(long milliseconds) throws ParseException {
		Calendar calendar  = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return calendar;
	}
	
	private static String correctTime(String time) {
		if(time.length() == 1) {
			time = "0" + time;
		}
		return time;
	}
	
	public static String getTime(Calendar date) {
		return correctTime(String.valueOf(date.get(Calendar.HOUR_OF_DAY))) + ":" + correctTime(String.valueOf(date.get(Calendar.MINUTE)));
	}
	

	public static void readHTTPFromFile(Activity activity) {
		SharedPreferences sPref = activity.getPreferences(Activity.MODE_PRIVATE);
		Client.HTTP_SITE = sPref.getString(SAVED_TEXT, "");
		if (Client.HTTP_SITE.equals("")) {
			Client.HTTP_SITE = VFUSION_HTTP;
			writeHTTPToFile(activity, VFUSION_HTTP);
		}
	}

	
	public static void writeHTTPToFile(Activity activity, String httpString){
		SharedPreferences sPref = activity.getPreferences(Activity.MODE_PRIVATE);
		Editor edit = sPref.edit();
		edit.putString(SAVED_TEXT, httpString);
		edit.commit();
	}

	public static String correctNameRepresentation(String nameRepresentation) {
		if(nameRepresentation.length() > 24)
			return nameRepresentation.substring(0, 24) + "...";
		return nameRepresentation;
	}

	public static boolean isDateNow(Calendar date) {
        Calendar calendarNow = Calendar.getInstance();
        if (("" + date.get(Calendar.YEAR)).equals(""
                + calendarNow.get(Calendar.YEAR))
                && ("" + date.get(Calendar.MONTH)).equals(""
                + calendarNow.get(Calendar.MONTH))
                && ("" + date.get(Calendar.DATE)).equals(""
                + calendarNow.get(Calendar.DATE))) {
            return true;
        }
        return false;
    }

	public static void hideKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if(view != null) {
			((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
