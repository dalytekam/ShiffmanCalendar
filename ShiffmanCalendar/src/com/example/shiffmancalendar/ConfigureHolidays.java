package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;

public class ConfigureHolidays extends Activity {

	EditText text;
	CalendarPickerView cal;
	Button save;
	Button clear;
	Date lastDate = null;
	SharedPreferences prefs;
	Editor edit;
	Set<String> holidays = new HashSet<String>();
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holiday_layout);
		
		prefs = getSharedPreferences("shiffman_calendar", 0);
		edit = prefs.edit();
		
		text = (EditText) findViewById(R.id.holiday_editText);
		cal = (CalendarPickerView) findViewById(R.id.holiday_calendar_view);
		save = (Button) findViewById(R.id.holiday_save);
		clear = (Button) findViewById(R.id.holiday_clear);
		
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.remove("holidays");
				edit.commit();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lastDate != null) {
					saveHolidayText(lastDate);
				}
				finish();
			}
			
		});
		
		long start = prefs.getLong("start", System.currentTimeMillis());
		long end = prefs.getLong("end", System.currentTimeMillis());
		
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeInMillis(start);
		c.set(Calendar.DAY_OF_MONTH, 1); // enable selection of the entire month
		Date startDate = c.getTime();
		c.setTimeInMillis(end);
		System.out.println("end of month " + c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
		Date endDate = c.getTime();
		cal.init(startDate, endDate);
		
		cal.setOnDateSelectedListener(new OnDateSelectedListener() {

			@Override
			public void onDateSelected(Date date) {
				if (lastDate != null) {
					saveHolidayText(lastDate);
				}
				text.setText("");
				lastDate = date;
				String dateStr = format.format(date);
				for (String label : holidays) {
					String[] parts = label.split(":");
					if (dateStr.equalsIgnoreCase(parts[0])) {
						text.setText(parts[1]);
					}
				}
				
			}

			

			@Override
			public void onDateUnselected(Date date) {
				// TODO Auto-generated method stub
				
			}
			
		});
		List<CalendarCellDecorator> decorators = new ArrayList<CalendarCellDecorator>();
		decorators.add(new HolidayDecorator());
		cal.setDecorators(decorators);
	}
	
	private void saveHolidayText(Date date) {
		String label = text.getText().toString();
		String dateStr = format.format(date);
		if (!label.equalsIgnoreCase("")) {
			holidays.add(dateStr + ":" + label);
		} else {
			for (String existing : holidays) {
				String[] parts = existing.split(":");
				if (dateStr.equalsIgnoreCase(parts[0])) {
					holidays.remove(existing);
				}
			}
		}
		edit.putStringSet("holidays", holidays);
		edit.commit();
	}
}
