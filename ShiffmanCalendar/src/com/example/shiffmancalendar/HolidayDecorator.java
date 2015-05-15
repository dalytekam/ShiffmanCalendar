package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

public class HolidayDecorator implements CalendarCellDecorator {

	@Override
	public void decorate(CalendarCellView cellView, Date date) {
		SharedPreferences prefs = cellView.getContext().getSharedPreferences("shiffman_calendar", 0);
		Set<String> holidays = prefs.getStringSet("holidays", null);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		String dateStr = format.format(date);
		
		String holiday = null;
		for (String h: holidays) {
			String[] parts = h.split(":");
			if (dateStr.equalsIgnoreCase(parts[0])) {
				holiday = parts[1];
			}
		}
		if (holiday != null) {
			// decorate!
			String dateString = Integer.toString(date.getDate());
			SpannableString string = new SpannableString(dateString + "\n" + holiday);
		    string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
		        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		    cellView.setText(string);
		    cellView.setTextColor(Color.BLUE);
		}
	}

}
