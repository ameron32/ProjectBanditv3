package com.ameron32.apps.projectbanditv3;


import android.os.AsyncTask;

import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager.OnCharacterManagerInitializationCompleteListener;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager.OnGameManagerInitializationCompleteListener;
import com.ameron32.apps.projectbanditv3.manager.UserManager;
import com.ameron32.apps.projectbanditv3.manager.UserManager.OnUserManagerInitializationCompleteListener;

public class LoadingAsyncTask 
  extends 
    AsyncTask<Void, Boolean, Boolean> 
  implements 
    OnGameManagerInitializationCompleteListener,
    OnCharacterManagerInitializationCompleteListener, 
    OnUserManagerInitializationCompleteListener
{
 
  private OnLoadingListener mListener;
  public LoadingAsyncTask(OnLoadingListener listener) {
    mListener = listener;
  }

  @Override protected Boolean doInBackground(
      Void... params) {

    UserManager.get().initialize(this);
    CharacterManager.get().initialize(this);
    GameManager.get().initialize(this);
    
    return null;
  }
  
  @Override public void onCharacterManagerInitializationComplete() {
    isCharacterDone = true;
    continueIfAllManagersReportDone();
  }
  
  @Override public void onGameManagerInitializationComplete() {
    isGameDone = true;
    continueIfAllManagersReportDone();
  }
  
  @Override public void onUserManagerInitializationComplete() {
    isUserDone = true;
    continueIfAllManagersReportDone();
  }

  public static int CHARACTER = 0;
  public static int GAME = 1;
  public static int USER = 2;
  private volatile boolean isCharacterDone, isGameDone, isUserDone;
  private void continueIfAllManagersReportDone() {
    publishProgress(new Boolean[] { isCharacterDone, isGameDone, isUserDone });
  }
  
  @Override protected void onProgressUpdate(
      Boolean... values) {
    super.onProgressUpdate(values);
    mListener.onLoading(values);
  }
  
  @Override protected void onPostExecute(
      Boolean result) {
    super.onPostExecute(result);
    boolean success = false;
    if (isCharacterDone && isGameDone && isUserDone) {
      success = true;
    }
    mListener.onLoadingComplete(success);
  }
  
  public interface OnLoadingListener {
    public void onLoading(Boolean[] completed);
    public void onLoadingComplete(boolean successful);
  }
}
