package com.example.shiffmancalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SummarizeData extends Activity {

	TableLayout table;
	Button export;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		TableLayout table = new TableLayout(this);
//		table.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(R.layout.summarize_layout);
		table = (TableLayout) findViewById(R.id.summary_table);
		export = (Button) findViewById(R.id.summary_button);
		
		export.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DataExport.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
			}
			
		});
		SharedPreferences prefs = getSharedPreferences("shiffman_calendar", 0);
		String id = prefs.getString("id", "unknown");
		String session = prefs.getString("session", "unknown");
		
		DBHelper db = new DBHelper(this);
		List<ContentValues> data = db.getAllEntries(id, session);
		List<String> columns = db.getColumns(0);
		
		TableRow columnRow = new TableRow(this);
		columnRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		for (String columnName : columns) {
			TextView cell = new TextView(this);
			cell.setText(columnName);
			cell.setTextSize(20);
			cell.setTextColor(Color.BLUE);
			cell.setGravity(Gravity.CENTER);
			columnRow.addView(cell);
		}
		table.addView(columnRow);
		final View vline = new View(this);
        vline.setLayoutParams(new       
        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        vline.setBackgroundColor(Color.BLUE);
        table.addView(vline); // add line below heading
        
        for (ContentValues value : data) {
        	TableRow dataRow = new TableRow(this);
    		dataRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    		
    		for (String columnName : columns) {
    			TextView cell = new TextView(this);
    			if (columnName.equalsIgnoreCase("date")) {
    				long date = value.getAsLong(columnName);
    				Calendar cal = Calendar.getInstance();
    				cal.setTimeInMillis(date);
    				SimpleDateFormat format1 = new SimpleDateFormat("d MMM yyyy", Locale.US);
    				cell.setText(format1.format(cal.getTime()));
    			} else {
    				cell.setText(value.getAsString(columnName));
    			}
    			cell.setTextSize(15);
    			cell.setTextColor(Color.BLACK);
    			cell.setGravity(Gravity.CENTER);
    			dataRow.addView(cell);
    		}
    		table.addView(dataRow);
    		final View vline1 = new View(this);
            vline1.setLayoutParams(new       
            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            vline1.setBackgroundColor(Color.BLACK);
            table.addView(vline1); // add line below heading
        }
	}

}
