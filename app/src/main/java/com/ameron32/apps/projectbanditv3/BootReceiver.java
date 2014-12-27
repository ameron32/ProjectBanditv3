package com.ameron32.apps.projectbanditv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends
    BroadcastReceiver {
  
  private static final String  TAG   = "BootReceiver";
  private static final boolean TOAST = false;
  private static final boolean LOG   = true;

  private Context             context;

  @Override
  public void onReceive(Context context, Intent intent) {
    this.context = context;
    
    Intent chatServiceIntent = ChatService.makeIntent(context);
    context.startService(chatServiceIntent);
    Log.d(TAG, "service started");
    
    context = null;
  }

  private void toastAndLog(String message) {
    if (LOG) Log.d(TAG, message);
    if (TOAST) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
}
