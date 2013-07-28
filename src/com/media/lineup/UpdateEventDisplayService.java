package com.media.lineup;

import java.util.Random;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.RemoteViews;
import android.database.Cursor;
import android.preference.*;
import android.content.SharedPreferences;


public class UpdateEventDisplayService extends Service {
  private static final String tag = "com.media.lineup.Service";
  private int widgetId = 0;
  private RemoteViews remoteViews;
  private static Intent self;
  private static DateFormat dateNowFormat = new SimpleDateFormat("HH:mm");
  private SharedPreferences settings = null;
  private static Boolean events_updated;

  public static Intent getSingleton(Context context){
    if ( self == null )
       self = new Intent(context, UpdateEventDisplayService.class);
    return self;
  }
  public static void setEventsUpdated(){
    events_updated = true;
  }

  @Override
  public void onStart(Intent intent, int startId) {
    //Log.i(tag, "onStart");
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
    if ( widgetId == 0 ){
      int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
      if ( allWidgetIds == null ){
        //Log.w(tag," invalid intent extras" );
        return;
      }
      ComponentName thisWidget = new ComponentName(getApplicationContext(),LineupProvider.class);
      int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
      if ( allWidgetIds2 == null ){
        Log.w(tag," invalid widget ids" );
        return;
      }

      widgetId = allWidgetIds[0];
      remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(),R.layout.widget_layout);

      // Register an onClickListener
      Intent clickIntent = new Intent(this.getApplicationContext(),SelectActivity.class);
      clickIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
      PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, clickIntent, 0);
      remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
    }

    updateView();

    super.onStart(intent, startId);
    stopSelf();

  }
  public void updateView(){
      if ( remoteViews == null ) return;
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
      DateFormat ddateFormat = new SimpleDateFormat("y/M/d HH:mm:ss");
      settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
      Boolean future_time = settings.getBoolean("future_time", true);
      int widget_event_count = 3;
      try{
        widget_event_count = Integer.parseInt(settings.getString("widget_event_count", "3"));
      }catch(NumberFormatException e){}
      if ( widget_event_count == 0 ) widget_event_count = 3;
      if ( widget_event_count > 8 ) widget_event_count = 8;


      ModelAdapter.setNow();
      ModelAdapter.setfDeltaFutureTime(future_time);
      Date now_date = new Date(ModelAdapter.now * 1000L);
      // update the current time
      remoteViews.setTextViewText(R.id.now,dateNowFormat.format(now_date));
      int max_futures = widget_event_count+1;

      // fetch future&past events
      ModelAdapter m = new ModelAdapter(this);
      m.openToRead();
      class WidgetItem{
        String distance;
        String text;
        int imported;
        public WidgetItem(String distance, String text, int imported){
          this.distance = distance;
          this.text = text;
          this.imported = imported;
        }
      };
      ArrayList<WidgetItem> updates = new ArrayList<WidgetItem>();
      Cursor events;
      events= m.queryPast(1);
      int index_begin = events.getColumnIndex("begin");
      int index_end = events.getColumnIndex("end");
      int index_display_text = events.getColumnIndex("display_text");
      int index_imported = events.getColumnIndex("imported");
      for(events.moveToFirst(); !(events.isAfterLast()); events.moveToNext()){
        String sdelta = ModelAdapter.fDelta(events.getLong(index_begin));
        updates.add(new WidgetItem(sdelta, events.getString(index_display_text), events.getInt(index_imported)));
      }
      if ( updates.size() == 0 )
        updates.add(new WidgetItem("","",1));
      events.close();
      events= m.queryFutures(max_futures);
      for(events.moveToFirst(); !(events.isAfterLast()); events.moveToNext()){
        String sdelta = ModelAdapter.fDelta(events.getLong(index_begin));
        updates.add(new WidgetItem(sdelta, events.getString(index_display_text), events.getInt(index_imported)));
        /*
        // set vibrating alarm
        if ( events_updated ){
          AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
          Intent alarm_receiver=new Intent(context, AlarmReceiver.class);
          alarm_receiver.setExtra("event_id",event.get("event_id"));
          service=PendingIntent.getBroadcast(context, 0, alarm_receiver, 0);
          alarm.set(AlarmManager.RTC_WAKEUP,Long.valueOf(events.getInt(index_begin)) * 1000,service);
          events_updated = false;
        }
        */
      }
      events.close();
      // fix the updates for to less events
      for (int i = updates.size(); i < 10; i++ ){
        updates.add(new WidgetItem("","",1));
      }
      int event_index = 0;
      int widget_color = android.graphics.Color.WHITE;
      Map<Integer,Integer> widgets = new HashMap<Integer,Integer>();
      widgets.put(0,R.id.event_0);
      widgets.put(1,R.id.event_next);
      widgets.put(2,R.id.event_1);
      widgets.put(3,R.id.event_2);
      widgets.put(4,R.id.event_3);
      widgets.put(5,R.id.event_4);
      widgets.put(6,R.id.event_5);
      widgets.put(7,R.id.event_6);
      widgets.put(8,R.id.event_7);
      widgets.put(9,R.id.event_8);

      // update the texts in the widget
      for(WidgetItem update: updates){
        if ( update.imported == 0 )
          widget_color=android.graphics.Color.YELLOW;
        else
          widget_color=android.graphics.Color.WHITE;
        if ( event_index > widget_event_count ){
          update.distance="";
          update.text="";
          remoteViews.setViewVisibility(widgets.get(event_index),View.GONE);
        }else
          remoteViews.setViewVisibility(widgets.get(event_index),View.VISIBLE);
        remoteViews.setTextViewText(widgets.get(event_index), update.distance + " " + update.text );
        remoteViews.setTextColor(widgets.get(event_index),widget_color);
        if ( event_index == 1 ){
          remoteViews.setTextViewText(R.id.event_next,  update.distance);
          remoteViews.setTextViewText(R.id.event_next_info, update.text );
          remoteViews.setTextColor(R.id.event_next,widget_color);
        }
        event_index++;
      }
      m.close();
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
  }
  @Override
  public IBinder onBind(Intent intent) {
    //Log.w(tag, "onBind");
    return null;
  }
}

