package com.jaymcd.leap_calc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SpalshScreen extends Activity {


	private static int TIME_OUT = 2000; //variable for splash timer

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.spalsh_screen);

		/* Shows splash screen*/
		new Handler().postDelayed(new Runnable() {

			public void run() {
				/* Open main after flash*/
				Intent i = new Intent(getApplicationContext(), Main.class);
				startActivity(i);

				// close splash activity
				finish();
			}
		}, TIME_OUT);
	}

}
