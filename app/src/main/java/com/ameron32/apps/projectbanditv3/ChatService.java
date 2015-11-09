package com.ameron32.apps.projectbanditv3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ameron32.apps.projectbanditv3.manager.MessageManager;
import com.ameron32.apps.projectbanditv3.manager.MessageManager.MessageListener;
import com.ameron32.apps.projectbanditv3.object.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO needs revision
 */
public class ChatService extends
    Service implements MessageListener {

  private static final String TAG = ChatService.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = true;

  private static final String NEW_MESSAGE_NOTIFICATION_ID = "77424";
  private static final String QUIET_STATE = "quiet_state";

  public ChatService() {}

  public static Intent makeIntent(
      Context context) {
    return new Intent(context, ChatService.class);
  }

  @Override public IBinder onBind(
      Intent intent) {
    setAppOffToNow();
    return getMyBinder();
  }

  @Override public boolean onUnbind(
      Intent intent) {
    setAppOffToNow();
    return super.onUnbind(intent);
  }

  @Override public int onStartCommand(
      Intent intent, int flags,
      int startId) {
    setAppOffToNow();
    return START_STICKY;
  }

  @Override public void onCreate() {
    super.onCreate();
    if (LOG) {
      Log.i(TAG, "onCreate");
    }
    restoreQuietSetting();
    if (LOG) {
      Log.i(TAG, "quiet is "
          + isQuiet());
    }
    MessageManager.get().addMessageListener(this);
  }

  @Override public void onDestroy() {
    MessageManager.get().removeMessageListener(this);
    if (LOG) {
      Log.i(TAG, "onDestroy");
    }
    storeQuietSetting(quiet);
    super.onDestroy();
  }

  public static long getLastSystemTimeWhenAppOff() {
    return lastSystemTimeWhenAppOff;
  }

  public void setAppState(
      boolean newState) {
    lock.writeLock().lock();
    try {
      this.appState = newState;
      if (LOG)
        Log.d(TAG, "appState is now: "
            + appState);

      if (newState == APP_OFF) {
        setAppOffToNow();
      }
    } finally {
      lock.writeLock().unlock();
    }

//    resetUnreadCount();
    updateQuiet();
  }

  public boolean isQuiet() {
    boolean isQuiet = false;
    lock.readLock().lock();
    try {
      isQuiet = quiet;
    } finally {
      lock.readLock().unlock();
    }
    return isQuiet;
  }

  public void setQuiet(boolean isQuiet) {
    lock.writeLock().lock();
    try {
      this.quiet = isQuiet;
      if (LOG)
        Log.d(TAG, "quiet is now: "
            + quiet);
    } finally {
      lock.writeLock().unlock();
    }
    storeQuietSetting(quiet);
  }

  private final Context context = this;
  private boolean quiet = false;
  public class MyBinder extends Binder {

    public ChatService getService() {
      return ChatService.this;
    }
  }

  @Override public void onMessageReceived() {
    boolean willSend = !isQuiet();
//    incrementUnreadCount();
    if (LOG)
      Log.d(TAG, "willSend is "
          + willSend);
    if (willSend) {
      queryNotificationDetails();
    }
  }

  private final IBinder myBinder = new MyBinder();

  protected IBinder getMyBinder() {
    return myBinder;
  }

  private void queryNotificationDetails() {
    ParseQuery<Message> query = Query._Message.getNotificationQuery();

    query.findInBackground(new FindCallback<Message>() {

      @Override public void done(
          List<Message> unreadMessages,
          ParseException e) {
        if (e == null) {
          List<String> mstr = new ArrayList<String>();
          for (Message m : unreadMessages) {
            String shortName = m.getParseObject("ofGame").getString("shortName");
            mstr.add(shortName + m.toString());
          }

          putNotification(NEW_MESSAGE_NOTIFICATION_ID, mstr);
          setSilentFor10SecondsThenUnsilence();
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  private void putNotification(
      String id,
      List<String> unreadMessageTexts) {
    if (unreadMessageTexts.size() == 0) {
      // DO NOTHING
      return;
    }

    // unreadMessageTexts > 0
    NewMessageNotification.notify(context, unreadMessageTexts.toArray(new String[unreadMessageTexts.size()]));
  }

  private void setSilentFor10SecondsThenUnsilence() {
    final int silentForDelayInSeconds = 10;
    final int silentForDelayInMillis = silentForDelayInSeconds * 1000;

    final Thread delay = new Thread(new Runnable() {

      @Override public void run() {
        try {
          setQuiet(true);
          Thread.sleep(silentForDelayInMillis);
          if (LOG) {
            Log.i(TAG, "sleep end");
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        updateQuiet();
      }
    });
    delay.start();
  }

  ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);

  private void updateQuiet() {
    if (getAppState() == APP_ON) {
      setQuiet(true);
    }
    if (getAppState() == APP_OFF) {
      setQuiet(false);
    }

    if (LOG) {
      Log.i(TAG, "updated quiet state to: "
          + isQuiet());
      Log.i(TAG, "appState was: "
          + getAppState());
    }
  }

  public static final boolean APP_OFF = false;
  public static final boolean APP_ON = true;
  private boolean appState = APP_OFF;

  private boolean getAppState() {
    boolean appState = APP_OFF;
    lock.readLock().lock();
    try {
      appState = this.appState;
    } finally {
      lock.readLock().unlock();
    }
    return appState;
  }

//  private int unreadCount = 0;
  private static volatile long lastSystemTimeWhenAppOff;

  private void setAppOffToNow() {
    lastSystemTimeWhenAppOff = System.currentTimeMillis();
  }
//
//  private void resetUnreadCount() {
//    unreadCount = 0;
//  }
//
//  private void incrementUnreadCount() {
//    unreadCount++;
//  }

  private void restoreQuietSetting() {
    SharedPreferences prefs = context.getSharedPreferences("ChatService", Context.MODE_PRIVATE);
    quiet = prefs.getBoolean(QUIET_STATE, true);
  }

  private void storeQuietSetting(
      boolean quiet) {
    Editor editor = context.getSharedPreferences("ChatService", Context.MODE_PRIVATE).edit();
    editor.putBoolean(QUIET_STATE, quiet);
    editor.commit();
  }

}
