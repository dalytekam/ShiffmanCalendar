package com.example.shiffmancalendar;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;


public class DateEnteredDecorator implements CalendarCellDecorator {

	@Override
	public void decorate(CalendarCellView cellView, Date date) {
		
		DBHelper db = new DBHelper(cellView.getContext());
		ContentValues value = db.entryExists(date.getTime());
		
		if (value != null) {
			cellView.setBackgroundColor(Color.GREEN);
		}
	
	}

}
