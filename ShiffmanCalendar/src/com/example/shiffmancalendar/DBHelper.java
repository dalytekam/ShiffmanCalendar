package com.example.shiffmancalendar;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "shiffmanCalendar";
	private static final int DB_VERSION = 1;
	
	private static final String TABLE_NAME = "calendarData";
	
	public static final String KEY_ID = "id";
	public static final String KEY_DATE_ENTERED = "date_entered";
	public static final String KEY_PID = "pid";
	public static final String KEY_STUDY = "study";
	public static final String KEY_SESSION = "session";
	public static final String KEY_DATE = "date";
	public static final String KEY_CIG_COUNT = "cig_count";
	public static final String KEY_NRES_CIG_COUNT = "nres_cig_count";
	public static final String KEY_GUM_COUNT = "gum_count";
	public static final String KEY_OTHER_COUNT = "other_count";
	public static final String KEY_OTHER_TYPE_1 = "other_type_1";
	public static final String KEY_OTHER_FREE_1 = "other_free_1";
	public static final String KEY_OTHER_TYPE_2 = "other_type_2";
	public static final String KEY_OTHER_FREE_2 = "other_free_2";
	
//	private static final String CREATE_TABLE_PHASE_1 = "CREATE TABLE " + TABLE_NAME + "("
//            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PID + " TEXT, " + KEY_STUDY + " TEXT, " + KEY_SESSION + " TEXT, "
//			+ KEY_DATE + " INTEGER," + KEY_CIG_COUNT + " INTEGER" + ")";
//	private static final String CREATE_TABLE_PHASE_2 = "CREATE TABLE " + TABLE_NAME + "("
//            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PID + " TEXT, " + KEY_STUDY + " TEXT, " + KEY_SESSION + " TEXT, "
//			+ KEY_DATE + " INTEGER," + KEY_CIG_COUNT 
//            + " INTEGER, " + KEY_GUM_COUNT + " INTEGER" + ")";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE_ENTERED + " INTEGER, "
			+ KEY_PID + " TEXT, " + KEY_STUDY + " TEXT, " + KEY_SESSION + " TEXT, "
			+ KEY_DATE + " INTEGER," + KEY_CIG_COUNT + " TEXT, " + KEY_NRES_CIG_COUNT + " TEXT, " 
			+ KEY_GUM_COUNT + " TEXT, " + KEY_OTHER_COUNT + " TEXT, "
            + KEY_OTHER_TYPE_1 + " TEXT, " + KEY_OTHER_FREE_1 + " TEXT, "
            + KEY_OTHER_TYPE_2 + " TEXT, " + KEY_OTHER_FREE_2 + " TEXT" + ")";
	
	
	SharedPreferences prefs;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
		prefs = context.getSharedPreferences("shiffman_calendar", 0);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	// CRUD OPERATIONS BELOW
	
	public ContentValues entryExists(String pid, String session, long date) {
		ContentValues exists = null;
		
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_PID + "=? AND " + KEY_SESSION + "=? AND " 
							+ KEY_DATE + "= ?";
        Cursor cursor = db.rawQuery(countQuery, new String[] { pid, session, Long.toString(date) });
        
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
	
	public boolean participantSettingsExist(String pid, String session, String study) {
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_PID + "=? AND " + KEY_SESSION + "=? AND " 
							+ KEY_STUDY + "= ?";
        Cursor cursor = db.rawQuery(countQuery, new String[] { pid, session, study });
        
        return cursor.getCount() > 0;
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
	
	public int applyIDAndSession(String pid, String session) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PID, pid);
		values.put(KEY_SESSION, session);
		
		return db.update(TABLE_NAME, values, KEY_PID + " =?", new String[] {"default"});
	}
	
	public int removeDefaultIDRows() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		return db.delete(TABLE_NAME, KEY_PID + " =?", new String[] {"default"});
	}
	
	public List<ContentValues> getAllEntries(String pid, String session, Time startDate) {
		List<ContentValues> entries = new ArrayList<ContentValues>();
		
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	    String[] vals = null;
	    if (pid != null && session != null) {
	    	selectQuery += " WHERE " + KEY_PID + "=? AND " + KEY_SESSION + "=?";
	    	vals = new String[2];
	    	vals[0] = pid;
	    	vals[1] = session;
	    } else if (pid != null) {
	    	selectQuery += " WHERE " + KEY_PID + "=?";
	    	vals = new String[1];
	    	vals[0] = pid;
	    } else if (startDate != null) {
	    	selectQuery += " WHERE " + KEY_DATE_ENTERED + ">=?";
	    	vals = new String[1];
	    	vals[0] = Long.toString(startDate.toMillis(false));
	    }
	    selectQuery +=  " ORDER BY " + KEY_DATE;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, vals);
	 
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

	public List<String> getColumns(int phase) {
		List<String> columns = new ArrayList<String>();
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
        	int colCount = cursor.getColumnCount();
        	// skip column 0 cause thats just the id of the row
        	for (int i=1; i<colCount; i++) {
        		String key = cursor.getColumnName(i);
        		columns.add(key);
        	}
	    }
	    db.close();
	    
	    return columns;
	}

}
