package com.example.shiffmancalendar;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Configuration extends Activity {

	EditText id;
	Spinner phase;
	DatePicker start;
	DatePicker end;
	Button cancel;
	Button save;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.configuration_layout);
		
		id = (EditText) findViewById(R.id.config_id_editText);
		phase = (Spinner) findViewById(R.id.config_phase_spinner);
		start = (DatePicker) findViewById(R.id.config_start_datePicker);
		end = (DatePicker) findViewById(R.id.config_end_datePicker);
		cancel = (Button) findViewById(R.id.config_cancel);
		save = (Button) findViewById(R.id.config_save);
		
		String[] items = new String[] {"Phase 1","Phase 2","Phase 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phase.setAdapter(adapter);
        phase.setSelection(0);
        
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
				String idText = id.getText().toString();
				if (idText.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(), "You forgot to enter a participant id!", Toast.LENGTH_SHORT).show();
					return;
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Save Participant Configuration?").setMessage("You will lose any un-exported data!!!!")
		               .setPositiveButton("Yes, save", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
			       				save_data_to_prefs();
			    				
			    				clear_db();
			    				
			    				finish();
		                   }

						private void clear_db() {
							getApplicationContext().deleteDatabase(DBHelper.DB_NAME);	
						}

						private void save_data_to_prefs() {
							String idText = id.getText().toString();
							int phase_num = phase.getSelectedItemPosition();
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
							edit.clear();
							edit.putString("id", idText);
							edit.putInt("phase", phase_num);
							edit.putLong("start", startDate);
							edit.putLong("end", endDate);
							edit.commit();
						}
		               })
		               .setNegativeButton("No, I need to export data", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // User cancelled the dialog
		                	   finish();
		                   }
		               });
		        // Create the AlertDialog object and return it
		        AlertDialog dialog = builder.create();
		        dialog.show();
			}
        	
        });
	}

}
