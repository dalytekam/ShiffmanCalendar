package com.example.shiffmancalendar;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DataEntryDatePhase2 extends DataEntryDate {

	TextView title;
	EditText cigs;
	EditText gums;
	Button cancel;
	Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_phase2);
		
		title = (TextView) findViewById(R.id.title);
		cigs = (EditText) findViewById(R.id.editText1);
		gums = (EditText) findViewById(R.id.editText2);
		cancel = (Button) findViewById(R.id.cancel_button);
		save = (Button) findViewById(R.id.save_button);
		
		title.setText(title.getText() + "\n" + super.formattedDate);
		
		if (super.existingData != null) {
			cigs.setText(super.existingData.getAsString(DBHelper.KEY_CIG_COUNT));
			gums.setText(super.existingData.getAsString(DBHelper.KEY_GUM_COUNT));
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
				String gum_count = gums.getText().toString();
				if (gum_count.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "Please enter a value in the gum count field", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentValues values = initContentValues();
				values.put(DBHelper.KEY_DATE, date);
				values.put(DBHelper.KEY_CIG_COUNT, cig_count);
				values.put(DBHelper.KEY_GUM_COUNT, gum_count);
				
				saveOrUpdateEntry(values);
				finish();
			}
			
		});
	}

}
