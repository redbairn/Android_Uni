package com.jaymcd.leap_calc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity {

	String username, password, passconf;
	EditText usern, pass, passc;
	TextView login, instruct, SU;
	Button signup;
	Server s;
	SharedPreferences.Editor editor;
	SharedPreferences getprefs;
	Boolean logged, Gaeilge;
	String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setScreen();
		setContentView(R.layout.sign_up);
		
		signup = (Button) findViewById(R.id.btnSgnUp);
		login = (TextView) findViewById(R.id.txtLgIn);
		usern = (EditText) findViewById(R.id.edUser);
		pass = (EditText) findViewById(R.id.edPass);
		passc = (EditText) findViewById(R.id.edPwConf);
		instruct = (TextView) findViewById(R.id.txtSUInst);
		SU = (TextView) findViewById(R.id.txtSU);

		login.setOnClickListener(new loginListener());
		signup.setOnClickListener(new signupListener());

		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		editor = getprefs.edit();
		logged = getprefs.getBoolean("logged", false);
		Gaeilge = getprefs.getBoolean("irish", false);

		s = new Server(this);

		if (Gaeilge) {
			instruct.setText(R.string.GsynInst);
			SU.setText(R.string.GSup);
			signup.setText(R.string.GSup);
			usern.setHint(R.string.Guname);
			pass.setHint(R.string.Gpwstr);
			passc.setHint(R.string.Gpwconfstr);
			login.setText(R.string.GhavAcc);
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

	public class loginListener implements OnClickListener {
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(), Login.class);
			startActivity(i);
			finish();
		}
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public class signupListener implements OnClickListener {

		public void onClick(View arg0) {
			boolean uniqueMail;

			password = pass.getText().toString();
			passconf = passc.getText().toString();
			username = usern.getText().toString();
			password = md5(password);
			passconf = md5(passconf);
			uniqueMail = s.getUserN(username);
			if (uniqueMail) {
				if (isEmailValid(username)) {
					if (password.equals(passconf)) {

						ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
						data.add(new BasicNameValuePair("user", username));
						data.add(new BasicNameValuePair("password", password));

						s.addUser(data);
						editor.putBoolean("logged", true);
						editor.putString("user", username);
						editor.commit();
						Intent i = new Intent(getApplicationContext(),
								Sync.class);
						i.putExtra("user", username);
						startActivity(i);
						finish();

					} else {
						Toast.makeText(getApplicationContext(),
								"Passwords do not match", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Not a valid email address", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Email address already registered, log in",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
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
