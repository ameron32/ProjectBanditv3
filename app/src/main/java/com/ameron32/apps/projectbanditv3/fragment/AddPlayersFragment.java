package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.view.MultiSelectObjectSpinner;
import com.ameron32.apps.projectbanditv3.view.MultiSelectObjectSpinner.Item;
import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.Game;
import com.ameron32.apps.projectbanditv3.object.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;



public class AddPlayersFragment extends
    AbsResettingContentFragment
    implements
    AbsResettingContentFragment.OnPerformTaskListener,
    AbsResettingContentFragment.TaskWorker {

  
  
  @InjectView(R.id.button_save) Button saveButton;
  @InjectView(R.id.mss_query_results)
  MultiSelectObjectSpinner<User> mPlayers;
  @InjectView(R.id.edittext_filter) EditText mFilter;
  
  
  
  @Override protected int getCustomLayoutResource() {
    return R.layout.view_filtered_multiselect;
  }
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);

    loadMSS();
    mFilter.setOnFocusChangeListener(new OnFocusChangeListener() {
      
      @Override public void onFocusChange(
          View v, boolean hasFocus) {
        if (!hasFocus
            && (v.getId() == R.id.edittext_filter)) {
          loadMSS();
        }
      }
    });
  }
  
  private void loadMSS() {
    final String filter = mFilter.getText().toString();
    Query._User.getAllUsers()
        .whereStartsWith("username", filter)
        .findInBackground(new FindCallback<User>() {
      
      @Override public void done(
          List<User> users,
          ParseException e) {
        if (e == null) {
//          List<String> strings = Util.toListOfStrings(users, "username");
          mPlayers.setItems(makeItems(users));
        }
      }
    });
  }
  
  private List<Item<User>> makeItems(List<User> users) {
    List<Item<User>> items = new ArrayList<Item<User>>();
    for (int i = 0; i < users.size(); i++) {
      User user = users.get(i);
      Item<User> userWrapper = new Item<User>(user.getUsername(), user);
      items.add(userWrapper);
    }
    return items;
  }
  
  @Override public int provideClickViewId() {
    return R.id.button_save;
  }
  
  @Override public OnPerformTaskListener provideOnPerformTaskListener() {
    return this;
  }
  
  @Override public TaskWorker provideTaskWorker() {
    return this;
  }
  
  @Override public void doTaskInBackground() {
    try {
      List<String> selectedStrings = mPlayers.getSelectedStrings();
      List<User> users = ParseQuery.getQuery(User.class).whereContainsAll("username", selectedStrings).find();
      Game currentGame = GameManager.get().getCurrentGame();
      for (User u : users) {
        Util.addPlayerToGame(u, currentGame);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
  
  @Override public void onPrePerformTask() {
    Toast.makeText(getActivity(), "Saving...", Toast.LENGTH_SHORT).show();
  }
  
  @Override public void onPostPerformTask() {
    Toast.makeText(getActivity(), "Players added to Game.", Toast.LENGTH_SHORT).show();
  }
}
