package com.example.kids_app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;




public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv=(TextView)findViewById(R.id.goApplePage);
		TextView ta=(TextView)findViewById(R.id.banana_id);
		TextView tb=(TextView)findViewById(R.id.grape_id);
		
	    Typeface face=Typeface.createFromAsset(getAssets(),
	                                          "fonts/DK Crayon Crumble.ttf");

	    tv.setTypeface(face);
	    ta.setTypeface(face);
	    tb.setTypeface(face);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**Apple activity starts up when the text is clicked */
	public void goApple(View view) {
	    // Do something in response to button
		
	Intent i = new Intent(MainActivity.this, Apple.class);
	startActivity(i);
	}
	
	/**Banana activity starts up when the text is clicked */
	public void goBanana(View view) {
	    // Do something in response to button
		
	Intent i = new Intent(MainActivity.this, Banana.class);
	startActivity(i);
	}
	
	/**Grapes activity starts up when the text is clicked */
	public void goGrapes(View view) {
	    // Do something in response to button
		
	Intent i = new Intent(MainActivity.this, Grapes.class);
	startActivity(i);
	}
}
