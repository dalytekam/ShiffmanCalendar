package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

public class DataEntryDatePhase1 extends DataEntryDate {
	
	TextView title;
	EditText cigs;
	Button cancel;
	Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_phase1);
		
		title = (TextView) findViewById(R.id.title);
		cigs = (EditText) findViewById(R.id.editText1);
		cancel = (Button) findViewById(R.id.cancel_button);
		save = (Button) findViewById(R.id.save_button);
		
		title.setText(title.getText() + "\n" + super.formattedDate);
		
		if (super.existingData != null) {
			cigs.setText(super.existingData.getAsString(DBHelper.KEY_CIG_COUNT));
		}
		
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
				values.put(DBHelper.KEY_PID, id);
				values.put(DBHelper.KEY_STUDY, study);
				values.put(DBHelper.KEY_SESSION, session);
				values.put(DBHelper.KEY_DATE, date);
				values.put(DBHelper.KEY_CIG_COUNT, cig_count);
				
				saveOrUpdateEntry(values);
				finish();
			}
			
		});
	}

}
