package com.example.kids_app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.support.v4.app.NavUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

public class Banana extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banana);
		
		// Adding font to banana
		TextView tv=(TextView)findViewById(R.id.bananaNom);
		
		
	    Typeface face=Typeface.createFromAsset(getAssets(),
	                                          "fonts/DK Crayon Crumble.ttf");

	    tv.setTypeface(face);
	    // for font_size on various devices
	    tv.setTextSize(getResources().getDimension(R.dimen.font_size));
	    
	    
	    //Big Capital letter
			// This is the text I'll be operating on
		    Spannable span = new SpannableString(tv.getText());
		    
		    // make "A" (characters 0 to 1) white
		    span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 1, 0);
		    span.setSpan(new RelativeSizeSpan(2.5f), 0, 1, 0);
		    
		    // shoves my styled text into the TextView          
		    tv.setText(span, BufferType.SPANNABLE);
		    

		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.banana, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
