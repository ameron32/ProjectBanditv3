package com.ameron32.apps.projectbanditv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.manager.MessageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class AppReceiver extends
    BroadcastReceiver {
  
  private static final String  TAG   = "AppReceiver";
  private static final boolean TOAST = false;
  private static final boolean LOG   = true;
  
  private Context              context;

  @Override
  public void onReceive(Context context, Intent intent) {
    this.context = context;
    
    String action = intent.getAction();
    String channel = intent.getExtras().getString("com.parse.Channel");
    
    String message = "got action " + action + " on channel " + channel;
    toastAndLog(message);
    
    try {
      String jsonStr = intent.getExtras().getString("com.parse.Data");
      JSONObject json = null;
      if (jsonStr == null) {
        Log.d(TAG, "jsonStr was null");
      }
      else {
        json = new JSONObject(jsonStr);
      }
      
      if (json != null) {
        Iterator itr = json.keys();
        while (itr.hasNext()) {
          String key = (String) itr.next();
          message = "..." + key + " => " + json.getString(key);
          Log.d(TAG, message);
        }
      }
      else {
        Log.d(TAG, "json was null");
      }
    }
    catch (JSONException e) {
      Log.d(TAG, "JSONException: " + e.getMessage());
    }
    
    MessageManager.get().notifyMessageReceived();
    // NewMessageNotification.notify(ParseApplication.getContext(),
    // "New Message", 987);

    context = null;
  }

  private void toastAndLog(String message) {
    if (LOG) Log.d(TAG, message);
    if (TOAST) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
  
}
