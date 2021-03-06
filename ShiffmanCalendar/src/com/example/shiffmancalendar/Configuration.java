package com.example.shiffmancalendar;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

public class Configuration extends Activity {

//	EditText id;
//	EditText session;
	//Spinner phase;
	RadioButton[] phase;
	DatePicker start;
	DatePicker end;
	Button cancel;
	Button save;
	
	public static final String[] phaseNames = {"QUITS-Baseline", "QUITS-Experimental", "LONIC-Visit_1", "LONIC-Visits_2-11"};
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.configuration_layout);
		
//		id = (EditText) findViewById(R.id.config_id_editText);
//		session = (EditText) findViewById(R.id.config_session_editText);
		phase = new RadioButton[4];
		phase[0] = (RadioButton) findViewById(R.id.config_phase1);
		phase[1] = (RadioButton) findViewById(R.id.config_phase2);
		phase[2] = (RadioButton) findViewById(R.id.config_phase3);
		phase[3] = (RadioButton) findViewById(R.id.config_phase4);
		for (int i=0; i<phase.length; i++) {
			phase[i].setText(phaseNames[i]);
		}
		
		start = (DatePicker) findViewById(R.id.config_start_datePicker);
		end = (DatePicker) findViewById(R.id.config_end_datePicker);
		cancel = (Button) findViewById(R.id.config_cancel);
		save = (Button) findViewById(R.id.config_save);
		
        
        cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				String idText = id.getText().toString();
//				if (idText.equalsIgnoreCase("")) {
//					Toast.makeText(getApplicationContext(), "You forgot to enter a participant id!", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				
//				String sessionText = session.getText().toString();
//				if (sessionText.equalsIgnoreCase("")) {
//					Toast.makeText(getApplicationContext(), "You forgot to enter a session number!", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
				int phase_checked = getSelectedPhase();
				if (phase_checked == -1) {
					Toast.makeText(getApplicationContext(), "You forgot to select a study!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (!checkStartDateBeforeEndDate()) {
					Toast.makeText(getApplicationContext(), "Start date must be equal to or before the end date", Toast.LENGTH_SHORT).show();
					return;
				}
				
         	   	if (unsavedData()) {
         	   		showUnsavedDataDialog();
         	   		return;
         	   	}

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Save Participant Configuration?").setMessage("This will overwrite the current participant configuration.")
		               .setPositiveButton("Yes, save", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   
			       				save_data_to_prefs();
			    				
			    				finish();
		                   }

						private void save_data_to_prefs() {
							String idText = "default"; //id.getText().toString();
							String sessionNum = "0"; //session.getText().toString();
							int phase_num = getSelectedPhase();
							Calendar startCal = Calendar.getInstance();
							startCal.clear();
							startCal.set(start.getYear(), start.getMonth(), start.getDayOfMonth());
							long startDate = startCal.getTimeInMillis();
							Calendar endCal = Calendar.getInstance();
							endCal.clear();
							endCal.set(end.getYear(), end.getMonth(), end.getDayOfMonth());
							long endDate = endCal.getTimeInMillis();
							
							SharedPreferences prefs = getSharedPreferences("shiffman_calendar", 0);
							Editor edit = prefs.edit();
							//edit.clear();
							edit.putString("id", idText);
							edit.putString("session", sessionNum);
							edit.putInt("phase", phase_num);
							edit.putLong("start", startDate);
							edit.putLong("end", endDate);
							edit.commit();
						}
		               })
		               .setNegativeButton("No, don't save!", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // User cancelled the dialog
		                	   finish();
		                   }
		               });
		        // Create the AlertDialog object and return it
		        AlertDialog dialog = builder.create();
		        dialog.show();
			}
			
			private boolean unsavedData() {
				// TODO Auto-generated method stub
				DBHelper db = new DBHelper(getApplicationContext());
				List<ContentValues> data = db.getAllEntries("default", "0", null);
				
				return data.size() > 0;
			}
			
			private void showUnsavedDataDialog() {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Unsaved Data in Database!!!!").setMessage("There is unsaved data in the database. Please view the data summary and apply an ID to this data before changing the configuration.")
		        	   .setCancelable(false)
		               .setPositiveButton("View Data Summary", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   
			       				Intent intent = new Intent(getApplicationContext(), SummarizeData.class);
			       				startActivity(intent);
		                   }
	
		               });
		        // Create the AlertDialog object and return it
		        AlertDialog dialog = builder.create();
		        dialog.show();
			}

			private boolean checkStartDateBeforeEndDate() {
				Calendar startCal = Calendar.getInstance();
				startCal.clear();
				startCal.set(start.getYear(), start.getMonth(), start.getDayOfMonth());
				long startDate = startCal.getTimeInMillis();
				Calendar endCal = Calendar.getInstance();
				endCal.clear();
				endCal.set(end.getYear(), end.getMonth(), end.getDayOfMonth());
				long endDate = endCal.getTimeInMillis();
				return startDate <= endDate;
			}

			private int getSelectedPhase() {
				int phase_checked = -1;
				for (int i=0; i<phase.length; i++) {
					if (phase[i].isChecked()) {
						phase_checked = i;
					}
				}
				return phase_checked;
			}
        	
        });
	}

}
