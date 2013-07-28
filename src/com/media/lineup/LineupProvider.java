package com.media.lineup;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.app.PendingIntent;
import android.app.AlarmManager;
import android.os.SystemClock;
import java.util.Calendar;
import android.preference.*;
import android.content.SharedPreferences;

public class LineupProvider extends AppWidgetProvider {

  private static final String LOG = "com.media.lineup.LineupProvider";
  private PendingIntent service;
  private SharedPreferences settings = null;


  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // Get all ids
    settings = PreferenceManager.getDefaultSharedPreferences(context);
    int widget_update_time = 10;
    try{
    widget_update_time = Integer.parseInt(settings.getString("widget_update_time", "10"));
    }catch(NumberFormatException e){}
    if ( widget_update_time == 0 ) widget_update_time = 10;
    ComponentName thisWidget = new ComponentName(context, LineupProvider.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

    // Build the intent to call the service
    Intent intent = UpdateEventDisplayService.getSingleton(context.getApplicationContext());
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

    // Update the widgets via the service
    context.startService(intent);

    // update the widget every minute

    AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    Intent update_receiver=new Intent(context, WidgetUpdateReceiver.class);
    service=PendingIntent.getBroadcast(context, 0, update_receiver, 0);
    alarm.setRepeating(AlarmManager.RTC,
                       System.currentTimeMillis()+500, // first invoke
                       widget_update_time * 1000 ,     // repeated invoke
                       service);
    // service has to be registered in Manifest, otherwise events wont show
    //Log.w(LOG, "Update set");
  }
  @Override
  public void onDisabled(Context context) {
    final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Log.w(LOG,"onDisabled"); 
    alarm.cancel(service);
  }
  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    //Log.w(LOG,"onReceive");
  }
}