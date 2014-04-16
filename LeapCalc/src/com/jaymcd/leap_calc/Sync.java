package com.jaymcd.leap_calc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Sync extends Activity {

	SharedPreferences.Editor editor;
	SharedPreferences getprefs;
	Boolean Gaeilge, logged;
	Server s;
	DB d;
	String u, bal;
	TextView logout, instruct;
	Button syncD, syncS;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setScreen();
		setContentView(R.layout.sync);
		
		
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		editor = getprefs.edit();
		Gaeilge = getprefs.getBoolean("irish", false);
		logged = getprefs.getBoolean("logged", false);
		s = new Server(this);
		d = new DB(this);
		Intent input = getIntent(); // gets the previous intent and extras
		Bundle b = input.getExtras();
		if (b != null) {
			u = (String) b.get("user");
		}
		if (!isNetworkConnected()) {
			Toast.makeText(getApplicationContext(), "No internet connection",
					Toast.LENGTH_SHORT).show();
			i = new Intent(this, Login.class);
			startActivity(i);
			finish();

		}

		instruct = (TextView) findViewById(R.id.txtServInst);
		syncD = (Button) findViewById(R.id.btnSyncD);
		syncS = (Button) findViewById(R.id.btnSyncS);
		logout = (TextView) findViewById(R.id.txtLogOS);

		syncD.setOnClickListener(new syncDListener());
		syncS.setOnClickListener(new syncSListener());
		logout.setOnClickListener(new logoutListener());

		if (Gaeilge) { // sets text to irish strings
			instruct.setText(R.string.GsyncInfo);
			syncD.setText(R.string.GupDev);
			syncS.setText(R.string.GupSrv);
			logout.setText(R.string.Glgout);
		}
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

	private boolean isNetworkConnected() { // Checks internet connection
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	public class syncSListener implements OnClickListener {
		/* This updates the server using the currently stored balance */

		public void onClick(View arg0) {

			if (isNetworkConnected()) { //accounts for network loss after page load
				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();	
				int n = findBalance();
				bal = String.valueOf(n);
				data.add(new BasicNameValuePair("user", u));
				data.add(new BasicNameValuePair("balance", bal));
				s.addBal(data);
				Toast.makeText(getApplicationContext(), "Server Synced",
						Toast.LENGTH_SHORT).show();
				i = new Intent(getApplicationContext(), Main.class);
				startActivity(i);
				finish();

			} else {
				Toast.makeText(getApplicationContext(),
						"No internet connection", Toast.LENGTH_SHORT).show();
				i = new Intent(getApplicationContext(), Login.class);
				startActivity(i);
				finish();

			}

		}

	}
	public class syncDListener implements OnClickListener {
		/* This updates the device using the currently stored server balance */
		public void onClick(View arg0) {
			if (isNetworkConnected()) { //accounts for network loss after page load
			int balance = s.getBal(u);

			String date = checkTime();
			d.open();
			d.updateBal(balance, date);
			d.close();
			updateWidget();
			Toast.makeText(getApplicationContext(), "Device Synced",
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(), Main.class);
			startActivity(i);
			finish();

			} else {
				Toast.makeText(getApplicationContext(),
						"No internet connection", Toast.LENGTH_SHORT).show();
				i = new Intent(getApplicationContext(), Login.class);
				startActivity(i);
				finish();

			}
		}

	}
	public void updateWidget() {
		/* This updates the widget with the current balance value */
		final AlarmManager m = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent service = null;

		final Intent i = new Intent(this, LeapService.class);

		if (service == null) {
			service = PendingIntent.getService(this, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT); // sets widget as
														// pending intent
		}

		m.set(AlarmManager.RTC, System.currentTimeMillis(), service);

	}

	

	@SuppressLint("SimpleDateFormat")
	public String checkTime() {
		// Store time in database
		Calendar c = Calendar.getInstance();
		String date;
		SimpleDateFormat df;
		df = new SimpleDateFormat("\ndd/MM/yyyy\u00A0HH:mm:ss");
		date = df.format(c.getTime());
		String Time = date;
		return Time;
	}

	public int findBalance() {

		/* This method uses the DB class it calls the getBalance method */
		int rtnBalance = 0;
		DB data = new DB(this); // defines an instance of DB
		data.open();
		rtnBalance = data.getBalance(); // sets rtnBalance to the returned value
										// from the method in DB
		data.close(); // closes the DB

		return rtnBalance;

	}

	@Override
	public void onBackPressed() {
		/* Overides the default back button action, moves back to main */
		Intent i = new Intent(this, Main.class);
		startActivity(i);
		finish();
	}

	public class logoutListener implements OnClickListener {

		public void onClick(View v) {
			editor.putBoolean("logged", false);
			editor.putString("user", null);
			editor.commit();
			Intent i = new Intent(getApplicationContext(), Main.class);
			startActivity(i);
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

		switch (item.getItemId()) {
		case R.id.sync:
			if (logged) {
				i = new Intent(this, Sync.class);
				i.putExtra("user", u);
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
				i.putExtra("user", u);
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

}
