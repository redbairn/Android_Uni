package com.jaymcd.leap_calc;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class LCWidget extends AppWidgetProvider {

	Context c;
	RemoteViews v;
	private PendingIntent service = null;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
		  
        final Calendar TIME = Calendar.getInstance(); //gets current time 
        TIME.set(Calendar.MINUTE, 0);  
        TIME.set(Calendar.SECOND, 0);  
        TIME.set(Calendar.MILLISECOND, 0);  
  
        final Intent i = new Intent(context, LeapService.class);  
  
        if (service == null)  
        {  
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);  //sets widget as pending intent
        }  
        
        m.set(AlarmManager.RTC, System.currentTimeMillis(), service); //calls service ever 2 seconds

	}
	
	
	@Override  
    public void onDisabled(Context context)  
    {  
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
  
        m.cancel(service);  
    }  


	
}
