package com.example.shiffmancalendar;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;


public class DateEnteredDecorator implements CalendarCellDecorator {

	@Override
	public void decorate(CalendarCellView cellView, Date date) {
		String dateString = Integer.toString(date.getDate());
		Calendar comp = Calendar.getInstance();
		comp.clear();
		comp.set(2015, 4, 15, 0, 0, 0);
		if (date.getDate() == 15 && date.getMonth() == 4) {
		System.out.println("DATE STRING: " + date.getTime());
		System.out.println("DATE STRING: " + comp.getTime().getTime());
		}
		if (date.equals(comp.getTime())) {
		    SpannableString string = new SpannableString(dateString + "\ndone");
		    string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
		        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		    cellView.setText(string);
		    cellView.setBackgroundColor(Color.GREEN);
		} else {
			//System.out.println("DATE STRING: no match");
		}
	}

}
