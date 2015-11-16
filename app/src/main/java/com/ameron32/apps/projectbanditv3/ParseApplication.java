package com.ameron32.apps.projectbanditv3;

import android.app.Application;
import android.util.Log;

import com.ameron32.apps.projectbanditv3.object.Advantage;
import com.ameron32.apps.projectbanditv3.object.CAction;
import com.ameron32.apps.projectbanditv3.object.CInventory;
import com.ameron32.apps.projectbanditv3.object.Game;
import com.ameron32.apps.projectbanditv3.object.Item;
import com.ameron32.apps.projectbanditv3.object.Message;
import com.ameron32.apps.projectbanditv3.object.Skill;
import com.ameron32.apps.projectbanditv3.object.Token;
import com.ameron32.apps.projectbanditv3.object.User;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import timber.log.Timber.DebugTree;


import timber.log.Timber;

public class ParseApplication extends
    Application {

  private static String YOUR_APPLICATION_ID;
  private static String YOUR_CLIENT_KEY;
//  private static Context applicationContext;

  @Override public void onCreate() {
    super.onCreate();
//    applicationContext = getApplicationContext();

    JodaTimeAndroid.init(this);

    YOUR_APPLICATION_ID = getResources().getString(R.string.parse_application_id);
    YOUR_CLIENT_KEY = getResources().getString(R.string.parse_client_key);

    registerSubclasses();

    Parse.enableLocalDatastore(this);
//    Stetho.initialize(
//        Stetho.newInitializerBuilder(this)
//            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//            .build());
//    Parse.addParseNetworkInterceptor(new ParseStethoInterceptor());
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
    plantTimberLogging();

    setDefaultSettings();
  }

    private void registerSubclasses() {
      // Add your initialization code here
      ParseObject.registerSubclass(CAction.class);
      ParseObject.registerSubclass(Character.class);
      ParseObject.registerSubclass(CInventory.class);
      ParseObject.registerSubclass(Game.class);
      ParseObject.registerSubclass(Message.class);
      ParseObject.registerSubclass(Item.class);
      ParseObject.registerSubclass(Advantage.class);
      ParseObject.registerSubclass(Skill.class);
      ParseObject.registerSubclass(Token.class);

      ParseObject.registerSubclass(User.class);
    }

    private void plantTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
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





    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override public void i(String message, Object... args) {
            Crashlytics.log(Log.DEBUG,
                    ParseApplication.class.getSimpleName(),
                    String.format(message, args));
        }

        @Override public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override public void e(Throwable t, String message, Object... args) {
            e(message, args);
            Crashlytics.logException(t);
        }
    }
}
