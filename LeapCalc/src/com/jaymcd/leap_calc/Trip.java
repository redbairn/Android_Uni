/* Leap Calculator App
 * This is the most complicated activity so bear with me
 * The layout of this activity is generated based on what string has been passed from the previous activity
 * The button text and fares are generated based on whether or not settings have been set in preferences
 * */

package com.jaymcd.leap_calc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

@SuppressLint("SimpleDateFormat")
public class Trip extends Activity {
	String tripType;
	int rtnBalance, fare;
	Boolean student = false;
	Boolean child = false;
	Boolean schTime = false;
	Boolean logged;
	String user;
	SharedPreferences getprefs;
	SharedPreferences.Editor editor;
	int[] bus, cbus, sbus, luasR, cluasR, sluasR, luasG, cluasG, sluasG, train,
			ctrain, strain, expbus, cexpbus;
	Button fare1, fare2, fare3, fare4, fare5, fare6, fare7;
	Button[] Fares;
	Boolean Gaeilge;
	TextView txtBLbl1, txtBLbl2, txtBLbl3, txtBLbl4, txtBLbl5, txtLGLbl1,
			txtLGLbl2, txtLGLbl3, txtLGLbl4, txtLGLbl5, txtLRLbl1, txtLRLbl2,
			txtLRLbl3, txtLRLbl4, txtLRLbl5, txtTLbl1, txtTLbl2, txtTLbl3,
			txtTLbl4, txtTLbl5, txtTLbl6, txtTLbl7, txtExBLbl1, txtExBLbl2,
			txtFare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreen();
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		editor = getprefs.edit();
		child = getprefs.getBoolean("chld", false);
		student = getprefs.getBoolean("schld", false);
		Gaeilge = getprefs.getBoolean("irish", false);
		logged = getprefs.getBoolean("logged", false);
		user = getprefs.getString("user", null);
		DB data = new DB(this);
		data.open();
		rtnBalance = data.getBalance();
		data.close();

		/*
		 * Below is an array of fares for each type of trip, here an array is
		 * used as these fares may change
		 */
		// bus fares
		bus = new int[] { 55, 140, 190, 210, 245 }; // standard
		cbus = new int[] { 50, 90, 90, 110, 110 }; // child fare
		sbus = new int[] { 50, 70, 70, 70, 70 }; // school child

		// express bus fares
		expbus = new int[] { 290, 400 };
		cexpbus = new int[] { 190, 220 };

		// luas red line fares
		luasR = new int[] { 145, 175, 205, 225, 240 };
		cluasR = new int[] { 80, 80, 80, 100, 100 };
		sluasR = new int[] { 80, 80, 80, 100, 100 };

		// luas green line fares
		luasG = new int[] { 145, 180, 215, 235, 250 };
		cluasG = new int[] { 80, 80, 80, 100, 100 };
		sluasG = new int[] { 80, 80, 80, 100, 100 };

		// train fares
		train = new int[] { 135, 165, 195, 230, 275, 330, 435 };
		ctrain = new int[] { 80, 80, 115, 115, 130, 130, 175 };
		strain = new int[] { 70, 70, 70, 70, 70, 70, 70 };

		checkTime(); // calls the check time method from below
		
		/* This code checks the string variable sent from previous activity */
		Intent input = getIntent();
		Bundle b = input.getExtras();
		if (b != null) {
			tripType = (String) b.get("Type");
		}

		if (tripType.equals("Bus")) {
			setContentView(R.layout.addbus); // sets content view to the addbus
												// layout
			setupBus(); // calls the setupBus method from below
		} else if (tripType.equals("ExpBus")) {
			setContentView(R.layout.addexpbus); // sets content view to the
												// addbus
			// layout
			setupExpBus(); // calls the setupBus method from below
		} else if (tripType.equals("Train")) {
			setContentView(R.layout.addtrain); // sets content view to the
												// addtrain layout
			setupTrain();
		} else if (tripType.equals("LuasG")) {
			setContentView(R.layout.addluasg); // sets content view to the
												// addluasg layout
			setupLuasG();
		} else if (tripType.equals("LuasR")) {
			setContentView(R.layout.addluasr); // sets content view to the
												// addluasr layout
			setupLuasR();
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

	public void checkTime() {
		// Gets the current system time to note if student fares apply

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		if (hour > 6 && hour < 17) {
			/*
			 * if between 6am an 5pm sets schtime to true currently within
			 * school hours
			 */

			schTime = true;
		} else {
			schTime = false;
		}

	}

	public void setupTrain() {
		/*
		 * This code sets the buttons based on the train layout If school child
		 * (and currently school hours) or child has been selected appropriate
		 * button text is set
		 */
		fare1 = (Button) findViewById(R.id.btnTrain1);
		fare1.setOnClickListener(new Trfare1Listener());
		fare2 = (Button) findViewById(R.id.btnTrain2);
		fare2.setOnClickListener(new Trfare2Listener());
		fare3 = (Button) findViewById(R.id.btnTrain3);
		fare3.setOnClickListener(new Trfare3Listener());
		fare4 = (Button) findViewById(R.id.btnTrain4);
		fare4.setOnClickListener(new Trfare4Listener());
		fare5 = (Button) findViewById(R.id.btnTrain5);
		fare5.setOnClickListener(new Trfare5Listener());
		fare6 = (Button) findViewById(R.id.btnTrain6);
		fare6.setOnClickListener(new Trfare6Listener());
		fare7 = (Button) findViewById(R.id.btnTrain7);
		fare7.setOnClickListener(new Trfare7Listener());

		txtFare = (TextView) findViewById(R.id.txtlblFare);

		txtTLbl1 = (TextView) findViewById(R.id.txtTLbl1);
		txtTLbl2 = (TextView) findViewById(R.id.txtTLbl2);
		txtTLbl3 = (TextView) findViewById(R.id.txtTLbl3);
		txtTLbl4 = (TextView) findViewById(R.id.txtTLbl4);
		txtTLbl5 = (TextView) findViewById(R.id.txtTLbl5);
		txtTLbl6 = (TextView) findViewById(R.id.txtTLbl6);
		txtTLbl7 = (TextView) findViewById(R.id.txtTLbl7);

		if (Gaeilge) {// change to irish labels
			txtFare.setText(R.string.GselFare);
			txtTLbl1.setText(R.string.GcatA);
			txtTLbl2.setText(R.string.GcatB);
			txtTLbl3.setText(R.string.GcatG);
			txtTLbl4.setText(R.string.GcatD);
			txtTLbl5.setText(R.string.GcatJ);
			txtTLbl6.setText(R.string.GcatC);
			txtTLbl7.setText(R.string.GcatE);
		}

		Fares = new Button[] { fare1, fare2, fare3, fare4, fare5, fare6, fare7 }; // makes
																					// an
																					// array
																					// of
																					// buttons

		double c, val;
		DecimalFormat dec = new DecimalFormat("###.00"); // format the value to
															// decimal

		if (student && schTime) { // if school child is checked and school time
									// is true
			for (int i = 0; i < strain.length; i++) {
				c = Double.valueOf(strain[i]); // convert array value to double
				val = ((double) (c) / 100.00);
				if (strain[i] < 100) {
					Fares[i].setText("€0" + dec.format(val)); // set button text
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (student && !schTime) {
			for (int i = 0; i < ctrain.length; i++) {
				c = Double.valueOf(ctrain[i]);
				val = ((double) (c) / 100.00);
				if (ctrain[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (child) {
			for (int i = 0; i < ctrain.length; i++) {
				c = Double.valueOf(ctrain[i]);
				val = ((double) (c) / 100.00);
				if (ctrain[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else {
			for (int i = 0; i < train.length; i++) {
				c = Double.valueOf(train[i]);
				val = ((double) (c) / 100.00);
				if (train[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		}
	}

	public void setupLuasG() {
		/*
		 * This code sets the buttons based on the luas green layout If school
		 * child (and currently school hours) or child has been selected
		 * appropriate button text is set
		 */
		fare1 = (Button) findViewById(R.id.btnLuasG1);
		fare1.setOnClickListener(new LGfare1Listener());
		fare2 = (Button) findViewById(R.id.btnLuasG2);
		fare2.setOnClickListener(new LGfare2Listener());
		fare3 = (Button) findViewById(R.id.btnLuasG3);
		fare3.setOnClickListener(new LGfare3Listener());
		fare4 = (Button) findViewById(R.id.btnLuasG4);
		fare4.setOnClickListener(new LGfare4Listener());
		fare5 = (Button) findViewById(R.id.btnLuasG5);
		fare5.setOnClickListener(new LGfare5Listener());
		Fares = new Button[] { fare1, fare2, fare3, fare4, fare5 };

		txtFare = (TextView) findViewById(R.id.txtlblFare);

		txtLGLbl1 = (TextView) findViewById(R.id.txtLGLbl1);
		txtLGLbl2 = (TextView) findViewById(R.id.txtLGLbl2);
		txtLGLbl3 = (TextView) findViewById(R.id.txtLGLbl3);
		txtLGLbl4 = (TextView) findViewById(R.id.txtLGLbl4);
		txtLGLbl5 = (TextView) findViewById(R.id.txtLGLbl5);

		if (Gaeilge) {// change to irish labels
			txtFare.setText(R.string.GselFare);
			txtLGLbl1.setText(R.string.GZone1);
			txtLGLbl2.setText(R.string.GZone2);
			txtLGLbl3.setText(R.string.GZone3);
			txtLGLbl4.setText(R.string.GZone4);
			txtLGLbl5.setText(R.string.GZone5);
		}

		double c, val;
		DecimalFormat dec = new DecimalFormat("###.00");

		if (student && schTime) {
			for (int i = 0; i < sluasG.length; i++) {
				c = Double.valueOf(sluasG[i]);
				val = ((double) (c) / 100.00);
				if (sluasG[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (student && !schTime) {
			for (int i = 0; i < cluasG.length; i++) {
				c = Double.valueOf(cluasG[i]);
				val = ((double) (c) / 100.00);
				if (cluasG[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (child) {
			for (int i = 0; i < cluasG.length; i++) {
				c = Double.valueOf(cluasG[i]);
				val = ((double) (c) / 100.00);
				if (cluasG[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else {
			for (int i = 0; i < luasG.length; i++) {
				c = Double.valueOf(luasG[i]);
				val = ((double) (c) / 100.00);
				if (luasG[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		}
	}

	public void setupLuasR() {
		/*
		 * This code sets the buttons based on the luas red layout If school
		 * child (and currently school hours) or child has been selected
		 * appropriate button text is set
		 */
		fare1 = (Button) findViewById(R.id.btnLuasR1);
		fare1.setOnClickListener(new LRfare1Listener());
		fare2 = (Button) findViewById(R.id.btnLuasR2);
		fare2.setOnClickListener(new LRfare2Listener());
		fare3 = (Button) findViewById(R.id.btnLuasR3);
		fare3.setOnClickListener(new LRfare3Listener());
		fare4 = (Button) findViewById(R.id.btnLuasR4);
		fare4.setOnClickListener(new LRfare4Listener());
		fare5 = (Button) findViewById(R.id.btnLuasR5);
		fare5.setOnClickListener(new LRfare5Listener());

		txtFare = (TextView) findViewById(R.id.txtlblFare);

		txtLRLbl1 = (TextView) findViewById(R.id.txtLRLbl1);
		txtLRLbl2 = (TextView) findViewById(R.id.txtLRLbl2);
		txtLRLbl3 = (TextView) findViewById(R.id.txtLRLbl3);
		txtLRLbl4 = (TextView) findViewById(R.id.txtLRLbl4);
		txtLRLbl5 = (TextView) findViewById(R.id.txtLRLbl5);

		if (Gaeilge) {// change to irish labels
			txtFare.setText(R.string.GselFare);
			txtLRLbl1.setText(R.string.GZone1);
			txtLRLbl2.setText(R.string.GZone2);
			txtLRLbl3.setText(R.string.GZone3);
			txtLRLbl4.setText(R.string.GZone4);
			txtLRLbl5.setText(R.string.GZone5);
		}

		Fares = new Button[] { fare1, fare2, fare3, fare4, fare5 };

		double c, val;
		DecimalFormat dec = new DecimalFormat("###.00");

		if (student && schTime) {
			for (int i = 0; i < sluasR.length; i++) {
				c = Double.valueOf(sluasR[i]);
				val = ((double) (c) / 100.00);
				if (sluasR[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (student && !schTime) {
			for (int i = 0; i < cluasR.length; i++) {
				c = Double.valueOf(cluasR[i]);
				val = ((double) (c) / 100.00);
				if (cluasR[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (child) {
			for (int i = 0; i < cluasR.length; i++) {
				c = Double.valueOf(cluasR[i]);
				val = ((double) (c) / 100.00);
				if (cluasR[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else {
			for (int i = 0; i < luasR.length; i++) {
				c = Double.valueOf(luasR[i]);
				val = ((double) (c) / 100.00);
				if (luasR[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		}
	}

	public void setupBus() {
		/*
		 * This code sets the buttons based on the bus layout If school child
		 * (and currently school hours) or child has been selected appropriate
		 * button text is set
		 */
		fare1 = (Button) findViewById(R.id.btnBus1);
		fare1.setOnClickListener(new busFare1Listener());
		fare2 = (Button) findViewById(R.id.btnBus2);
		fare2.setOnClickListener(new busFare2Listener());
		fare3 = (Button) findViewById(R.id.btnBus3);
		fare3.setOnClickListener(new busFare3Listener());
		fare4 = (Button) findViewById(R.id.btnBus4);
		fare4.setOnClickListener(new busFare4Listener());
		fare5 = (Button) findViewById(R.id.btnBus5);
		fare5.setOnClickListener(new busFare5Listener());

		txtFare = (TextView) findViewById(R.id.txtlblFare);

		txtBLbl1 = (TextView) findViewById(R.id.txtBLbl1);
		txtBLbl2 = (TextView) findViewById(R.id.txtBLbl2);
		txtBLbl3 = (TextView) findViewById(R.id.txtBLbl3);
		txtBLbl4 = (TextView) findViewById(R.id.txtBLbl4);
		txtBLbl5 = (TextView) findViewById(R.id.txtBLbl5);

		if (Gaeilge) {// change to irish labels
			txtFare.setText(R.string.GselFare);
			txtBLbl1.setText(R.string.GccBFare);
			txtBLbl2.setText(R.string.Gstg1);
			txtBLbl3.setText(R.string.Gstg4);
			txtBLbl4.setText(R.string.Gstg4);
			txtBLbl5.setText(R.string.Gov13stg);
		}

		Fares = new Button[] { fare1, fare2, fare3, fare4, fare5 };

		double c, val;
		DecimalFormat dec = new DecimalFormat("###.00");

		if (student && schTime) {
			for (int i = 0; i < sbus.length; i++) {
				c = Double.valueOf(sbus[i]);
				val = ((double) (c) / 100.00);
				if (sbus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (student && !schTime) {
			for (int i = 0; i < cbus.length; i++) {
				c = Double.valueOf(cbus[i]);
				val = ((double) (c) / 100.00);
				if (cbus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else if (child) {
			for (int i = 0; i < cbus.length; i++) {
				c = Double.valueOf(cbus[i]);
				val = ((double) (c) / 100.00);
				if (cbus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else {
			for (int i = 0; i < bus.length; i++) {
				c = Double.valueOf(bus[i]);
				val = ((double) (c) / 100.00);
				if (bus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		}

	}

	public void setupExpBus() {
		/*
		 * This code sets the buttons based on the bus layout If school child
		 * (and currently school hours) or child has been selected appropriate
		 * button text is set
		 */
		fare1 = (Button) findViewById(R.id.btnExB1);
		fare1.setOnClickListener(new expbusFare1Listener());
		fare2 = (Button) findViewById(R.id.btnExB2);
		fare2.setOnClickListener(new expbusFare2Listener());

		txtFare = (TextView) findViewById(R.id.txtlblFare);

		txtExBLbl1 = (TextView) findViewById(R.id.txtExBLbl1);
		txtExBLbl2 = (TextView) findViewById(R.id.txtExBLbl2);

		if (Gaeilge) {// change to irish labels
			txtFare.setText(R.string.GselFare);
			txtExBLbl1.setText(R.string.GZone1);
			txtExBLbl2.setText(R.string.GZone2);

		}

		Fares = new Button[] { fare1, fare2 };

		double c, val;
		DecimalFormat dec = new DecimalFormat("###.00");

		if (child) {
			for (int i = 0; i < cexpbus.length; i++) {
				c = Double.valueOf(cexpbus[i]);
				val = ((double) (c) / 100.00);
				if (cexpbus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		} else {
			for (int i = 0; i < expbus.length; i++) {
				c = Double.valueOf(expbus[i]);
				val = ((double) (c) / 100.00);
				if (expbus[i] < 100) {
					Fares[i].setText("€0" + dec.format(val));
				} else {
					Fares[i].setText("€" + dec.format(val));
				}

			}
		}

	}

	/*
	 * Below are the listeners for all the bus fare buttons If school child (and
	 * currently school hours) or child has been selected appropriate fare is
	 * set
	 */
	class busFare1Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = sbus[0];
				} else {
					fare = cbus[0];
				}
			} else if (child) {
				fare = cbus[0];
			} else {
				fare = bus[0];
			}

			Calc(); // calls the Calc method
		}

	}

	class busFare2Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = sbus[1];
				} else {
					fare = cbus[1];
				}
			} else if (child) {
				fare = cbus[1];
			} else {
				fare = bus[1];
			}
			Calc(); // calls the Calc method
		}
	}

	class busFare3Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = sbus[2];
				} else {
					fare = cbus[2];
				}
			} else if (child) {
				fare = cbus[2];
			} else {
				fare = bus[2];
			}
			Calc(); // calls the Calc method
		}
	}

	class busFare4Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = sbus[3];
				} else {
					fare = cbus[3];
				}
			} else if (child) {
				fare = cbus[3];
			} else {
				fare = bus[3];
			}
			Calc(); // calls the Calc method
		}
	}

	class busFare5Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = sbus[4];
				} else {
					fare = cbus[4];
				}
			} else if (child) {
				fare = cbus[4];
			} else {
				fare = bus[4];
			}
			Calc(); // calls the Calc method
		}
	}

	class expbusFare1Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (child) {
				fare = cexpbus[0];
			} else {
				fare = expbus[0];
			}

			Calc(); // calls the Calc method
		}

	}

	class expbusFare2Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (child) {
				fare = cexpbus[1];
			} else {
				fare = expbus[1];
			}
			Calc(); // calls the Calc method
		}
	}

	/*
	 * Below are the listeners for all the luas red fare buttons If school child
	 * (and currently school hours) or child has been selected appropriate fare
	 * is set
	 */
	class LRfare1Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasR[0];
			} else if (child) {
				fare = cluasR[0];
			} else {
				fare = luasR[0];
			}
			Calc(); // calls the Calc method
		}
	}

	class LRfare2Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasR[1];
			} else if (child) {
				fare = cluasR[1];
			} else {
				fare = luasR[1];
			}
			Calc(); // calls the Calc method
		}
	}

	class LRfare3Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasR[2];
			} else if (child) {
				fare = cluasR[2];
			} else {
				fare = luasR[2];
			}
			Calc(); // calls the Calc method
		}
	}

	class LRfare4Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasR[3];
			} else if (child) {
				fare = cluasR[3];
			} else {
				fare = luasR[3];
			}
			Calc(); // calls the Calc method
		}
	}

	class LRfare5Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasR[4];
			} else if (child) {
				fare = cluasR[4];
			} else {
				fare = luasR[4];
			}
			Calc(); // calls the Calc method
		}
	}

	/*
	 * Below are the listeners for all the luas green fare buttons If school
	 * child (and currently school hours) or child has been selected appropriate
	 * fare is set
	 */
	class LGfare1Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasG[0];
			} else if (child) {
				fare = cluasG[0];
			} else {
				fare = luasG[0];
			}
			Calc(); // calls the Calc method
		}
	}

	class LGfare2Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasG[1];
			} else if (child) {
				fare = cluasG[1];
			} else {
				fare = luasG[1];
			}
			Calc(); // calls the Calc method
		}
	}

	class LGfare3Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasG[2];
			} else if (child) {
				fare = cluasG[2];
			} else {
				fare = luasG[2];
			}
			Calc(); // calls the Calc method
		}
	}

	class LGfare4Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasG[3];
			} else if (child) {
				fare = cluasG[3];
			} else {
				fare = luasG[3];
			}

			Calc(); // calls the Calc method

		}
	}

	class LGfare5Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				fare = cluasG[4];
			} else if (child) {
				fare = cluasG[4];
			} else {
				fare = luasG[4];
			}
			Calc(); // calls the Calc method

		}
	}

	/*
	 * Below are the listeners for all the train fare buttons If school child
	 * (and currently school hours) or child has been selected appropriate fare
	 * is set
	 */
	class Trfare1Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance

			if (student) {
				if (schTime) {
					fare = strain[0];
				} else {
					fare = ctrain[0];
				}
			} else if (child) {
				fare = ctrain[0];
			} else {
				fare = train[0];
			}

			Calc(); // calls the Calc method
		}
	}

	class Trfare2Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[1];
				} else {
					fare = ctrain[1];
				}
			} else if (child) {
				fare = ctrain[1];
			} else {
				fare = train[1];
			}
			Calc(); // calls the Calc method
		}
	}

	class Trfare3Listener implements View.OnClickListener {
		public void onClick(View v) {
			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[2];
				} else {
					fare = ctrain[2];
				}
			} else if (child) {
				fare = ctrain[2];
			} else {
				fare = train[2];
			}
			Calc(); // calls the Calc method
		}
	}

	class Trfare4Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[3];
				} else {
					fare = ctrain[3];
				}
			} else if (child) {
				fare = ctrain[3];
			} else {
				fare = train[3];
			}
			Calc(); // calls the Calc method
		}
	}

	class Trfare5Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[4];
				} else {
					fare = ctrain[4];
				}
			} else if (child) {
				fare = ctrain[4];
			} else {
				fare = train[4];
			}
			Calc(); // calls the Calc method
		}
	}

	class Trfare6Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[5];
				} else {
					fare = ctrain[5];
				}
			} else if (child) {
				fare = ctrain[5];
			} else {
				fare = train[5];
			}
			Calc(); // calls the Calc method
		}
	}

	class Trfare7Listener implements View.OnClickListener {
		public void onClick(View v) {

			// deducts the fare values (from the database) from the balance
			if (student) {
				if (schTime) {
					fare = strain[6];
				} else {
					fare = ctrain[6];
				}
			} else if (child) {
				fare = ctrain[6];
			} else {
				fare = train[6];
			}
			Calc(); // calls the Calc method
		}
	}

	public void Calc() {
		// calculates the balance deducting the fare
		int Result = rtnBalance - fare;

		String date = bcheckTime(); // get current time
		DB data = new DB(this); // opens the database
		data.open();
		data.updateBal(Result, date); // calls the update balance function of
										// the database
		data.close();
		updateWidget();
		// moves to main screen
		Intent i = new Intent(this, Main.class);
		startActivity(i);
		finish();

	}

	public String bcheckTime() {
		// Store time in database
		Calendar c = Calendar.getInstance();
		String date;
		SimpleDateFormat df;
		df = new SimpleDateFormat("\ndd/MM/yyyy\u00A0HH:mm:ss");
		date = df.format(c.getTime());
		String Time = date;
		return Time;
	}

	public void updateWidget() { // updates widget

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
	public void onBackPressed() {
		/* Overides the default back button action, moves back to main */
		Intent i = new Intent(this, AddTrip.class);
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
