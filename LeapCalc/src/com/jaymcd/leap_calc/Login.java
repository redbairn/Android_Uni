package com.jaymcd.leap_calc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	String username, password;
	EditText usern, pass;
	TextView signup, instruct, loginTxt;
	Button login;
	Server s;
	SharedPreferences.Editor editor;
	SharedPreferences getprefs;
	Boolean logged, Gaeilge;
	String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setScreen();
		setContentView(R.layout.login);
			
		getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		login = (Button) findViewById(R.id.btnLgIn);
		instruct = (TextView) findViewById(R.id.txtInstL);
		loginTxt = (TextView) findViewById(R.id.txtloginL);
		signup = (TextView) findViewById(R.id.txtSup);
		login.setOnClickListener(new loginListener());
		signup.setOnClickListener(new signupListener());
		usern = (EditText) findViewById(R.id.edUser);
		pass = (EditText) findViewById(R.id.edPass);
		editor = getprefs.edit();
		logged = getprefs.getBoolean("logged", false);
		Gaeilge = getprefs.getBoolean("irish", false);
		user = getprefs.getString("user", null);
		 s = new Server(this);
		 
		 if (!isNetworkConnected()){ //logs out if no internet
			 editor.putBoolean("logged", false);
				editor.putString("user", "");
				editor.commit();
		 }
		 
		 if (Gaeilge) {
				instruct.setText(R.string.GsynInst);
				loginTxt.setText(R.string.Glin);
				login.setText(R.string.Glin);
				signup.setText(R.string.GnoAcc);
				usern.setHint(R.string.Guname);
				pass.setHint(R.string.Gpwstr);
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
			/* App crashes if there is no internet connection for server methods, so we only use methods if there is a conection*/
			if (isNetworkConnected()){
			username = usern.getText().toString();
			password = pass.getText().toString();
			password = md5(password);

			
			 if (s.getUser(username, password)){
				 editor.putBoolean("logged", true);
					editor.putString("user", username);
					editor.commit();
				 Intent i = new Intent(getApplicationContext(), Sync.class);
				 i.putExtra("user", username);
					startActivity(i);
					finish(); 
			 }else{
			Toast.makeText(getApplicationContext(), "User Not found",
			 Toast.LENGTH_SHORT).show(); }
			  
			  }
			else{
				 editor.putBoolean("logged", false); //logs out if no internet
					editor.putString("user", "");
					editor.commit();
				Toast.makeText(getApplicationContext(), "No internet connection",
					 Toast.LENGTH_SHORT).show();}
		}
			 

		}
	
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {
		   // There are no active networks.
		   return false;
		  } else
		   return true;
		 }

	@Override
	public void onBackPressed() {
		/* Overides the default back button action, moves back to main */
		Intent i = new Intent(this, Main.class);
		startActivity(i);
		finish();
	}


	public class signupListener implements OnClickListener {

		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), SignUp.class);
			startActivity(i);
			finish();

		}
	}
		public String md5(String s) {
			try {
				// Create MD5 Hash
				MessageDigest encode = java.security.MessageDigest
						.getInstance("MD5");
				encode.update(s.getBytes());
				byte messageEncode[] = encode.digest();

				// Create Hexidecimal String
				StringBuffer encodeString = new StringBuffer();
				for (int x = 0; x < messageEncode.length; x++)
					encodeString.append(Integer
							.toHexString(0xFF & messageEncode[x]));
				return encodeString.toString();

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