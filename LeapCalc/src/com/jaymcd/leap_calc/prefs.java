/* Leap Calculator App
 * This is the preference activity it defines the options for the settings screen
 * */

package com.jaymcd.leap_calc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class prefs extends PreferenceActivity {

	Boolean Gaeilge;
	SharedPreferences getprefs;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setScreen();
		/* Check if Irish is selected*/
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Gaeilge = getprefs.getBoolean("irish", false);
		
		if (Gaeilge){ //changes preference screen to irish
			addPreferencesFromResource(R.xml.gprefs);
		}else{
			addPreferencesFromResource(R.xml.prefs);	
		}
		
		
	}

	public void setScreen() {
		/*
		 * checks the screen size in configuration to determine screen size
		 * (will be removed for landscape option of assignment)
		 */

		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		}
	}

	@Override
	public void onBackPressed() {
		/*
		 * Overides the default back button action, moves back to main after
		 * preferences As fare prices and buttons are based on preferences, we
		 * need to move back to main after changing
		 */
		Intent i = new Intent(this, Main.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prefs, menu);
		/* Set irish menu visible*/
		menu.findItem(R.id.save).setVisible(!Gaeilge);
		menu.findItem(R.id.Gsave).setVisible(Gaeilge);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Adds a save option under the menu for preferences, as they are
		 * automatically saved, just moves to main
		 */
		Intent i;
		switch (item.getItemId()) {
		case R.id.save:
			i = new Intent(this, Main.class);
			startActivity(i);
			finish();
			return true;
		case R.id.Gsave:
			i = new Intent(this, Main.class);
			startActivity(i);
			finish();
			return true;
		}
		return true;

	}

}
