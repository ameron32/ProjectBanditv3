package com.ameron32.apps.projectbanditv3;

import android.os.AsyncTask;

import com.ameron32.apps.projectbanditv3.fragment.AbsResettingContentFragment.TaskWorker;

/**
 * DESIGNED FOR USE with ResettingContentFragment
 * Simple AsyncTask extension for running a TaskWorker in a background thread
 */
public class ResettingFragmentAsyncTask extends AsyncTask<Void, Void, Void> {
  
  private TaskWorker worker;
  private FragmentTaskCallbacks callbacks;
  
  public ResettingFragmentAsyncTask(TaskWorker worker, FragmentTaskCallbacks callbacks) {
    this.worker = worker;
    this.callbacks = callbacks;
  }

  @Override protected Void doInBackground(
      Void... arg0) {
    if (worker != null) {
      worker.doTaskInBackground();
    }
    return null;
  }
  
  @Override protected void onPostExecute(
      Void result) {
    super.onPostExecute(result);
    callbacks.onTaskComplete();
  }
  
  public interface FragmentTaskCallbacks {
    public void onTaskComplete();
  }
}