package com.jaymcd.leap_calc;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

	public static final String ID = "id"; // each record will have a unique ID
											// to allow deletion and finding
	public static final String BAL = "balance";
	public static final String TIME = "dateTime";
	public static final String DATABASE_NAME = "LeapDB";
	public static final String TABLE_NAME = "BalanceTable";

	public static final int DB_VERS = 1;

	private Helper dbHelp;
	private final Context context;
	private SQLiteDatabase database;

	public static class Helper extends SQLiteOpenHelper {

		public Helper(Context context) {
			super(context, DATABASE_NAME, null, DB_VERS);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + BAL
					+ " INTEGER, " + TIME + " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	public DB(Context context) { // the constructor for this class
		this.context = context;
	}

	public DB open() throws SQLException {
		dbHelp = new Helper(context);
		database = dbHelp.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelp.close();
	}

	public long updateBal(int bal, String when) {
		ContentValues val = new ContentValues();
		val.put(BAL, bal);
		val.put(TIME, when);
		return database.insert(TABLE_NAME, null, val);
	}

	public int getBalance() {

		String[] cols = { ID, BAL };

		Cursor c = database.query(TABLE_NAME, cols, null, null, null, null,
				null);

		int ibal = c.getColumnIndex(BAL);
		int result = 0;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getInt(ibal);
		}

		c.close();
		return result;
	}

	public ArrayList<String> getHistory() {
		String[] cols = new String[] { ID, BAL, TIME };

		Cursor c = database.query(TABLE_NAME, cols, null, null, null, null,
				null);
		ArrayList<String> result = new ArrayList<String>();
		int id = c.getColumnIndex(ID);
		int ibal = c.getColumnIndex(BAL);
		int itime = c.getColumnIndex(TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result.add(c.getString(id) + " " + c.getString(ibal) + " "
					+ c.getString(itime));
		}
		;

		return result;
	}

	public void delRecords() {
		String[] cols = new String[] { ID, BAL, TIME };

		Cursor c = database.query(TABLE_NAME, cols, null, null, null, null,
				null);
		int id = c.getColumnIndex(ID);
		int x = 0;
		/* Code below gets most recent entry, current balance will not display if all entries are deleted */
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			x = c.getInt(id);
		}

		/* Deletes all entries before current*/
		database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + ID + " < "
				+ x + ";");

	}
}
