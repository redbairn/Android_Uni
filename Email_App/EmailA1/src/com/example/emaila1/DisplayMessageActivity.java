package com.example.emaila1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView readWord1=(TextView)findViewById(R.id.txtReadWord);
		TextView readWord2=(TextView)findViewById(R.id.txtReadWord2);
		TextView readWord3=(TextView)findViewById(R.id.txtReadWord3);
		TextView readWord4=(TextView)findViewById(R.id.txtReadWord4);
		TextView readWord5=(TextView)findViewById(R.id.txtReadWord5);

		
		
		// Get the message from the intent
	    Intent i = getIntent();
	    
	    
	    // Reading bundle (layout)
	    Bundle b = i.getExtras(); //get extras from previous intent

	    if(b!=null) //as long as there are extras

	    {
	        String FromRead =(String) b.get("FROM"); //assign j the value of the "word" string

	        readWord1.setText("From: " + FromRead);

	        String ToRead = (String) b.get("TO");

	        readWord2.setText("To: " + ToRead);
	        
	        String CcRead =(String) b.get("CC");

	        readWord3.setText("Cc:" + CcRead);
	        
	        String SubjectRead =(String) b.get("SUBJECT");

	        readWord4.setText("Subject:" + SubjectRead);
	        
	        String BodyRead =(String) b.get("BODY");

	        readWord5.setText(BodyRead);
	    }  
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


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.display_message, menu);
//		return true;
//	}
	
	//Back/Return button used to return to Main
	public void returnMain(View view) {
		finish();
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
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
