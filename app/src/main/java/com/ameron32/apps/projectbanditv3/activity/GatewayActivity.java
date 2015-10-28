package com.ameron32.apps.projectbanditv3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.LoadingAsyncTask;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.adapter.GameListAdapter;
import com.ameron32.apps.projectbanditv3.adapter.GameListAdapter.GameChangeListener;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.ContentManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.manager.MessageManager;
import com.ameron32.apps.projectbanditv3.manager.ObjectManager;
import com.ameron32.apps.projectbanditv3.manager.UserManager;
import com.ameron32.apps.projectbanditv3.object.Game;
import com.ameron32.apps.projectbanditv3.parseui.LoginActivity;
import com.crashlytics.android.Crashlytics;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class GatewayActivity extends
    AppCompatActivity implements
    GameChangeListener {

  private static final String TAG = GatewayActivity.class.getSimpleName();
  private static final Class<ExpandedCoreActivity> PRIMARY_ACTIVITY = ExpandedCoreActivity.class;

  private RecyclerView mRecyclerView;

  private Activity getActivity() {
    return GatewayActivity.this;
  }

  @Override protected void onCreate(
      Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_gateway);

    mRecyclerView = (RecyclerView) findViewById(R.id.lv_game_list);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setHasFixedSize(true);
  }

  @Override protected void onResume() {
    super.onResume();

    /**
     * IF USER IS ALREADY LOGGED IN, CONTINUE TO MAIN ACTIVITY. IF USER IS NOT
     * LOGGED IN, GOTO LOGIN ACTIVITY. WILL GO TO MAIN ACTIVITY AFTER SUCCESSFUL
     * LOGIN. WILL ABORT--finish()--IF CANCELLED.
     */
    if (!UserManager.get().isLoggedIn()) {
      UserManager.destroy();
      GameManager.destroy();
      CharacterManager.destroy();
      ObjectManager.destroy();
      MessageManager.destroy();
      ContentManager.destroy();
      startLoginActivity();
      // not logged in. exit onResume()
      return;
    }

    // logged in, continue
    loadGame();
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * LOGIN ACTIVITY RELATED SECTION
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  private static final int LOGIN_REQUEST_CODE = 4647;

  private void startLoginActivity() {
    startActivityForResult(new Intent(GatewayActivity.this, LoginActivity.class), LOGIN_REQUEST_CODE);
    // exit to ParseLogin
  }

  @Override protected void onActivityResult(
      int requestCode, int resultCode,
      Intent arg2) {
    // return from ParseLogin
    if (requestCode == LOGIN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        loadGame();
      }
    }
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * GAME LOADING AND SELECTION RELATED SECTION
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  public volatile boolean lock = false;

  private void loadGame() {
    // use local current game, if possible
    if (GameManager.get().getCurrentGame() != null) {
      // reload current game
      Log.i(TAG, "restoring game from GameManager.");
      oneGame(GameManager.get().getCurrentGame());
      // restore single game from local data
      return;
    }

    // use local list of games, if possible
    if (UserManager.get().getStoredGamesOfCurrentUser() != null
        && !(UserManager.get().getStoredGamesOfCurrentUser().isEmpty())) {
      Log.i(TAG, "loading local list of available games.");
      multipleGames(UserManager.get().getStoredGamesOfCurrentUser());
      // display a list of games from local data
      return;
    }

    // LAST RESORT: no games stored, pull from server
    pullGamesFromServer();
  }

  /**
   * TODO: add a refresh process to the Gateway Activity
   * Button or Pull-to-Refresh
   */
  private void pullGamesFromServer() {
    Log.i(TAG, "pulling Games from Server.");

    // LOAD GAMES FROM SERVER
    // only one load should pass through lock
    // prevents simultaneous racing loads
    if (!lock) {
      lock = true;

      GameManager.get().selectAGame(new FindCallback<Game>() {

        @Override public void done(
            List<Game> games,
            ParseException e) {
          lock = false;
          if (e == null) {
            switch (games.size()) {
            case 0:
              noGames();
              break;
            case 1:
              oneGame(games.get(0));
              break;
            default:
              multipleGames(games);
            }
          }
        }
      });
    }
  }

  private void noGames() {
    // do not continueToStructureActivity() without a game
    Log.i(TAG, "game returned with no results.");
    final String message = "You are not registered for any games. Contact an administrator.";
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    /**
     * STOP!
     */
    // TODO: create a process to allow a User to:
    // -- request a game OR
    // -- start their own
  }

  private void oneGame(Game game) {
    // select this game, transparently
    Log.i(TAG, "game returned with one result.");
    List<Game> games = new ArrayList<Game>();
    games.add(game);
    UserManager.get().setGamesOfCurrentUser(games);
    changeGame(game);
  }

  private void multipleGames(
      final List<Game> games) {
    // offer a GameList for the User to choose which game to commit
    Log.i(TAG, "game returned with several results.");
    UserManager.get().setGamesOfCurrentUser(games);
    GameListAdapter mAdapter = new GameListAdapter(games, this);
    mRecyclerView.setAdapter(mAdapter);
    // TODO: convert OnClickListener within GameListAdapter to
    // OnItemClickListener here
  }

  @Override public void onGameChange(
      Game game) {
    changeGame(game);
  }

  private void changeGame(Game game) {
    GameManager.get().changeGame(game);
    performLoading();
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * PRE-ACTIVITY LOADING SECTION
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  private void performLoading() {
    // PERFORM PRE-ACTIVITY INITIALIZATIONS OF SINGLETON MANAGERS
    new LoadingAsyncTask(new LoadingAsyncTask.OnLoadingListener() {

      @Override public void onLoadingComplete(
          boolean successful) {
        if (!successful) {
          Toast.makeText(getActivity(), "Initialization failed.", Toast.LENGTH_SHORT).show();
          return;
        }

        // successful
        allInitializationsComplete();
      }

      @Override public void onLoading(
          Boolean[] completed) {
        final String m = "onLoading(): " + completed[0] +","+ completed[1] +","+ completed[2];
        Log.d(LoadingAsyncTask.class.getSimpleName(), m);
      }
    }).execute();
  }

  private void allInitializationsComplete() {
    /*
     * MOVE to PrimaryActivity after loading
     */
    Intent beginStructureActivity = new Intent(getActivity(), PRIMARY_ACTIVITY);
    startActivity(beginStructureActivity);
    finish();
  }
}
