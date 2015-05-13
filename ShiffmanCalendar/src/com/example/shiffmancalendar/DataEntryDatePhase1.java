package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DataEntryDatePhase1 extends Activity {
	
	TextView title;
	EditText cigs;
	Button cancel;
	Button save;
	
	long date = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_phase1);
		
		title = (TextView) findViewById(R.id.title);
		cigs = (EditText) findViewById(R.id.editText1);
		cancel = (Button) findViewById(R.id.cancel_button);
		save = (Button) findViewById(R.id.save_button);
		
		Intent intent = getIntent();
		date = intent.getLongExtra("date", 0);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM yyyy");
		String formatted = format1.format(cal.getTime());
		
		title.setText(title.getText() + "\n" + formatted);
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();	
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cig_count = cigs.getText().toString();
				if (cig_count.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "Please enter a value in the cigarette count field", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentValues values = new ContentValues();
				DBHelper db = new DBHelper(getApplicationContext());
				
				values.put(DBHelper.KEY_DATE, date);
				values.put(DBHelper.KEY_CIG_COUNT, cig_count);
				
				db.addEntry(values);
				
			}
			
		});
	}

}
