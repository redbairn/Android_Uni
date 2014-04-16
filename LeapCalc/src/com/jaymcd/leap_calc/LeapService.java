package com.jaymcd.leap_calc;

import java.text.DecimalFormat;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class LeapService extends Service {
	RemoteViews v;
	

	@Override  
    public void onCreate()  
    {  
        super.onCreate();  
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId)  
    {  
        buildUpdate();  
  
        return super.onStartCommand(intent, flags, startId);  
    }  
    
    private void buildUpdate()  
    { 
    	int rtnBalance;
		DB data = new DB(this); // defines an instance of DB
		data.open();
		rtnBalance = data.getBalance(); //sets rtnBalance to the returned value from the DB
		data.close(); // closes the DB
		
		String balance = formatBalance(rtnBalance); //calls a method defined below to format the value
		
		
		v = new RemoteViews(getPackageName(), R.layout.widget); //calls the layout of the widget
		v.setTextViewText(R.id.txtBal, balance); //sets the text on the textview btnBal
		
		/* This code creates a click action for the widget to open main app*/
		Intent x = new Intent(this, Main.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, x, 0); //creates a pending intent 
		v.setOnClickPendingIntent(R.id.btnBal,pi); // sets an onclick action for the btnBal layout
		v.setOnClickPendingIntent(R.id.txtBal,pi); // sets an onclick action for the txtBal textview
		
		// Push update for this widget to the home screen  
        ComponentName thisWidget = new ComponentName(this, LCWidget.class);  //Makes a component from widget
        AppWidgetManager manager = AppWidgetManager.getInstance(this);  
        manager.updateAppWidget(thisWidget, v);  //calls the widget update
    	
    	
    }
	
    public String formatBalance(int bal) {
		/* Sets up a decimal format for the value of bal
		 * As the database can only store ints we cannot use decimal values 
		 * When storing values in DB, but we do need them to display as decimals
		*/
		String balance;
		double calc = Double.valueOf(bal);
		DecimalFormat dec = new DecimalFormat("###.00"); // sets the decimal
															// format
		double rawPercent = ((double) (calc) / 100.00); // divides by 100 as
														// the integers in
														// the database are
														// the cent values
														// not euro
		// displays the balance in percent format and adds a € or -€ when
		// needed
		if (rawPercent < 0 && rawPercent > -10) {
			int minusnum;
			minusnum = Math.abs(-bal);// turns the negative number to
										// positive

			double minuscalc = Double.valueOf(minusnum);
			double minusPercent = ((double) (minuscalc) / 100.00);
			balance ="- €0" + dec.format(minusPercent);
		} else if (rawPercent < 0 && rawPercent > -100) {
			int minusnum;
			minusnum = Math.abs(-bal);// turns the negative number to
										// positive

			double minuscalc = Double.valueOf(minusnum);
			double minusPercent = ((double) (minuscalc) / 100.00);
			balance = "- €" + dec.format(minusPercent);
		} else if (rawPercent >= 0 && rawPercent < 10) {

			balance = "€0" + dec.format(rawPercent);
		} else {
			balance = "€" + dec.format(rawPercent);

		}
		
		return balance;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
