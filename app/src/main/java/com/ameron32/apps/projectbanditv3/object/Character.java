package com.ameron32.apps.projectbanditv3.object;

import android.util.Log;

import com.ameron32.apps.projectbanditv3.SaveObjectSerialExecutor;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Set;


@ParseClassName("Character")
public class Character
  extends AbsBanditObject<AbsBanditObject.Column>
{
  private static final String TAG = Character.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = true;

  private String name = "Nameless";
  private int currentHealth = 0;
  private int maxHealth = 0;
  private int currentLevel = 0;
  private int currentXP = 0;
  private boolean isGameCharacter = false;
  private String currentChannel = "root";

  public static Character getFromName(
      String name) {
    try {
      return ParseQuery.getQuery(Character.class).whereEqualTo("name", name).getFirst();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  // public static Character create(String name) {
  // return new Character().setName(name);
  // }
  //
  // public static Character getFromParseObject(ParseObject character) {
  // return ParseObject.createWithoutData(Character.class,
  // character.getObjectId());
  // }
  //
  // private static Character fromParseObject(ParseObject character) {
  // final String objectId = character.getObjectId();
  // final String name = character.getString("name");
  // final int currentHealth = character.getInt("currentHealth");
  // final int maxHealth = character.getInt("maxHealth");
  // final int currentLevel = character.getInt("currentLevel");
  // final int currentXP = character.getInt("currentXP");
  //
  // Character newCharacter = new Character();
  // newCharacter.setName(name).setHealth(currentHealth,
  // maxHealth).setXP(currentXP, currentLevel);
  // newCharacter.setObjectId(objectId);
  // return newCharacter;
  // }

  public Character() {

  }

  public Character setName(String name) {
    this.name = name;
    return this;
  }

  public Character setHealth(
      int currentHealth, int maxHealth) {
    this.currentHealth = currentHealth;
    this.maxHealth = maxHealth;
    return this;
  }

  public Character setXP(int currentXP,
      int currentLevel) {
    this.currentLevel = currentLevel;
    this.currentXP = currentXP;
    return this;
  }

  public Character setGameCharacter(
      boolean isGameCharacter) {
    this.isGameCharacter = isGameCharacter;
    return this;
  }

  public void send() {
    applyToParseObject();
      send(null);
  }

  public void send(
      SaveObjectSerialExecutor.OnSaveCallbacks callback) {
    applyToParseObject();
      SaveObjectSerialExecutor.get().sendMessage(this, callback);
//    new SaveObjectAsyncTask(callback).execute(this);
  }

  private void applyToParseObject() {
    this.put("name", name);
    this.put("currentHealth", currentHealth);
    this.put("maxHealth", maxHealth);
    this.put("currentLevel", currentLevel);
    this.put("currentXP", currentXP);
    this.put("isGameCharacter", isGameCharacter);
    this.put("currentChannel", currentChannel);
  }

  public String getName() {
    return this.getString("name");
  }

  public int getCurrentHealth() {
    return this.getInt("currentHealth");
  }

  public int getMaxHealth() {
    return this.getInt("maxHealth");
  }

  public int getLevel() {
    return this.getInt("currentLevel");
  }

  public int getXP() {
    return this.getInt("currentXP");
  }

  public boolean isPlayable() {
    return this.getBoolean("inGameCharacter");
  }

  public int getGold() {
    return this.getInt("currentGold");
  }

  public String getUrlFullSize() {
    return this.getString("profilePicFullSizeUrl");
  }

  public String getCurrentChannel() {
    return this.getString("currentChannel");
  }

  public void moveToChannel(String channel) {
    this.currentChannel = channel;
    this.put("currentChannel", channel);
  }

//  @Override public String get(
//      int columnPosition) {
//    if (isHeader) { return getColumnHeader(columnPosition); }
//
//    switch (columnPosition) {
//    case 0:
//      return getName();
//    case 1:
//      return "playable:"
//          + isPlayable();
//    case 2:
//      if (isPlayable())
//      return getCurrentHealth() + "/"
//          + getMaxHealth();
//      else
//        return "";
//    case 3:
//      if (isPlayable())
//      return getLevel() + "["
//          + getXP() + "]";
//      else
//        return "";
//    case 4:
//      if (isPlayable())
//      return "$"+getGold();
//      else
//        return "";
//    default:
//      return "none";
//    }
//  }
//
//  @Override public int getColumnCount() {
//    return 5;
//  }
//
//  @Override public String getColumnHeader(
//      int columnPosition) {
//    switch (columnPosition) {
//    case 0:
//      return "name";
//    case 1:
//      return "isPlayable";
//    case 2:
//      return "health/max";
//    case 3:
//      return "level[xp]";
//    case 4:
//      return "gold";
//    default:
//      return "none";
//    }
//  }
//
//  private boolean isHeader = false;
//  @Override public void useAsHeaderView(boolean b) {
//    isHeader = b;
//  }
//
//  @Override public boolean isHeaderView() {
//    return isHeader;
//  }

  private static final AbsBanditObject.Column[] COLUMNS = {
    new Column("name", DataType.String),
    new Column("isNPC", DataType.Boolean),
    new Column("currentChannel", DataType.String),
    new Column("ofGame", DataType.Relation),
    new Column("owner", DataType.Relation),

  };

  @Override public String toString() {
    final Set<String> keySet = this.keySet();
    final StringBuilder sb = new StringBuilder();
    for (String key : keySet) {
      sb.append("\n");
      sb.append(key);
      sb.append(": ");
      sb.append(this.get(key));
    }
    return sb.toString();
  }

  @Override public AbsBanditObject.Column get(
      int columnPosition) {
    return COLUMNS[columnPosition];
  }

  @Override public int getColumnCount() {
    return COLUMNS.length;
  }

  private boolean isIdEquals(Character character) {
    if (this.getObjectId().equalsIgnoreCase(character.getObjectId())) { return true; }
    return false;
  }

  @Override public boolean equals(
      Object o) {
    if (o instanceof Character) {
      return this.equals((Character) o);
    } else {
      return false;
    }
  }

  public boolean equals(
      Character character) {
    return this.isIdEquals(character);
  }

  private static Character makeCharacter(
      Character.Builder builder,
      SaveCallback callback) {
    if (callback == null) {
      callback = new SaveCallback() {

        @Override public void done(
            ParseException e) {
          if (e == null) {
            if (LOG)
              Log.i(TAG, "Character saved.");
          } else {
            e.printStackTrace();
          }
        }
      };
    }

    Character character = new Character();
    character.loadCharacter(builder);
    character.saveInBackground(callback);
    return character;
  }

  private void loadCharacter(Character.Builder builder) {
    // TODO: populate fields
  }

  public static class Builder {
    private Builder() {}
    public static Builder getNewCharacter() { return new Builder(); }



    public Character create() {
      return create(null);
    }

    public Character create(
        SaveCallback callback) {
//      rootView = null;
      return Character.makeCharacter(this, callback);
    }
  }

  public String getProfilePicUrl() {
    return this.getString("profilePicUrl");
  }
}
