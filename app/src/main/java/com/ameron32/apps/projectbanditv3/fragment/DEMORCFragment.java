package com.ameron32.apps.projectbanditv3.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.fragment.AbsResettingContentFragment.OnPerformTaskListener;
import com.ameron32.apps.projectbanditv3.fragment.AbsResettingContentFragment.TaskWorker;
import com.ameron32.apps.projectbanditv3.R;

public class DEMORCFragment extends
    AbsResettingContentFragment implements OnPerformTaskListener, TaskWorker {
//  
//  @Override public View onCreateView(
//      LayoutInflater inflater,
//      ViewGroup container,
//      Bundle savedInstanceState) {
//    return inflater.inflate(R.layout.fragment____demo___resetting, container, false);
//  }
  
  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment____demo___resetting;
  }
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public int provideClickViewId() {
    return R.id.button1;
  }

  @Override public OnPerformTaskListener provideOnPerformTaskListener() {
    return this;
  }

  @Override public TaskWorker provideTaskWorker() {
    return this;
  }

  @Override public void onPrePerformTask() {
    toast("starting");
    ((Button) getActivity().findViewById(R.id.button1)).setText("...Acting...");
  }
  
  @Override public void doTaskInBackground() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override public void onPostPerformTask() {
    toast("complete");
  }
  
  private void toast(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

}
