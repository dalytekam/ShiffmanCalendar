package com.example.shiffmancalendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DataExport extends Activity {

	Spinner dateSpinner;
	Button export;
	TextView text;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.exportdata_layout);
		text = (TextView) findViewById(R.id.textView1);
		dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
		export = (Button) findViewById(R.id.exportButton);
		
		List<String> dates = generateListOfDates();
		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, dates);
		dateSpinner.setAdapter(dateAdapter);
		
		export.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String selectedDate = (String) dateSpinner.getSelectedItem();
				if (selectedDate.equalsIgnoreCase("")) {
					Toast.makeText(context, "Please select a start date", Toast.LENGTH_SHORT).show();
					return;
				}

				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
				Date parsedDate = null;
				try {
					parsedDate = format1.parse(selectedDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(context, "Date parse failed", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Time t = new Time();
				t.set(parsedDate.getTime());
				System.out.println("Date in millis: " + t.toMillis(false));
				
				SharedPreferences prefs = context.getSharedPreferences("shiffman_calendar", 0);
				String id = prefs.getString("id", "unknown");
				String session = prefs.getString("session", "unknown");
				long start = prefs.getLong("start", System.currentTimeMillis());
				long end = prefs.getLong("end", System.currentTimeMillis());
				int phase = prefs.getInt("phase", -1);
				
				printToScreen(printMetaData(id, start, end, phase));
				
				printToScreen("Loading data...");
				DBHelper db = new DBHelper(context);
				List<ContentValues> data = db.getAllEntries(null, null, t);
				List<String> columns = db.getColumns(0);
				
				printToScreen("Generating CSV file...");
				
				printToScreen("Building text...");
				String csvText = generateCSVText(data, columns);
				
				printToScreen("Writing to file...");
				String mostRecent = (String) dateSpinner.getItemAtPosition(1);
				File output = generateFile(selectedDate, mostRecent);
				String filename = output.getName();
				String success = writeToCSV(csvText, output);
				
				if (success.equalsIgnoreCase("success")) {
					printToScreen("SUCCESS: " + filename);
				} else {
					printToScreen("ERROR: " + filename + ", please try again.");
				}
				
				showDialogBox(success, output.getAbsolutePath());
				
			}
			
		});
	}

	private List<String> generateListOfDates() {
		DBHelper db = new DBHelper(context);
		List<ContentValues> data = db.getAllEntries(null, null, null);
		Set<String> dates = new HashSet<String>();
		
		for (ContentValues d : data) {
			long entered = d.getAsLong(DBHelper.KEY_DATE_ENTERED);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(entered);
			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			dates.add(format1.format(cal.getTime()));
		}
		List<String> dateList = new ArrayList<String>();
		for (String date : dates) {
			dateList.add(date);
		}
		Collections.sort(dateList);
		Collections.reverse(dateList);
		dateList.add(0, "");
		
		return dateList;
	}

	private void showDialogBox(String success, String filename) {
		String title;
		String msg;
		if (success.equalsIgnoreCase("success")) {
			title = "Data Export: SUCCESS";
			msg = "Saved data to: " + filename;
		} else {
			title = "Data Export: FAILURE!!!";
			msg = success;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
	    				finish();
                   }
               })
               .setCancelable(false);

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
	}

	private String writeToCSV(String csvText, File output) {
		String success = "success";
		FileOutputStream outputStream;


		  try {
			outputStream = new FileOutputStream(output);
			outputStream.write(csvText.getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = "Couldn't create the file. Check that the tablet is not plugged into a computer.";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = "Failed while writing file, Please try again.";
		}
		  

		return success;
	}

	private File generateFile(String startDate, String mostRecentDate) {
		
		startDate = startDate.replaceAll("/", "-");
		mostRecentDate = mostRecentDate.replaceAll("/", "-");
		
		SharedPreferences prefs = context.getSharedPreferences("shiffman_calendar", 0);
		String deviceid = prefs.getString("deviceid", "unknownTablet");
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy_h-m-s");
		String formattedDate = df.format(c.getTime());
		String filename = "SHIFFMANCAL_" + deviceid + "_" + startDate + "_" + mostRecentDate + "_exported_"+ formattedDate + ".csv";
		File dir = new File(Environment.getExternalStorageDirectory(), "SHIFFMANCAL");
		dir.mkdir();
		return new File(dir, filename);
	}

	private String generateCSVText(List<ContentValues> data, List<String> columns) {
		StringBuilder sb = new StringBuilder();
		
		// add header
		for(String col: columns) {
			sb.append(col).append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("\n");
		
		// add data
		for (ContentValues values: data) {
			for (String col: columns) {
				if (col.equalsIgnoreCase("date") || col.equalsIgnoreCase("date_entered")) {
    				long date = values.getAsLong(col);
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(date);
    				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    				sb.append(format1.format(cal.getTime()));
    			} else {
    				sb.append(values.getAsString(col));
    			}
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append("\n");
		}
		
		return sb.toString();
	}

	private String printMetaData(String id, long start, long end, int phase) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Participant: ").append(id).append("\n");
		
		String phaseStr;
		if (phase == -1) {
			phaseStr = "Phase: Unknown?";
		} else {
			phase += 1;
			phaseStr = "Phase: " + phase;
		}
		sb.append(phaseStr).append("\n");
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(start);
		SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy", Locale.US);
		String startStr = format1.format(cal.getTime());
		cal.setTimeInMillis(end);
		String endStr = format1.format(cal.getTime());
		sb.append(startStr).append(" to ").append(endStr).append("\n\n");
		
		return sb.toString();
	}
	
	private void printToScreen(String str) {
		String curr = text.getText().toString();
		
		if (curr.equalsIgnoreCase("")) {
			curr = str;
		} else {
			curr += "\n" + str;
		}
		text.setText(curr);
		
	}
}
