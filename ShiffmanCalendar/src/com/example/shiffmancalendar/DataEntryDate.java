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
		String id = prefs.getString("id", "unknown");
		String session = prefs.getString("session", "unknown");
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
}
