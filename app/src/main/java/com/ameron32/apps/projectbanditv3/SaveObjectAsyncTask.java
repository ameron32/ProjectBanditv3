package com.ameron32.apps.projectbanditv3;

import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.List;

public class SaveObjectAsyncTask extends AsyncTask<ParseObject, Void, List<ParseObject>> {
  
  private static boolean LOGGY = false;
  
  private final OnSaveCallbacks listener;
  
  public SaveObjectAsyncTask(
      OnSaveCallbacks listener) {
    this.listener = listener;
    listener.onBegin();
  }

  private boolean sendMessage(
      ParseObject object) {
    int saveObject = 0;
    if (LOGGY) {
      saveObject = Loggy.start("saveObject");
    }
    
    try {
      object.save();
      if (LOGGY)
        Loggy.stop(saveObject);
    } catch (ParseException e) {
      e.printStackTrace();
      if (LOGGY)
        Loggy.stop(saveObject);
      return false;
    }
    
    // return success
    return true;
  }
  
  @Override
	protected void onPostExecute(List<ParseObject> result) {
		super.onPostExecute(result);
	    listener.onComplete();
	}
  
  public interface OnSaveCallbacks {
    
    public void onBegin();
    
    public void onComplete();
  }



@Override
protected List<ParseObject> doInBackground(ParseObject... params) {
	for (ParseObject o : params) {
		try {
			o.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	return Arrays.asList(params);
}
}
