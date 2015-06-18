package com.example.shiffmancalendar;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;


public class DateEnteredDecorator implements CalendarCellDecorator {

	@Override
	public void decorate(CalendarCellView cellView, Date date) {
		SharedPreferences prefs = cellView.getContext().getSharedPreferences("shiffman_calendar", 0);
		String id = prefs.getString("id", "unknown");
		String session = prefs.getString("session", "unknown");
		
		DBHelper db = new DBHelper(cellView.getContext());
		ContentValues value = db.entryExists(id, session, date.getTime());
		
		if (value != null) {
			cellView.setBackgroundColor(cellView.getResources().getColor(R.color.LightGreen));
		}
	
	}

}
