package com.media.lineup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetUpdateReceiver extends BroadcastReceiver {
  private static String tag = "com.media.lineup.WidgetUpdateReceiver";
  @Override
  public void onReceive(Context context, Intent intent) {
    //Log.i(tag,"onReceive");
    context.startService(UpdateEventDisplayService.getSingleton(context));
  }
}

