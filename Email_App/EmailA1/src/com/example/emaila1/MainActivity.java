package com.example.emaila1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override 
	public void onSaveInstanceState(Bundle savedInstanceState) {
		//CAREFUL: you need to call super.onSaveInstanceState(savedInstanceState) before adding your values to the Bundle, or they will get wiped out on that call
		super.onSaveInstanceState(savedInstanceState); 
		// Store UI state to the savedInstanceState.   
		// This bundle will be passed to onCreate on next call.  
		EditText sender = (EditText) findViewById(R.id.senderEmail);
		EditText recipient = (EditText) findViewById(R.id.recipientEmail);
		EditText cc = (EditText) findViewById(R.id.ccEmail);
		EditText bcc = (EditText) findViewById(R.id.bccEmail);
		EditText subject = (EditText) findViewById(R.id.subjectEmail);
		EditText body = (EditText) findViewById(R.id.bodyEmail);
		
		String FROM = sender.getText().toString();
		String TO = recipient.getText().toString();
		String CC = cc.getText().toString();
		String BCC = bcc.getText().toString();
		String SUBJECT = subject.getText().toString();
		String BODY = body.getText().toString();
		
		savedInstanceState.putString("sender", FROM);   
		savedInstanceState.putString("recipient", TO);
		savedInstanceState.putString("carboncopy", CC);
		savedInstanceState.putString("blindcarboncopy", BCC);
		savedInstanceState.putString("eSubject", SUBJECT);
		savedInstanceState.putString("eBody", BODY);
		}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  String myString1 = savedInstanceState.getString("sender");
	  String myString2 = savedInstanceState.getString("recipient");
	  String myString3 = savedInstanceState.getString("carboncopy");
	  String myString4 = savedInstanceState.getString("blindcarboncopy");
	  String myString5 = savedInstanceState.getString("eSubject");
	  String myString6 = savedInstanceState.getString("eBody");
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		
	Intent i = new Intent(MainActivity.this, DisplayMessageActivity.class);
	
	EditText sender = (EditText) findViewById(R.id.senderEmail);
	EditText recipient = (EditText) findViewById(R.id.recipientEmail);
	EditText cc = (EditText) findViewById(R.id.ccEmail);
	EditText bcc = (EditText) findViewById(R.id.bccEmail);
	EditText subject = (EditText) findViewById(R.id.subjectEmail);
	EditText body = (EditText) findViewById(R.id.bodyEmail);
	
	String FROM = sender.getText().toString();
	String TO = recipient.getText().toString();
	String CC = cc.getText().toString();
	String BCC = bcc.getText().toString();
	String SUBJECT = subject.getText().toString();
	String BODY = body.getText().toString();
	
	i.putExtra("FROM", FROM);
	i.putExtra("TO", TO);
	i.putExtra("CC", CC);
	i.putExtra("BCC", BCC);
	i.putExtra("SUBJECT", SUBJECT);
	i.putExtra("BODY", BODY);
	
	startActivity(i);
	}
	
	public void clearMessage(View view)
	{
	    EditText FROM = (EditText)this.findViewById(R.id.senderEmail);
	    EditText TO = (EditText)this.findViewById(R.id.recipientEmail);
	    EditText CC = (EditText)this.findViewById(R.id.ccEmail);
	    EditText BCC = (EditText)this.findViewById(R.id.bccEmail);
	    EditText SUBJECT = (EditText)this.findViewById(R.id.subjectEmail);
	    EditText BODY = (EditText)this.findViewById(R.id.bodyEmail);
	    //...etc...
	    
	    FROM.setText("");
	    TO.setText("");
	    CC.setText("");
	    BCC.setText("");
	    SUBJECT.setText("");
	    BODY.setText("");

	}
}
