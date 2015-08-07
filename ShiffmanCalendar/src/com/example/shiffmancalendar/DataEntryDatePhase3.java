package com.example.shiffmancalendar;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DataEntryDatePhase3 extends DataEntryDate {

	TextView title, otherText1, otherText2, otherText3, otherText4;
	EditText cigs, nresCigs;
	EditText otherNicCnt;
	Spinner otherNic1, otherNic2;
	EditText otherFree1, otherFree2;
	Button cancel;
	Button save;
	ScrollView scroll;
	SharedPreferences prefs;
	
	String[] otherNicSource = {"", "E-cigarette", "Smokeless Tobacco",
							   "Cigar", "Hookah", "Pipe", "Other"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_phase3);
		
		title = (TextView) findViewById(R.id.title);
		cigs = (EditText) findViewById(R.id.p3_editText1);
		nresCigs = (EditText) findViewById(R.id.p3_editText7);
		otherNicCnt = (EditText) findViewById(R.id.p3_editText2);
		otherNic1 = (Spinner) findViewById(R.id.dropdown1);
		otherNic2 = (Spinner) findViewById(R.id.dropdown2);
		otherFree1 = (EditText) findViewById(R.id.otherText1);
		otherFree2 = (EditText) findViewById(R.id.otherText2);
		cancel = (Button) findViewById(R.id.p3_cancel_button);
		save = (Button) findViewById(R.id.p3_save_button);
		otherText1 = (TextView) findViewById(R.id.field3Text);
		otherText1.setTextColor(Color.LTGRAY);
		otherText2 = (TextView) findViewById(R.id.field4Text);
		otherText2.setTextColor(Color.LTGRAY);
		otherText3 = (TextView) findViewById(R.id.field5Text);
		otherText3.setTextColor(Color.LTGRAY);
		otherText4 = (TextView) findViewById(R.id.field6Text);
		otherText4.setTextColor(Color.LTGRAY);
		scroll = (ScrollView) findViewById(R.id.scrollView1);
		
		title.setText(title.getText() + "\n" + super.formattedDate);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, otherNicSource);
		otherNic1.setAdapter(adapter);
		otherNic2.setAdapter(adapter);
		
		prefs = getSharedPreferences("shiffman_calendar", 0);
		int study = prefs.getInt("phase", 3);
		if (study == 2) { // LONIC Visit 1
			TableRow row = (TableRow) findViewById(R.id.tableRow2);
			TextView cigLabel = (TextView) findViewById(R.id.field7Text);
			row.setVisibility(View.GONE);
			row.setEnabled(false);
			cigLabel.setText("Number of cigarettes smoked: ");
			cigs.setText("0");
		}
		
		if (super.existingData != null) {
			cigs.append(super.existingData.getAsString(DBHelper.KEY_CIG_COUNT));
			nresCigs.append(super.existingData.getAsString(DBHelper.KEY_NRES_CIG_COUNT));
			otherNicCnt.append(super.existingData.getAsString(DBHelper.KEY_OTHER_COUNT));
			int nicCnt = Integer.parseInt(super.existingData.getAsString(DBHelper.KEY_OTHER_COUNT));
			if (nicCnt > 0) {
				String src1 = super.existingData.getAsString(DBHelper.KEY_OTHER_TYPE_1);
				String src2 = super.existingData.getAsString(DBHelper.KEY_OTHER_TYPE_2);
				
				int src1_pos = getNicSourceIndex(src1);
				otherNic1.setSelection(src1_pos);
				int src2_pos = getNicSourceIndex(src2);
				otherNic2.setSelection(src2_pos);
				
				otherFree1.append(super.existingData.getAsString(DBHelper.KEY_OTHER_FREE_1));
				otherFree2.append(super.existingData.getAsString(DBHelper.KEY_OTHER_FREE_2));
				
				otherText1.setTextColor(Color.BLACK);
				otherText2.setTextColor(Color.BLACK);
				otherText3.setTextColor(Color.BLACK);
				otherText4.setTextColor(Color.BLACK);
				
				otherNic1.setClickable(true);
				otherNic2.setClickable(true);
				
				if (!otherFree1.getText().toString().equalsIgnoreCase("")) {
					otherFree1.setEnabled(true);
				}
				if (!otherFree2.getText().toString().equalsIgnoreCase("")) {
					otherFree2.setEnabled(true);
				}
			}
		}
		
		nresCigs.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					scroll.smoothScrollTo(0, nresCigs.getBottom());
					//scroll.fullScroll(ScrollView.FOCUS_UP);
				}
			}
			
		});
		
		otherNicCnt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					scroll.smoothScrollTo(0, otherNicCnt.getBottom());
					//scroll.fullScroll(ScrollView.FOCUS_UP);
				}
			}
			
		});
		
		
		otherNicCnt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!s.toString().equalsIgnoreCase("") && !s.toString().endsWith(".") && Integer.parseInt(s.toString()) > 0) {
					otherText1.setTextColor(Color.BLACK);
					otherText3.setTextColor(Color.BLACK);
					otherNic1.setClickable(true);
					otherNic2.setClickable(true);
				} else {
					otherText1.setTextColor(Color.LTGRAY);
					otherText3.setTextColor(Color.LTGRAY);
					otherNic1.setSelection(0);
					otherNic1.setClickable(false);
					otherNic2.setSelection(0);
					otherNic2.setClickable(false);
					
					otherText2.setTextColor(Color.LTGRAY);
					otherText4.setTextColor(Color.LTGRAY);
					otherFree1.setText("");
					otherFree1.setEnabled(false);
					otherFree2.setText("");
					otherFree2.setEnabled(false);
				}
				
			}
			
		});
		
		otherNic1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == otherNicSource.length - 1) {
					otherText2.setTextColor(Color.BLACK);
					otherFree1.setEnabled(true);
				} else {
					otherText2.setTextColor(Color.LTGRAY);
					otherFree1.setText("");
					otherFree1.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				otherText2.setTextColor(Color.LTGRAY);
				otherFree1.setText("");
				otherFree1.setEnabled(false);
			}
			
		});
		
		otherNic2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position == otherNicSource.length - 1) {
					otherText4.setTextColor(Color.BLACK);
					otherFree2.setEnabled(true);
				} else {
					otherText4.setTextColor(Color.LTGRAY);
					otherFree2.setText("");
					otherFree2.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				otherText4.setTextColor(Color.LTGRAY);
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
				String nres_cig_count = nresCigs.getText().toString();
				String other_count = otherNicCnt.getText().toString();
				String other_type1 = (String) otherNic1.getSelectedItem();
				String other_type2 = (String) otherNic2.getSelectedItem();
				String other_free1 = otherFree1.getText().toString();
				String other_free2 = otherFree2.getText().toString();
				if (prefs.getInt("phase", 3) == 2) {
					if (!checkCount(cig_count, "Please enter the number of cigarettes smoked.")) return;
				} else {
					if (!checkCount(cig_count, "Please enter the number of research cigarettes smoked.")) return;
					if (!checkCount(nres_cig_count, "Please enter the number of non-research cigarettes smoked.")) return;
				}
				if (!checkCount(other_count, "Please enter the number of other nicotine sources used.")) return;
				if (!checkSourceSelected(other_count, other_type1)) return;
				if (!checkOtherSelected(other_type1, other_free1)) return;
				if (!checkOtherSelected(other_type2, other_free2)) return;
				
				ContentValues values = initContentValues();
				values.put(DBHelper.KEY_DATE, date);
				values.put(DBHelper.KEY_CIG_COUNT, cig_count);
				values.put(DBHelper.KEY_NRES_CIG_COUNT, nres_cig_count);
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
