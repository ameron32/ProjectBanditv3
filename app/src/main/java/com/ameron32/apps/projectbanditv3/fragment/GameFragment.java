package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;

import com.ameron32.apps.projectbanditv3.view.FloatingHintTextView;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.Util;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.manager.UserManager;
import com.ameron32.apps.projectbanditv3.object.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class GameFragment extends
    SectionContainerTestFragment {
  
  private static final String ARG_PARAM1 = "param1";
  
  public static GameFragment newInstance(int param1) {
    GameFragment fragment = new GameFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM1, param1);
    fragment.setArguments(args);
    return fragment;
  }
  
  public GameFragment() {
    // Required empty public constructor
  }
  
  @Override public void onCreate(
      Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getInt(ARG_PARAM1);
    }
  }
  
  @InjectView(R.id.textcomboview_game)
  FloatingHintTextView game;
  @InjectView(R.id.textcomboview_game_gm) FloatingHintTextView gm;
  @InjectView(R.id.textcomboview_game_players) FloatingHintTextView players;
  @InjectView(R.id.textcomboview_username) FloatingHintTextView user;
  
  private int mParam1;
  
  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    
    String gameName = GameManager.get().getCurrentGame().getName();
    int sessionNumber = GameManager.get().getCurrentGame().getCurrentSession();
    game.setText(gameName + " ["
        + sessionNumber + "]");
    
    String username = UserManager.get().getCurrentUser().getString("username");
    user.setText(username);
    
    GameManager.get().getCurrentGame().getGMInBackground(new FindCallback<User>() {
      
      @Override public void done(
          List<User> gmParseUsers,
          ParseException e) {
        if (e == null) {
          gm.setText(Util.displayAsList('\n', gmParseUsers, "username"));
        }
      }
    });
    
    GameManager.get().getCurrentGame().getPlayers(new FindCallback<ParseUser>() {
      
      @Override public void done(
          List<ParseUser> gamePlayers,
          ParseException e) {
        if (e == null) {
          players.setText(Util.displayAsList('\n', gamePlayers, "username"));
        }
      }
    });
  }
  
  @Override protected int onReplaceFragmentLayout(
      int storedLayoutResource) {
    return mParam1;
  }
}
