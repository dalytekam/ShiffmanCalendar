package com.example.shiffmancalendar;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DataEntryDatePhase3 extends DataEntryDate {

	TextView title;
	EditText cigs;
	EditText otherNicCnt;
	Spinner otherNic1, otherNic2;
	EditText otherFree1, otherFree2;
	Button cancel;
	Button save;
	
	String[] otherNicSource = {"", "Non-research cigarette", "E-cigarette", "Smokeless Tobacco",
							   "Cigar", "Hookah", "Pipe", "Other"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_phase3);
		
		title = (TextView) findViewById(R.id.title);
		cigs = (EditText) findViewById(R.id.editText1);
		otherNicCnt = (EditText) findViewById(R.id.editText2);
		otherNic1 = (Spinner) findViewById(R.id.dropdown1);
		otherNic2 = (Spinner) findViewById(R.id.dropdown2);
		otherFree1 = (EditText) findViewById(R.id.otherText1);
		otherFree2 = (EditText) findViewById(R.id.otherText2);
		cancel = (Button) findViewById(R.id.cancel_button);
		save = (Button) findViewById(R.id.save_button);
		
		title.setText(title.getText() + "\n" + super.formattedDate);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, otherNicSource);
		otherNic1.setAdapter(adapter);
		otherNic2.setAdapter(adapter);
		
		if (super.existingData != null) {
			cigs.setText(super.existingData.getAsString(DBHelper.KEY_CIG_COUNT));
			otherNicCnt.setText(super.existingData.getAsString(DBHelper.KEY_OTHER_COUNT));
			int nicCnt = Integer.parseInt(super.existingData.getAsString(DBHelper.KEY_OTHER_COUNT));
			if (nicCnt > 0) {
				String src1 = super.existingData.getAsString(DBHelper.KEY_OTHER_TYPE_1);
				String src2 = super.existingData.getAsString(DBHelper.KEY_OTHER_TYPE_2);
				
				int src1_pos = getNicSourceIndex(src1);
				otherNic1.setSelection(src1_pos);
				int src2_pos = getNicSourceIndex(src2);
				otherNic2.setSelection(src2_pos);
				
				otherFree1.setText(super.existingData.getAsString(DBHelper.KEY_OTHER_FREE_1));
				otherFree2.setText(super.existingData.getAsString(DBHelper.KEY_OTHER_FREE_2));
			}
		}
		
		otherNic1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == otherNicSource.length - 1) {
					otherFree1.setEnabled(true);
				} else {
					otherFree1.setText("");
					otherFree1.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				otherFree1.setText("");
				otherFree1.setEnabled(false);
			}
			
		});
		
		otherNic2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == otherNicSource.length - 1) {
					otherFree2.setEnabled(true);
				} else {
					otherFree2.setText("");
					otherFree2.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				otherFree2.setText("");
				otherFree2.setEnabled(false);
			}
			
		});
		
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
				String other_count = otherNicCnt.getText().toString();
				String other_type1 = (String) otherNic1.getSelectedItem();
				String other_type2 = (String) otherNic2.getSelectedItem();
				String other_free1 = otherFree1.getText().toString();
				String other_free2 = otherFree2.getText().toString();
				
				if (!checkCount(cig_count, "Please enter the number of cigarettes smoked.")) return;
				if (!checkCount(other_count, "Please enter the number of other nicotine sources used.")) return;
				if (!checkSourceSelected(other_count, other_type1)) return;
				if (!checkOtherSelected(other_type1, other_free1)) return;
				if (!checkOtherSelected(other_type2, other_free2)) return;
				
				ContentValues values = initContentValues();
				values.put(DBHelper.KEY_DATE, date);
				values.put(DBHelper.KEY_CIG_COUNT, cig_count);
				values.put(DBHelper.KEY_OTHER_COUNT, other_count);
				values.put(DBHelper.KEY_OTHER_TYPE_1, other_type1);
				values.put(DBHelper.KEY_OTHER_FREE_1, other_free1);
				values.put(DBHelper.KEY_OTHER_TYPE_2, other_type2);
				values.put(DBHelper.KEY_OTHER_FREE_2, other_free2);
				
				saveOrUpdateEntry(values);
				finish();
			}
			
		});
	}
	
	private boolean checkCount(String value, String msg) {
		if (value.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean checkSourceSelected(String otherCnt, String otherType) {
		int cnt = Integer.parseInt(otherCnt);
		if (cnt > 0 && otherType.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), "Please select the other source(s) of nicotine used." , Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean checkOtherSelected(String otherType, String otherFree) {
		if (otherType.equalsIgnoreCase("Other") && otherFree.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), "Please enter the unlisted source(s) of nicotine used." , Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	

	private int getNicSourceIndex(String source) {
		int index = 0;
		
		for (int i=0; i<otherNicSource.length; i++) {
			if (source.equalsIgnoreCase(otherNicSource[i])) {
				index = i;
			}
		}
		
		return index;
	}
}
