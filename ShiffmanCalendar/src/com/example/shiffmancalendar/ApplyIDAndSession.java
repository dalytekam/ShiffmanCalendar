package com.example.shiffmancalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ApplyIDAndSession extends Activity {

	Context context;
	EditText id, re_id, session, re_session;
	Button save, cancel;
	String study;
	
	Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		resultIntent = new Intent();
		setContentView(R.layout.apply_id_session_layout);
		
		id = (EditText) findViewById(R.id.pid_editText);
		re_id = (EditText) findViewById(R.id.pid_editText2);
		session = (EditText) findViewById(R.id.session_editText);
		re_session = (EditText) findViewById(R.id.session_editText2);
		save = (Button) findViewById(R.id.apply_save_button);
		cancel = (Button) findViewById(R.id.apply_cancel_button);
		
		SharedPreferences prefs = getSharedPreferences("shiffman_calendar", 0);
		study = Configuration.phaseNames[prefs.getInt("phase", 0)];
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String idValue = id.getText().toString();
				String re_idValue = re_id.getText().toString();
				String sessionValue = session.getText().toString();
				String re_sessionValue = re_session.getText().toString();
				
				if (!checkEmptyText(idValue)) {
					Toast.makeText(context, "Please enter a participant id.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkEmptyText(re_idValue)) {
					Toast.makeText(context, "Please re-enter a participant id.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkEmptyText(sessionValue)) {
					Toast.makeText(context, "Please enter a session number.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkEmptyText(re_sessionValue)) {
					Toast.makeText(context, "Please re-enter a session number.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!compareStrings(idValue, re_idValue)) {
					Toast.makeText(context, "Participant IDs do not match. Please check them.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!compareStrings(sessionValue, re_sessionValue)) {
					Toast.makeText(context, "Session numbers do not match. Please check them.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (checkSettingsExist(idValue, sessionValue, study)) {
					Toast.makeText(context, "Data already exists with the given id, session number and study version.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				showApplyDialog(idValue, sessionValue);
				
			}

			private void showApplyDialog(final String idValue, final String sessionValue) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Apply participant ID and session number to data?").setMessage("This will update several rows in the database to apply the given id and session number.")
		               .setPositiveButton("Yes, save", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
			       				DBHelper db = new DBHelper(context);
			       				int rowsUpdated = db.applyIDAndSession(idValue, sessionValue);
			    				resultIntent.putExtra("pid", idValue);
			    				resultIntent.putExtra("session", sessionValue);
			    				
			       				showFeedbackDialog(rowsUpdated);
		                   }
		               })
		               .setNegativeButton("No, don't apply!", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // User cancelled the dialog
		                   }
		               })
		               .setCancelable(false);
		        // Create the AlertDialog object and return it
		        AlertDialog dialog = builder.create();
		        dialog.show();
			}
			
			private void showFeedbackDialog(final int rowsUpdated) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Participant ID and session applied.").setMessage("" + rowsUpdated + " database entries have been updated.")
		               .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   setResult(RESULT_OK, resultIntent);
		                	   finish();
		                   }
		               })
		               .setCancelable(false);
		        // Create the AlertDialog object and return it
		        AlertDialog dialog = builder.create();
		        dialog.show();
			}
			
		});
		

	}
	
	private boolean checkEmptyText(String value) {
		return !value.equalsIgnoreCase("");
	}
	
	private boolean compareStrings(String v1, String v2) {
		return v1.equalsIgnoreCase(v2);
	}
	
	private boolean checkSettingsExist(String id, String session, String study) {
		
		DBHelper db = new DBHelper(context);
		
		return db.participantSettingsExist(id, session, study);
	}
}
