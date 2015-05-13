package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

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
		
		DBHelper db = new DBHelper(getApplicationContext());
		existingData = db.entryExists(date);
	}
	
	protected void saveOrUpdateEntry(ContentValues values) {
		DBHelper db = new DBHelper(getApplicationContext());
		
		if (existingData == null) {
			db.addEntry(values);
		} else {
			db.updateEntry(values, existingData.getAsString(DBHelper.KEY_ID));
		}
	}
}
