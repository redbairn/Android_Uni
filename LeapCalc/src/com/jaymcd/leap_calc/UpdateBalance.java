package com.jaymcd.leap_calc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class UpdateBalance extends Activity {

	int topUp, rtnBalance, balance;
	Button addBalance, history, Eur5, Eur10, Eur20;
	TextView quick, newBal;
	EditText editBalance;
	Boolean Gaeilge, logged;
	SharedPreferences getprefs;
	String user;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreen();
		/* Check if Irish is selected */
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Gaeilge = getprefs.getBoolean("irish", false);

		setContentView(R.layout.upd_bal);
		
		
		// closes the notification on load
		NotificationManager x = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		x.cancel(2932553);

		newBal = (TextView) findViewById(R.id.txtTLbl1);
		quick = (TextView) findViewById(R.id.txtQUp);
		editBalance = (EditText) findViewById(R.id.edtBalance);
		addBalance = (Button) findViewById(R.id.btnUpdate);
		addBalance.setOnClickListener(new AddBalanceListener());
		history = (Button) findViewById(R.id.btnHistory);
		history.setOnClickListener(new HistoryListener());
		Eur5 = (Button) findViewById(R.id.btnEur5);
		Eur10 = (Button) findViewById(R.id.btnEur10);
		Eur20 = (Button) findViewById(R.id.btnEur20);
		Eur5.setOnClickListener(new Eur5Listener());
		Eur10.setOnClickListener(new Eur10Listener());
		Eur20.setOnClickListener(new Eur20Listener());

		logged = getprefs.getBoolean("logged", false);
		user = getprefs.getString("user", null);
		if (Gaeilge) {
			newBal.setText(R.string.GenterBal);
			quick.setText(R.string.GquUp);
			addBalance.setText(R.string.Gupd);
			history.setText(R.string.Ghis);
		}
		DB data = new DB(this);
		data.open();
		rtnBalance = data.getBalance();
		data.close();

		formatBalance(rtnBalance);

		/* Two listeners below submit text on enter or done being pressed */
		editBalance.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_NULL
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					AddBalance();
					return true;
				}
				return false;
			}
		});

		editBalance.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_DONE) {
					AddBalance();
				}
				return false;
			}
		});

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

	public void formatBalance(int bal) {
		double calc = Double.valueOf(bal);
		DecimalFormat dec = new DecimalFormat("###.00");
		double rawPercent = ((double) (calc) / 100.00);
		// sets the variable "balance" to the same as rtnBalance (so it
		// can be used in different methods)
		balance = bal;
		if (rawPercent < 0 && rawPercent > -10) {
			int minusnum;
			minusnum = Math.abs(-bal);
			double minuscalc = Double.valueOf(minusnum);
			// formats the balance to a decimal (it is stored as an int
			// in the table)
			double minusPercent = ((double) (minuscalc) / 100.00);

			// sets the text of the edit text to the current balance
			editBalance.setHint("-0" + dec.format(minusPercent));
			// if the balance is greater than 0 and less than 100 a 0 is
			// places before the balance value
			// if not the balance is set
		} else if (rawPercent < 0 && rawPercent > -100) {
			int minusnum;
			minusnum = Math.abs(-bal);
			double minuscalc = Double.valueOf(minusnum);
			// formats the balance to a decimal (it is stored as an int
			// in the table)
			double minusPercent = ((double) (minuscalc) / 100.00);

			// sets the text of the edit text to the current balance
			editBalance.setHint("-" + dec.format(minusPercent));

		} else if (rawPercent >= 0 && rawPercent < 10) {

			editBalance.setHint("0" + dec.format(rawPercent));
		} else {
			editBalance.setHint(dec.format(rawPercent));

		}
	}

	class AddBalanceListener implements View.OnClickListener {
		public void onClick(View v) {

			AddBalance();

		}
	}

	class Eur5Listener implements View.OnClickListener {
		public void onClick(View v) {
			// sets the value of topup to 500 and runs the TopUp method (adds €5
			// to balance)
			topUp = 500;
			TopUp();

		}
	}

	class Eur10Listener implements View.OnClickListener {
		public void onClick(View v) {
			// sets the value of topup to 1000 and runs the TopUp method (adds
			// €10 to balance)
			topUp = 1000;
			TopUp();

		}
	}

	class Eur20Listener implements View.OnClickListener {
		public void onClick(View v) {
			// sets the value of topup to 2000 and runs the TopUp method (adds
			// €20 to balance)
			topUp = 2000;
			TopUp();

		}
	}

	class cnclBalanceListener implements View.OnClickListener {
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), Main.class);
			startActivity(i);
			finish();
		}
	}

	public class HistoryListener implements OnClickListener {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), History.class);
			startActivity(i);
			finish();
		}

	}

	public void AddBalance() {
		// gets the value from the edit text box and adds it to the balancetable
		String balanceAmt = editBalance.getText().toString();
		if (balanceAmt.equals("") || balanceAmt.equals(" ")) {
			balanceAmt = "0";
			editBalance.setText("");
		}
		float parseVal;
		// converts the string to a double
		parseVal = Float.parseFloat(balanceAmt) * 100;
		int newVal = Math.round(parseVal);
		if (!balanceAmt.equals("") || !balanceAmt.equals(" ")) {

			String date = checkTime();
			DB data = new DB(this);
			data.open();
			data.updateBal(newVal, date);
			data.close();
			updateWidget();

			Intent i = new Intent(this, Main.class);
			startActivityForResult(i, 10);
			finish();
		} else {
			editBalance.setText("");
			Toast.makeText(this, "Invalid Balance", Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void TopUp() {
		// adds the value of topup (determined by the button pressed) to the
		// current balance
		int topVal;

		topVal = balance + topUp;
		String date = checkTime();
		DB data = new DB(this);
		data.open();
		data.updateBal(topVal, date);
		data.close();
		updateWidget();
		Intent i = new Intent(this, Main.class);
		startActivityForResult(i, 10);
		finish();
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

	
	public void updateWidget() {
	
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