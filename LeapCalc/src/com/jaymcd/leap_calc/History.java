package com.jaymcd.leap_calc;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class History extends Activity {

	private ArrayList<String> info = new ArrayList<String>();
	ListView LView;
	Boolean Gaeilge;
	SharedPreferences getprefs;
	TextView hisLbl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreen();
		setContentView(R.layout.history);
		
		
		/* Check if Irish is selected */
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Gaeilge = getprefs.getBoolean("irish", false);

		DB data = new DB(this);
		data.open();
		info = data.getHistory();
		data.close();
		LView = (ListView) findViewById(R.id.itemList);
		String[] display = info.toArray(new String[info.size()]);
		String bal;
		bal = "Balance:  ";
		hisLbl = (TextView) findViewById(R.id.txtLCHistory);
		if (Gaeilge) {
			bal = "Iarmhéid:  ";
			hisLbl.setText(R.string.GLCH);

		}

		int x, y;
		y = display.length;

		for (x = 0; x < y; x++) {

			display = info.get(x).split(" ");
			String val = display[1];
			int num = Integer.parseInt(val);
			String value = formatBalance(num);
			info.set(x, bal + value + display[2]);
		}

		final ArrayAdapter<String> adpt = new ArrayAdapter<String>(this,
				R.layout.simplelist, info);
		LView.setAdapter(adpt);
		LView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}
	
	public void setScreen() {
		/*
		 * checks the screen size in configuration to determine screen size
		 * (will be removed for landscape option of assignment)
		 */

		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			/* Hides app title bar */
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			/* Hides app title bar */
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			/* Hides app title bar */
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			/* Hides app title bar */
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		}
	}


	public String formatBalance(int bal) {
		String cost;
		double calc = Double.valueOf(bal);
		DecimalFormat dec = new DecimalFormat("###.00");
		double rawPercent = ((double) (calc) / 100.00);
		// sets the variable "balance" to the same as rtnBalance (so it
		// can be used in different methods
		if (rawPercent < 0 && rawPercent > -10) {
			int minusnum;
			minusnum = Math.abs(-bal);
			double minuscalc = Double.valueOf(minusnum);
			// formats the balance to a decimal (it is stored as an int
			// in the table)
			double minusPercent = ((double) (minuscalc) / 100.00);

			// sets the text of the edit text to the current balance
			cost = "-€0" + dec.format(minusPercent);
			// if the balance is greater than 0 and less than 100 a 0 is
			// places before the balance value
			// if not the balance is set
		} else if (bal < 0 && bal > -100) {
			int minusnum;
			minusnum = Math.abs(-bal);// turns the negative number to
										// positive

			double minuscalc = Double.valueOf(minusnum);
			double minusPercent = ((double) (minuscalc) / 100.00);

			cost = "-€" + dec.format(minusPercent);
		} else if (bal >= 0 && bal <10) {
			cost = "€0" + dec.format(rawPercent);
		}else {
			cost = "€" + dec.format(rawPercent);

		}
		return cost;
	}

	@Override
	public void onBackPressed() {
		/*
		 * Overides the default back button action, moves back to main
		 */
		Intent i = new Intent(this, Main.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);

		/* Set irish menu visible */
		menu.findItem(R.id.back).setVisible(!Gaeilge);
		menu.findItem(R.id.CHistory).setVisible(!Gaeilge);
		menu.findItem(R.id.Gback).setVisible(Gaeilge);
		menu.findItem(R.id.GCHistory).setVisible(Gaeilge);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Adds a back option under the menu for History, as they are
		 * automatically saved, just moves to main
		 */
		Intent i;
		switch (item.getItemId()) {
		case R.id.back:
			i = new Intent(this, Main.class);
			startActivity(i);
			finish();
			return true;
		case R.id.Gback:
			i = new Intent(this, Main.class);
			startActivity(i);
			finish();
			return true;
		case R.id.CHistory: // Clear History
			/* Code below creates an alter dialogue to confirm delete */
			AlertDialog.Builder confirmDialog = new AlertDialog.Builder(
					History.this);
			confirmDialog.setTitle("Confirm");

			// set dialog message
			confirmDialog
					.setMessage("Are you sure you want to delete?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() { // if yes
								/*
								 * If user chooses yes, record is deleted from
								 * database
								 */
								public void onClick(DialogInterface dialog,
										int id) {
									Intent i;
									DB data = new DB(getApplicationContext()); // defines
																				// an
																				// instance
																				// of
																				// DB
									data.open();
									data.delRecords(); // deletes all entries in
														// DB
									data.close(); // closes the DB
									i = new Intent(getApplicationContext(),
											Main.class);
									startActivity(i);
									finish();
									
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() { // if no
								public void onClick(DialogInterface dialog,
										int id) {
									// closes dialog and does nothing
									dialog.cancel();
									
								}
							});

			// create alert dialog
			AlertDialog alertDialog = confirmDialog.create();

			// show it
			alertDialog.show();
			return true;
		case R.id.GCHistory: // Clear History

			/* Code below creates an alter dialogue to confirm delete */
			AlertDialog.Builder GconfirmDialog = new AlertDialog.Builder(
					History.this);
			GconfirmDialog.setTitle("Deimhnigh");

			// set dialog message
			GconfirmDialog
					.setMessage(
							"An bhfuil tú cinnte gur mian leat a scriosadh?")
					.setCancelable(false)
					.setPositiveButton("Is ea",
							new DialogInterface.OnClickListener() { // if yes
								/*
								 * If user chooses yes, record is deleted from
								 * database
								 */
								public void onClick(DialogInterface dialog,
										int id) {
									Intent i;
									DB data = new DB(getApplicationContext()); // defines
																				// an
																				// instance
																				// of
																				// DB
									data.open();
									data.delRecords(); // deletes all entries in
														// DB
									data.close(); // closes the DB
									i = new Intent(getApplicationContext(),
											Main.class);
									startActivity(i);
									finish();

								}
							})
					.setNegativeButton("Níl",
							new DialogInterface.OnClickListener() { // if no
								public void onClick(DialogInterface dialog,
										int id) {
									// closes dialog and does nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog GalertDialog = GconfirmDialog.create();

			// show it
			GalertDialog.show();
			return true;

		}
		return true;
	}
}
