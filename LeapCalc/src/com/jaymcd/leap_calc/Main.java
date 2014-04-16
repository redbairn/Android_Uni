/* Leap Calculator App
 * This is the main screen it incorporates the DB class (which is used to access the SQLite database)
 * The main screen displays the current balance (as read from DB) and also contains two buttons for adding a trip or updating the balance
 * There are settings options too that are access by pressing the menu button
 */

package com.jaymcd.leap_calc;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

	TextView balance, curBal;
	Button updateBalance, addTrip;
	NotificationManager x;
	Boolean Gaeilge, notify, logged;
	SharedPreferences getprefs;
	String user;
	static final int unID = 2932553;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

		/*
		 * This code is used for the exit option from the menu items, the exit
		 * option opens this activity and sends an extra called EXIT
		 */
		if (getIntent().getBooleanExtra("EXIT", false)) { // if EXIT is true
			System.exit(0); // exit the app
			finish();
		}
		setScreen();
		setContentView(R.layout.main);
		
		balance = (TextView) findViewById(R.id.txtBalance);
		updateBalance = (Button) findViewById(R.id.btnUpdateBalance);
		addTrip = (Button) findViewById(R.id.btnAddTrip);
		curBal = (TextView) findViewById(R.id.mCBal);
		/* Check if Irish is selected */
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Gaeilge = getprefs.getBoolean("irish", false);
		notify = getprefs.getBoolean("notify", false);
		logged = getprefs.getBoolean("logged", false);
		user = getprefs.getString("user", null);

		
		if (Gaeilge) {
			curBal.setText(R.string.Gbal);
			addTrip.setText(R.string.GaddT);
			updateBalance.setText(R.string.GupBal);
		}

		updateBalance.setOnClickListener(new updateBalanceListener());
		addTrip.setOnClickListener(new addTripListener());

		// calls the methods defined below
		findBalance();

	}

	class updateBalanceListener implements View.OnClickListener {
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), UpdateBalance.class);
			startActivity(i);
			finish();
		}
	}

	public void findBalance() {

		/* This method uses the DB class it calls the getBalance method */
		int rtnBalance;
		DB data = new DB(this); // defines an instance of DB
		data.open();
		rtnBalance = data.getBalance(); // sets rtnBalance to the returned value
										// from the method in DB
		data.close(); // closes the DB
		
		formatBalance(rtnBalance); // calls a method defined below

	}

	public void formatBalance(int bal) {
		/*
		 * Sets up a decimal format for the value of bal As the database can
		 * only store ints we cannot use decimal values When storing values in
		 * DB, but we do need them to display as decimals
		 */
		double calc = Double.valueOf(bal);
		DecimalFormat dec = new DecimalFormat("###.00"); // sets the decimal
															// format
		double rawPercent = ((double) (calc) / 100.00); // divides by 100 as
														// the integers in
														// the database are
														// the cent values
														// not euro
		// displays the balance in percent format and adds a € or -€ when
		// needed
		if (rawPercent < 0 && rawPercent > -10) {
			int minusnum;
			minusnum = Math.abs(-bal);// turns the negative number to
										// positive

			double minuscalc = Double.valueOf(minusnum);
			double minusPercent = ((double) (minuscalc) / 100.00);
			balance.setText("- €0" + dec.format(minusPercent));
		} else if (rawPercent < 0 && rawPercent > -100) {
			int minusnum;
			minusnum = Math.abs(-bal);// turns the negative number to
										// positive

			double minuscalc = Double.valueOf(minusnum);
			double minusPercent = ((double) (minuscalc) / 100.00);
			balance.setText("- €" + dec.format(minusPercent));
		} else if (rawPercent >= 0 && rawPercent < 10) {

			balance.setText("€0" + dec.format(rawPercent));
		} else {
			balance.setText("€" + dec.format(rawPercent));

		}
		if (bal < 50 && notify) {
			balNotify();

		}
	}

	class addTripListener implements View.OnClickListener {
		public void onClick(View v) {
			// moves on to the Add trip screen
			Intent i = new Intent(getApplicationContext(), AddTrip.class);
			startActivityForResult(i, 10);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		/* Set irish menu visible */
		menu.findItem(R.id.settings).setVisible(!Gaeilge);
		menu.findItem(R.id.exit).setVisible(!Gaeilge);
		menu.findItem(R.id.sync).setVisible(!Gaeilge);
		menu.findItem(R.id.Gsync).setVisible(Gaeilge);
		menu.findItem(R.id.Gsettings).setVisible(Gaeilge);
		menu.findItem(R.id.Gexit).setVisible(Gaeilge);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Checks which option from the menu is selected then navigates
		 * appropriately
		 */
		Intent i;
		switch (item.getItemId()) {
		case R.id.sync:
			if (logged) {
				i = new Intent(this, Sync.class);
				i.putExtra("user", user);
				startActivity(i);
				finish();
				return true;
			} else {
				i = new Intent(this, Login.class);
				startActivity(i);
				finish();
				return true;
			}

		case R.id.Gsync:
			if (logged) {
				i = new Intent(this, Sync.class);
				i.putExtra("user", user);
				startActivity(i);
				finish();
				return true;
			} else {
				i = new Intent(this, Login.class);
				startActivity(i);
				finish();
				return true;
			}
		case R.id.settings:
			i = new Intent(this, prefs.class);
			startActivity(i);
			finish();
			return true;
		case R.id.Gsettings:
			i = new Intent(this, prefs.class);
			startActivity(i);
			finish();
			return true;
		case R.id.exit:
			i = new Intent(this, Main.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("EXIT", true);
			startActivity(i);
			finish();
			return true;
		case R.id.Gexit:
			i = new Intent(this, Main.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("EXIT", true);
			startActivity(i);
			finish();
			return true;
		}
		return true;

	}

	/* Notifies user if balance is too low */
	@SuppressWarnings("deprecation")
	public void balNotify() {

		x = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent i = new Intent(this, UpdateBalance.class);
		PendingIntent y = PendingIntent.getActivity(this, 0, i, 0);
		String note = "Leap card balance too low. Please top up";
		if (Gaeilge){ //sets notification to irish if needed
			note ="Cothromaíocht cárta Leap ró-íseal. Cuir barr suas";	
		}
		String title = "Leap Calculator";
		Notification n = new Notification(R.drawable.not_icon, note,
				System.currentTimeMillis());
		n.setLatestEventInfo(this, title, note, y);
		n.defaults = Notification.DEFAULT_VIBRATE;

		x.notify(unID, n);

	}

	@Override
	public void onBackPressed() { // overides back button

		finish();
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

}
