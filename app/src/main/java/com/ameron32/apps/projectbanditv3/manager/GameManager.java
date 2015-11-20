package com.ameron32.apps.projectbanditv3.manager;

import android.util.Log;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.object.Game;
import com.ameron32.apps.projectbanditv3.object.TileMap;
import com.ameron32.apps.projectbanditv3.object.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class GameManager extends AbsManager {

  private static final String TAG = GameManager.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = false;

  private static GameManager gameManager = null;

  public static GameManager get() {
    if (gameManager == null) {
      gameManager = new GameManager();
    }
    return gameManager;
  }

  public static void destroy() {
    gameManager = null;
  }

  private Game currentGame;
  private TileMap currentTileMap;
  private boolean isCurrentUserGM = false;

  protected GameManager() {}

  public void initialize(final OnGameManagerInitializationCompleteListener listener) {
    initialize();
    if (listener != null) {
      listener.onGameManagerInitializationComplete();
    }
  }

  public void initialize() {
    try {
      List<User> gamemasters = currentGame.getGM();
      if (LOG) {
        Log.i(TAG, "initializing isCurrentUserGM");
      }
      isCurrentUserGM = false;
      for (User user : gamemasters) {
        if (LOG) {
          Log.i(TAG, "user: "
              + user.getObjectId()
              + " / currentUser: "
              + UserManager.get().getCurrentUser().getObjectId());
        }
        if (user.equals(UserManager.get().getCurrentUser())) {
          isCurrentUserGM = true;
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    setInitialized(true);
  }

  public void changeGame(
      Game newGame) {
    currentGame = newGame;
    ParseObject map = currentGame.getParseObject("currentTileMap");
    if (map != null) {
      currentTileMap = (TileMap) map;
      currentTileMap.fetchInBackground();
    }
  }

  public void selectAGame(
      FindCallback<Game> callback) {
    Query._Game.getCurrentGamesQuery().findInBackground(callback);
  }

  public boolean isCurrentUserGM() {
    return isCurrentUserGM;
  }

  public Game getCurrentGame() {
    // if (currentGame == null) {
    // throw new
    // IllegalStateException("Must initialize a game. Use GameManager#initialize.");
    // }
    return currentGame;
  }

  public TileMap getCurrentMap() {
    return (TileMap) getCurrentGame().get("currentTileMap");
  }

  public interface OnGameManagerInitializationCompleteListener {
    public void onGameManagerInitializationComplete();
  }
}
