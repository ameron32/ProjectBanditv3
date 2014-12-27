package com.ameron32.apps.projectbanditv3;

import android.app.Application;

import com.ameron32.apps.projectbanditv3.object.*;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseApplication extends
    Application {
  
  private static String YOUR_APPLICATION_ID;
  private static String YOUR_CLIENT_KEY;
//  private static Context applicationContext;
  
  @Override public void onCreate() {
    super.onCreate();
//    applicationContext = getApplicationContext();
    
    YOUR_APPLICATION_ID = getResources().getString(R.string.parse_application_id);
    YOUR_CLIENT_KEY = getResources().getString(R.string.parse_client_key);
    
    // Add your initialization code here
    ParseObject.registerSubclass(CAction.class);
    ParseObject.registerSubclass(com.ameron32.apps.projectbanditv3.object.Character.class);
    ParseObject.registerSubclass(CInventory.class);
    ParseObject.registerSubclass(Game.class);
    ParseObject.registerSubclass(Message.class);
    ParseObject.registerSubclass(Item.class);
    ParseObject.registerSubclass(Advantage.class);
    ParseObject.registerSubclass(Skill.class);
    
    ParseObject.registerSubclass(User.class);
    
    Parse.enableLocalDatastore(this);
    Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    
    // Save the current Installation to Parse.
    ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
      
      @Override public void done(
          ParseException e) {
        // PushService.setDefaultPushCallback(
        // ParseApplication.this,
        // ParseStarterProjectActivityUnused.class);
      }
    });

    
    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
    
    setDefaultSettings();
  }
  
  private void setAnonymousUsers() {
    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    
    // If you would like all objects to be private by default, remove this
    // line.
    defaultACL.setPublicReadAccess(true);
    
    ParseACL.setDefaultACL(defaultACL, true);
  }
  
  private void setDefaultSettings() {
    // SharedPreferences preferences = getSharedPreferences("size",
    // Context.MODE_PRIVATE);
    // Editor editor = preferences.edit();
    // editor.putInt("message_row", R.layout.row_message);
    // editor.commit(); // TODO review theme implementation
  }
//  
//  public static Context getContext() {
//    return ParseApplication.applicationContext;
//  }
}
