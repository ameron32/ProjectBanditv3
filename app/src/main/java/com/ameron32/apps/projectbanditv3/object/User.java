package com.ameron32.apps.projectbanditv3.object;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("_User") public class User
    extends ParseUser {

  protected static final String TAG = User.class.getSimpleName();

  public User() {}

  public Character getLastChatCharacter() {
    Character lastChatCharacter = (Character) this.getParseObject("lastChatCharacter");
//    if (lastChatCharacter == null) {
//      lastChatCharacter = Character.getFromName("Spectator");
//    }
    return lastChatCharacter;
  }

  public void setLastChatCharacter(
      Character chatCharacter) {
    this.put("lastChatCharacter", chatCharacter);
    this.saveInBackground(new SaveCallback() {

      @Override public void done(
          ParseException e) {
        if (e == null) {
          Log.i(TAG, "lastChatCharacter saved.");
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  public CAction getLastCharacterAction() {
    CAction characterAction = (CAction) this.getParseObject("lastCharacterAction");
    return characterAction;
  }

  public void setLastCharacterAction(
      CAction characterAction) {
    this.put("lastCharacterAction", characterAction);
    this.saveInBackground();
  }

  private boolean isIdEquals(User user) {
    if (this.getObjectId().equalsIgnoreCase(user.getObjectId())) { return true; }
    return false;
  }

  @Override public boolean equals(
      Object o) {
    if (o instanceof User) {
      return this.equals((User) o);
    } else {
      return false;
    }
  }

  public String getName() {
    return this.getString("username");
  }

  public boolean isTester() {
    return this.getBoolean("isTester");
  }

  public boolean isDataAdmin() {
    return this.getBoolean("isDataAdmin");
  }

  public boolean equals(
      User user) {
    return this.isIdEquals(user);
  }
}
