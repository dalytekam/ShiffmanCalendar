package com.example.shiffmancalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;

public class DataEntryCalendar extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		CalendarPickerView cal;
		SharedPreferences prefs;
		Button viewSummary;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_calendar,
					container, false);
			return rootView;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
			
			prefs = getActivity().getSharedPreferences("shiffman_calendar", 0);
			
			initCal();
			
			viewSummary = (Button) getActivity().findViewById(R.id.button2);
			viewSummary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity().getApplicationContext(), SummarizeData.class);
					startActivity(intent);
				}
				
			});
		}

		private void initCal() {
			cal = (CalendarPickerView)getView().findViewById(R.id.calendar_view);
			long start = prefs.getLong("start", System.currentTimeMillis());
			long end = prefs.getLong("end", System.currentTimeMillis());
			Calendar minDate = Calendar.getInstance();
			minDate.setTimeInMillis(start);
			Calendar maxDate = Calendar.getInstance();
			maxDate.setTimeInMillis(end);
			maxDate.add(Calendar.DAY_OF_MONTH, 1);

			cal.init(minDate.getTime(), maxDate.getTime());
			cal.setOnDateSelectedListener(new OnDateSelectedListener() {

				@Override
				public void onDateSelected(Date date) {
					// TODO Auto-generated method stub
					int phase = prefs.getInt("phase", 0);
					Intent intent;
					if (phase == 0) {
						intent = new Intent(getActivity().getApplicationContext(), DataEntryDatePhase1.class);
					} else if (phase == 1) {
						intent = new Intent(getActivity().getApplicationContext(), DataEntryDatePhase2.class);
					} else {
						intent = new Intent(getActivity().getApplicationContext(), DataEntryDatePhase3.class);
					}
					Log.d("DataEntryCalendar", "date: " + date.getTime());
					intent.putExtra("date", date.getTime());
					startActivity(intent);
				}

				@Override
				public void onDateUnselected(Date date) {
					// TODO Auto-generated method stub
					
				}
				
			});
			List<CalendarCellDecorator> decorators = new ArrayList();
			decorators.add(new DateEnteredDecorator());
			decorators.add(new HolidayDecorator());
			cal.setDecorators(decorators);
		}
	}

}
