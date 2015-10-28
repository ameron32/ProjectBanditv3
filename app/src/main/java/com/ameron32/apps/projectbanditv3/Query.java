package com.ameron32.apps.projectbanditv3;

import android.util.Log;

import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.manager.ObjectManager;
import com.ameron32.apps.projectbanditv3.manager.UserManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Query {

  private static final boolean TOAST = false;
  private static final boolean LOG = true;
  private static final String TAG = Query.class.getSimpleName();

  public static <O extends ParseObject> void getDefaultQuery(String parseClassName, FindCallback<O> callback) {
    if (parseClassName.equalsIgnoreCase("Character")) {
      FindCallback<com.ameron32.apps.projectbanditv3.object.Character> cCallback = (FindCallback<Character>) callback;
      CharacterManager.get().queryAllCharacters(cCallback);
    } else
    if (parseClassName.equalsIgnoreCase("CInventory")) {
      FindCallback<CInventory> cCallback = (FindCallback<CInventory>) callback;
      ObjectManager.get().queryAllInventory(cCallback);
    } else
    if (parseClassName.equalsIgnoreCase("Item")) {
      FindCallback<Item> cCallback = (FindCallback<Item>) callback;
      // TODO: ItemManager().get().queryAllItems(cCallback); ?
      Query._Item.getFullItemQuery().findInBackground(cCallback);
    } else {
      if (LOG) {
        final String eMessage = "getDefaultQuery(): parseClassName does not correspond to an appropriate default query";
        Log.e(TAG, eMessage);
      }
    }
  }

  public static class _Message {
    private static ParseQuery<Message> create() {
      return new ParseQuery<Message>(Message.class);
    }

    public static ParseQuery<Message> getRecentQuery() {
      final ParseQuery<Message> query = create();

      withinCurrentGame_CurrentSession(query);
      withinCurrentChannel(query);
      orderNewestFirst(query);
      recentOnly(query);
      setLimit(query, 1000);
      standardIncludes(query);

      return query;
    }

    public static ParseQuery<Message> getOOCQuery() {
      final ParseQuery<Message> query = create();

      withinCurrentGame_CurrentSession(query);
      orderNewestFirst(query);
      setLimit(query, 1000);
      standardIncludes(query);

      query.whereMatchesQuery("character", _Character.getOOCQuery());

      return query;
    }

    public static ParseQuery<Message> getStoryChatQuery() {
      final ParseQuery<Message> query = create();

      withinCurrentGame_All(query);
      orderNewestFirst(query);
      storyOnly(query);
      setLimit(query, 1000);
      standardIncludes(query);

      return query;
    }

    public static ParseQuery<Message> get1000ChatQuery() {
      final ParseQuery<Message> query = create();

      withinCurrentGame_CurrentSession(query);
      orderNewestFirst(query);
      setLimit(query, 1000);
      standardIncludes(query);

      return query;
    }

    public static ParseQuery<Message> getNotificationQuery() {
      final ParseQuery<Message> query = create();

      withinCurrentGame_CurrentSession(query);
      orderNewestFirst(query);
      since(query, ChatService.getLastSystemTimeWhenAppOff());
      setLimit(query, 1000);
      standardIncludes(query);

      return query;
    }

    private static void withinCurrentGame_CurrentSession(
        ParseQuery<Message> query) {
      Game game = null;
      game = GameManager.get().getCurrentGame();
      if (game != null) {
        int currentSession = game.getCurrentSession();
        query.whereEqualTo("ofGame", game);
        query.whereEqualTo("inSession", currentSession);
      } else {
        query.whereDoesNotExist("ofGame");
        query.whereDoesNotExist("isSession");
      }
    }

    private static void withinCurrentChannel(
        ParseQuery<Message> query) {
      String channel = CharacterManager.get().getCurrentCharacter().getCurrentChannel();
      query.whereEqualTo("channel", channel);
    }

    private static void storyOnly(
        ParseQuery<Message> query) {
      query.whereEqualTo("canon", true);
    }

    private static void standardIncludes(
        ParseQuery<Message> query) {
      include(query, "user", "character", "actionO", "ofGame");
    }
  }

  public static class _Character {
    private static ParseQuery<Character> create() {
      return new ParseQuery<Character>(Character.class);
    }

    public static ParseQuery<Character> getOOCQuery() {
      final ParseQuery<Character> query = create();

      oocOnly(query);
      standardIncludes(query);

      return query;
    }

    public static ParseQuery<Character> getAllLibraryCharacters() {
      final ParseQuery<Character> query = create();

      orderPlayableLast_AZ(query);

      return query;
    }

    public static ParseQuery<Character> getAllLibraryCharactersInCurrentGame() {
      final ParseQuery<Character> query = create();

      withinCurrentGame_All(query);
      orderPlayableLast_AZ(query);

      return query;
    }

    public static ParseQuery<Character> getChatCharacters() {
      final ParseQuery<Character> query = create();

      withinCurrentGame_All(query);
      currentUserIsOwner(query);
      orderNPCLast_AZ(query);

      return query;
    }

    public static ParseQuery<Character> getPlayableCharacters() {
      ParseQuery<Character> query = create();

      withinCurrentGame_All(query);
      currentUserIsOwner(query);
      playableCharactersOnly(query);
      orderNPCLast_AZ(query);
//      orderAZ(query);

      return query;
    }

    private static void orderPlayableLast_AZ(
        ParseQuery<Character> query) {
      query.orderByAscending("ooc");
//      query.addAscendingOrder("isNPC");
      query.addAscendingOrder("name");
    }

    private static void orderNPCLast_AZ(
        ParseQuery<Character> query) {
      query.orderByAscending("isNPC");
      query.addAscendingOrder("ooc");
      query.addAscendingOrder("name");
    }

    private static void oocOnly(
        ParseQuery<Character> query) {
      query.whereEqualTo("ooc", true);
    }

    private static void currentUserIsOwner(
        final ParseQuery<Character> query) {
      query.whereEqualTo("owner", UserManager.get().getCurrentUser());
    }

    private static void playableCharactersOnly(
        ParseQuery<Character> query) {
      query.whereEqualTo("inGameCharacter", true);
    }

    private static void standardIncludes(
        ParseQuery<Character> query) {
      include(query, "profilePic", "owner", "actionO");
    }
  }

  public static class _Game {

    private static ParseQuery<Game> create() {
      return new ParseQuery<Game>(Game.class);
    }

    public static ParseQuery<Game> getCurrentGamesQuery() {
      ParseQuery<Game> query = create();

      currentUserIsPlayer(query);
      orderAZ(query);

      return query;
    }

    private static void currentUserIsPlayer(
        ParseQuery<Game> query) {
      query.whereEqualTo("players", UserManager.get().getCurrentUser());
    }
  }

  public static class _CAction {

    private static ParseQuery<CAction> create() {
      return new ParseQuery<CAction>(CAction.class);
    }

    public static ParseQuery<CAction> getChatCActionsQuery() {
      ParseQuery<CAction> query = create();

      return query;
    }
  }

  public static class _Inventory {

    private static ParseQuery<CInventory> create() {
      return new ParseQuery<CInventory>(CInventory.class);
    }

    public static ParseQuery<CInventory> getCInventoryQuery() {
      ParseQuery<CInventory> query = create();

      return query;
    }

    public static ParseQuery<CInventory> getAllInventory() {
      ParseQuery<CInventory> query = create();

      standardIncludes(query);

      return query;
    }

    private static void standardIncludes(
        ParseQuery<CInventory> query) {
      include(query, "item");
    }
  }

  public static class _Item {

    private static ParseQuery<Item> create() {
      return new ParseQuery<Item>(Item.class);
    }

    public static ParseQuery<Item> getItemQuery() {
      ParseQuery<Item> query = create();

      withinCurrentGame_All(query);
      setLimit(query, 1000);

      return query;
    }

    public static ParseQuery<Item> getFullItemQuery() {
      ParseQuery<Item> query = create();

      setLimit(query, 1000);

      return query;
    }

    private static void withinCurrentGame_All(
        ParseQuery<? extends ParseObject> query) {
      Game game = null;
      game = GameManager.get().getCurrentGame();
      if (game != null) {
        query.whereEqualTo("usableInGame", game);
      } else {
        query.whereDoesNotExist("usableInGame");
      }
    }
  }

  public static class _User {

    private static ParseQuery<User> create() {
      return new ParseQuery<User>(User.class);
    }

    public static ParseQuery<User> getUsersQuery() {
      ParseQuery<User> query = create();

      return query;
    }

    public static ParseQuery<User> getAllUsers() {
      ParseQuery<User> query = create();

      setLimit(query, 1000);

      return query;
    }
  }

  private static GregorianCalendar createCalendar() {
    final GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
    return calendar;
  }

  private static void orderAZ(
      ParseQuery<? extends ParseObject> query) {
    query.orderByAscending("name");
  }

  private static void orderNewestFirst(
      ParseQuery<? extends ParseObject> query) {
    query.orderByAscending("createdAt");
  }

  private static void recentOnly(
      ParseQuery<? extends ParseObject> query) {
    final int hours = -12;
    query.whereGreaterThan("createdAt", makeDateOffsetBy(hours));
  }

  private static void withinCurrentGame_All(
      ParseQuery<? extends ParseObject> query) {
    Game game = null;
    game = GameManager.get().getCurrentGame();
    if (game != null) {
      query.whereEqualTo("ofGame", game);
    } else {
      query.whereDoesNotExist("ofGame");
    }
  }

  private static void since(
      ParseQuery<? extends ParseObject> query,
      long time) {
    query.whereGreaterThan("createdAt", makeDateFromMillis(time));
  }

  private static void setLimit(
      ParseQuery<? extends ParseObject> query,
      int limit) {
    query.setLimit(limit);
  }

  private static Date makeDateOffsetBy(int hours) {
    final GregorianCalendar calendar = createCalendar();
    calendar.add(GregorianCalendar.HOUR, hours);
    final Date d = calendar.getTime();
    return d;
  }

  private static Date makeDateFromMillis(long systemMillis) {
    final GregorianCalendar calendar = createCalendar();
    calendar.setTimeInMillis(systemMillis);
    final Date d = calendar.getTime();
    return d;
  }

  private static void include(
      ParseQuery<? extends ParseObject> query,
      String... keys) {
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      query.include(key);
    }
  }
}
