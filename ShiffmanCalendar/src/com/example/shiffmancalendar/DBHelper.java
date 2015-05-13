package com.example.shiffmancalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "shiffmanCalendar";
	private static final int DB_VERSION = 1;
	
	private static final String TABLE_NAME = "calendarData";
	
	public static final String KEY_ID = "id";
	public static final String KEY_DATE = "date";
	public static final String KEY_CIG_COUNT = "cig_count";
	public static final String KEY_OTHER_NICOTINE = "other_nicotine";
	
	private static final String CREATE_TABLE_PHASE_1 = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " INTEGER," + KEY_CIG_COUNT + " INTEGER" + ")";
	private static final String CREATE_TABLE_PHASE_2 = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " INTEGER," + KEY_CIG_COUNT 
            + " INTEGER," + KEY_OTHER_NICOTINE + " INTEGER" + ")";
	private static final String CREATE_TABLE_PHASE_3 = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " INTEGER," + KEY_CIG_COUNT + " INTEGER" + ")";
	
	SharedPreferences prefs;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
		prefs = context.getSharedPreferences("shiffman_calendar", 0);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		int phase = prefs.getInt("phase", 0);
		switch (phase) {
		case 0:
			db.execSQL(CREATE_TABLE_PHASE_1);
			break;
		case 1:
			db.execSQL(CREATE_TABLE_PHASE_2);
			break;
		case 2:
			db.execSQL(CREATE_TABLE_PHASE_3);
			break;
		default:
			db.execSQL(CREATE_TABLE_PHASE_1);
			break;
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	// CRUD OPERATIONS BELOW
	
	public ContentValues entryExists(long date) {
		ContentValues exists = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_DATE + "= ?";
        Cursor cursor = db.rawQuery(countQuery, new String[] { Long.toString(date) });
        
        if (cursor != null && cursor.getCount() != 0) {
        	cursor.moveToFirst();
        	int cols = cursor.getColumnCount();
        	exists = new ContentValues();
        	for (int i=0; i<cols; i++) {
        		exists.put(cursor.getColumnName(i), cursor.getString(i));
        	}
        }
        db.close();
		return exists;
	}
	
	public void addEntry(ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    // Inserting Row
	    db.insert(TABLE_NAME, null, values);
	    db.close(); // Closing database connection
	}
	
	public int updateEntry(ContentValues values, String rowId) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    // updating row
	    return db.update(TABLE_NAME, values, KEY_ID + " = ?",
	            new String[] { rowId });
	}
	
	public List<ContentValues> getAllEntries() {
		List<ContentValues> entries = new ArrayList<ContentValues>();
		
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	ContentValues entry = new ContentValues();
	        	int colCount = cursor.getColumnCount();
	        	// skip column 0 cause thats just the id of the row
	        	for (int i=1; i<colCount; i++) {
	        		String key = cursor.getColumnName(i);
	        		String value = cursor.getString(i);
	        		entry.put(key, value);
	        	}
	            entries.add(entry);
	        } while (cursor.moveToNext());
	    }
	    db.close();
		
		return entries;
	}

}
