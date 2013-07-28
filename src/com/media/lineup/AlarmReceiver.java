package com.media.lineup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
  private static Intent service;
  @Override
  public void onReceive(Context context, Intent intent) {
    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    // Vibrate for 300 milliseconds
    v.vibrate(300);
  }
}

