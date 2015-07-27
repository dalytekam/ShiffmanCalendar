package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class DataEntryDate extends Activity {
	
	protected long date = 0;
	protected String formattedDate;
	protected ContentValues existingData = null;
	protected String id;
	protected String study;
	protected String session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		date = intent.getLongExtra("date", 0);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
		formattedDate = format1.format(cal.getTime());
		
		SharedPreferences prefs = getSharedPreferences("shiffman_calendar", 0);
		id = prefs.getString("id", "unknown");
		study = Configuration.phaseNames[prefs.getInt("phase", 0)];
		session = prefs.getString("session", "unknown");
		DBHelper db = new DBHelper(getApplicationContext());
		existingData = db.entryExists(id, session, date);
		Log.d("DataEntryDate", "existing data: " + existingData);
	}
	
	protected void saveOrUpdateEntry(ContentValues values) {
		DBHelper db = new DBHelper(getApplicationContext());
		Log.d("DataEntryDate save", "entered");
		if (existingData == null) {
			db.addEntry(values);
			Log.d("DataEntryDate save", "new entry " + values);
		} else {
			db.updateEntry(values, existingData.getAsString(DBHelper.KEY_ID));
		}
	}
	
	protected ContentValues initContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(DBHelper.KEY_DATE_ENTERED, System.currentTimeMillis());
		values.put(DBHelper.KEY_PID, id);
		values.put(DBHelper.KEY_STUDY, study);
		values.put(DBHelper.KEY_SESSION, session);
		values.put(DBHelper.KEY_DATE, "");
		values.put(DBHelper.KEY_CIG_COUNT, "");
		values.put(DBHelper.KEY_NRES_CIG_COUNT, "");
		values.put(DBHelper.KEY_GUM_COUNT, "");
		values.put(DBHelper.KEY_OTHER_COUNT, "");
		values.put(DBHelper.KEY_OTHER_FREE_1, "");
		values.put(DBHelper.KEY_OTHER_FREE_2, "");
		values.put(DBHelper.KEY_OTHER_TYPE_1, "");
		values.put(DBHelper.KEY_OTHER_TYPE_2, "");
		
		
		return values;
	}
}
