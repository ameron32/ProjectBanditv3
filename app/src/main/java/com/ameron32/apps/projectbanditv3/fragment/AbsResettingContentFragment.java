package com.ameron32.apps.projectbanditv3.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ameron32.apps.projectbanditv3.ResettingFragmentAsyncTask;

import java.security.InvalidParameterException;


/**
 * EXTEND THIS FRAGMENT to have a worker-thread runnable interface
 * MUST OVERRIDE defineClickViewId() to enable the clickable view
 * MUST call setTaskWorker() to implement a task to perform in background thread
 * OPTIONALLY may call setOnPerformTaskListener() to tap into workcycle
 */
public abstract class AbsResettingContentFragment
    extends AbsContentFragment
    implements ResettingFragmentAsyncTask.FragmentTaskCallbacks
{
  
  private static final String TAG = AbsResettingContentFragment.class.getSimpleName();
  
  
  
  private OnResetCallbacks mCallbacks;
  
  @Override public void onAttach(
      Activity activity) {
    super.onAttach(activity);
    try {
      mCallbacks = (OnResetCallbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity must implement OnResetCallbacks.");
    }
  }
  
  @Override public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeClickView();
    setOnPerformTaskListener(provideOnPerformTaskListener());
    setTaskWorker(provideTaskWorker());
  }
  
  private View mClickView;
  
  /**
   * IMPORTANT: Must define a ClickView resource id that can be found within the Fragment's rootview
   */
  
  private void initializeClickView() {
    View testView = getView().findViewById(provideClickViewId());
    if (testView == null) {
      final String eMessage = "viewResourceId could not be found in this fragment's rootview";
      throw new InvalidParameterException(eMessage);
    }
    mClickView = testView;
    mClickView.setOnClickListener(new View.OnClickListener() {
      
      @Override public void onClick(View v) {
        onClickViewClick(v);
      }
    });
  }
  
  public abstract int provideClickViewId();
  public abstract OnPerformTaskListener provideOnPerformTaskListener();
  public abstract TaskWorker provideTaskWorker();
  
  protected void onClickViewClick(View v) {
    if (listener != null) {
      listener.onPrePerformTask();
    } else { Log.d(TAG, "OnPerformTaskListener is null"); }
    if (worker != null) {
      new ResettingFragmentAsyncTask(worker, this).execute();
    } else { Log.d(TAG, "TaskWorker is null. Skipping doTask"); }
  }
  
  public void onTaskComplete() {
    if (listener != null) {
      listener.onPostPerformTask();
    }
    mCallbacks.onRequestReset(this);
  }
  
  private OnPerformTaskListener listener;
  private TaskWorker worker;
  
  public interface OnPerformTaskListener {
    public void onPrePerformTask();
    public void onPostPerformTask();
  }
  
  public interface TaskWorker {
    public void doTaskInBackground();
  }
  
  private void setOnPerformTaskListener(OnPerformTaskListener listener) {
    this.listener = listener;
  }
  
  private void setTaskWorker(TaskWorker worker) {
    this.worker = worker;
  }
  
  public interface OnResetCallbacks {
    public void onRequestReset(AbsResettingContentFragment fragment);
  }
}
