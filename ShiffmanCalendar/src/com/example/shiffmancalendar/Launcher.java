package com.example.shiffmancalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Launcher extends Activity {

	ImageButton config, holiday, calendar, export;
	SharedPreferences prefs;
	Editor edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.launcher_layout);
		
		prefs = getSharedPreferences("shiffman_calendar", 0);
		edit = prefs.edit();
		
		config = (ImageButton) findViewById(R.id.imageButton1);
		holiday = (ImageButton) findViewById(R.id.imageButton2);
		calendar = (ImageButton) findViewById(R.id.imageButton3);
		export = (ImageButton) findViewById(R.id.imageButton4);
		
		config.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Configuration.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			
		});
		
		holiday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ConfigureHolidays.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			
		});
		
		calendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DataEntryCalendar.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			
		});
		
		export.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DataExport.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			
		});
		
		if (!deviceHasID()) {
			alertForDeviceID();
		}
	}

	private void alertForDeviceID() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter Device Identifer");
		alert.setMessage("This is the first time the application has been run since installation. Please enter a unique device identifier for this tablet.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
			  
				if (value.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "Please enter a device identifier", Toast.LENGTH_SHORT).show();
				  	return;
				}
			  
				edit.putString("deviceid", value);
				edit.commit();
			  
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	private boolean deviceHasID() {
		// TODO Auto-generated method stub
		return prefs.contains("deviceid");
	}

}
