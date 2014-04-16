/* Leap Calculator App
 * This is the add trip activity, it contains 4 navigation buttons
 * Each button sends a string to the trip activity which will be used to generate the layout
 * */
package com.jaymcd.leap_calc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AddTrip extends Activity {

	Boolean Gaeilge, logged;
	String user;
	SharedPreferences getprefs;
	Button AddBus, AddExpBus, AddLuasR, AddLuasG, AddTrain;
	TextView jType;
	MenuItem settingsItem, exitItem;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreen();
		setContentView(R.layout.addtrip);	

		jType = (TextView) findViewById(R.id.txtJType);
		AddBus = (Button) findViewById(R.id.btnAddBus);
		AddBus.setOnClickListener(new AddBusListener());
		AddExpBus = (Button) findViewById(R.id.btnAddExpBus);
		AddExpBus.setOnClickListener(new AddExpBusListener());
		AddLuasR = (Button) findViewById(R.id.btnAddLuasR);
		AddLuasR.setOnClickListener(new AddLuasRListener());
		AddLuasG = (Button) findViewById(R.id.btnAddLuasG);
		AddLuasG.setOnClickListener(new AddLuasGListener());
		AddTrain = (Button) findViewById(R.id.btnAddTrain);
		AddTrain.setOnClickListener(new AddTrainListener());

		/* Check if Irish is selected */
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Gaeilge = getprefs.getBoolean("irish", false);
		logged = getprefs.getBoolean("logged", false);
		user = getprefs.getString("user", null);

		if (Gaeilge) {
			AddBus.setText(R.string.Gbus);
			AddExpBus.setText(R.string.Gexpbus);
			AddLuasR.setText(R.string.GluasR);
			AddLuasG.setText(R.string.GluasG);
			AddTrain.setText(R.string.Gtrain);
			jType.setText(R.string.GjType);

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

	// button listeners to direct to trip activity
	class AddBusListener implements View.OnClickListener {
		public void onClick(View v) {

			Intent i = new Intent(getApplicationContext(), Trip.class);
			i.putExtra("Type", "Bus");
			startActivity(i);
			finish();
		}
	}

	public class AddExpBusListener implements OnClickListener {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), Trip.class);
			i.putExtra("Type", "ExpBus");
			startActivity(i);
			finish();
		}
	}

	class AddLuasRListener implements View.OnClickListener {
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), Trip.class);
			i.putExtra("Type", "LuasR");
			startActivity(i);
			finish();
		}
	}

	class AddLuasGListener implements View.OnClickListener {
		public void onClick(View v) {

			Intent i = new Intent(getApplicationContext(), Trip.class);
			i.putExtra("Type", "LuasG");
			startActivity(i);
			finish();
		}
	}

	class AddTrainListener implements View.OnClickListener {
		public void onClick(View v) {

			Intent i = new Intent(getApplicationContext(), Trip.class);
			i.putExtra("Type", "Train");
			startActivity(i);
			finish();
		}
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
}
