package com.ameron32.apps.projectbanditv3;

import android.util.Log;
import android.util.SparseArray;

import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.ameron32.apps.projectbanditv3.object.Message.MessageType;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
  public static String displayAsList(char separator, List<? extends ParseObject> list, String useKey) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i != 0) {
        sb.append(separator);
      }
      ParseObject obj = list.get(i);
      sb.append(obj.getString(useKey));
    }
    return sb.toString();
  }

  public static List<String> toListOfStrings(List<? extends ParseObject> list, String key) {
    List<String> strings = new ArrayList<String>();
    for (int i = 0; i < list.size(); i++) {
      ParseObject o = list.get(i);
      strings.add(o.getString(key));
    }
    return strings;
  }

  public static ParseObject addUserRelationToObject(String object1Type, String object1Name, String relation, String user2Type, String user2Name) {
    ParseObject targetCharacter1 = null;
    try {
      ParseUser parseUser = new ParseQuery<ParseUser>(user2Type)
          .whereContains("username", user2Name).getFirst();
      Log.d("PIA", "PUser.id = " + ((parseUser != null) ? parseUser.getObjectId() : "null"));
      targetCharacter1 = ParseQuery.getQuery(object1Type)
          .whereContains("name", object1Name).getFirst();
      ParseRelation<ParseUser> relation1 = targetCharacter1.getRelation(relation);
      relation1.add(parseUser);
      Log.d("PIA", relation + " [" + user2Name + "{" +user2Type+ "} into " + object1Name + "{" +object1Type+ "}] added");
    } catch (ParseException pEx) {
      pEx.printStackTrace();
      Log.d("PIA", relation + " [" + user2Name + "{" +user2Type+ "} into " + object1Name + "{" +object1Type+ "}] failed");
    }
    return targetCharacter1;
  }

  public static ParseObject addObjectRelationToObject(String object1Type, String object1Name, String relation, String object2Type, String object2Name) {
    ParseObject targetCharacter1 = null;
    try {
      ParseObject parseUser = new ParseQuery<ParseObject>(object2Type)
          .whereContains("name", object2Name).getFirst();
      targetCharacter1 = ParseQuery.getQuery(object1Type)
          .whereContains("name", object1Name).getFirst();
      ParseRelation<ParseObject> relation1 = targetCharacter1
          .getRelation(relation);
      relation1.add(parseUser);
      Log.d("PIA", relation + " [" + object2Name + "{" +object2Type+ "} into " + object1Name + "{" +object1Type+ "}] added");
    } catch (ParseException pEx) {
      pEx.printStackTrace();
      Log.d("PIA", relation + " [" + object2Name + "{" +object2Type+ "} into " + object1Name + "{" +object1Type+ "}] failed");
    }
    return targetCharacter1;
  }

  public static Game addPlayerToGame(User user, Game game) {
    addRelation(user, "players", game);
    return game;
  }

  public static com.ameron32.apps.projectbanditv3.object.Character addGameToCharacter(Character character, Game game) {
    addRelation(character, "ofGame", game);
    return character;
  }

  private static void addRelation(ParseObject object, String relation, ParseObject objectToAdd) {
    ParseRelation<ParseObject> relation1 = object.getRelation(relation);
    relation1.add(objectToAdd);
  }

  public static void addItemToGame(Item item, Game game) {
    ParseRelation<Game> relation = item.getRelation("usableInGame");
    relation.add(game);
  }

  public static void saveObjects(List<ParseObject> objects) {
  }

  public static void rollDice(int[][] diceQuantity, int modifier) {
    final int unitDieSize = 0;
    final int unitDieCount = 1;

    HashMap<String, Object> map = new HashMap<>();

    for (int[] pair : diceQuantity) {
      final int dieSize  = pair[unitDieSize];
      final int dieCount = pair[unitDieCount];
      map.put("d"+String.valueOf(dieSize), dieCount);
    }
    map.put("mod", modifier);

    Log.d("UTIL", map.toString());

//  map.put("user", ParseUser.getCurrentUser());
    ParseCloud.callFunctionInBackground("roll", map, new FunctionCallback<ArrayList<Object>>() {

      public void done(ArrayList<Object> result,
          ParseException e) {
        if (e == null) {
          // result is "Hello world!"
  //        Toast.makeText(MainPushActivity.this, result, Toast.LENGTH_SHORT).show();
          String user = (String) result.get(0);
          Integer total = (Integer) result.get(1);
          String description = (String) result.get(2);
          String combo = (String) result.get(3);

          Log.d("UTIL", result.toString());

          try {
            ParseObject oAttempts = new ParseQuery<ParseObject>("CAction").get("QJJa4LdZUV");

            Message.create()
              .setMessage(user + " rolls [" + total + "] on " + description + ".")
              .setAction(oAttempts)
              .setChannel(CharacterManager.get().getCurrentCharacter().getCurrentChannel())
              .setType(MessageType.SYSTEM)
              .setCharacter(CharacterManager.get().getChatCharacter())
              .send();
          } catch (ParseException e1) {
            e1.printStackTrace();
          } finally {}
        }
      }
    });
  }
}
