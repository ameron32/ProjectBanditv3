package com.ameron32.apps.projectbanditv3.manager;

import android.util.Log;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class CharacterManager extends AbsManager {
  protected static final String TAG = CharacterManager.class.getSimpleName();
  
  private static CharacterManager characterManager;
  
  public static CharacterManager get() {
    if (characterManager == null) {
      characterManager = new CharacterManager();
    }
    return characterManager;
  }
  
  public void initialize(
      final OnCharacterManagerInitializationCompleteListener listener) {
    initialize();
    if (listener != null) {
      listener.onCharacterManagerInitializationComplete();
    }
  }
  
  public void initialize() {
    try {
      if (mCurrentCharacter == null) {
        findPlayableCharacters();
      }
      if (mCurrentChatCharacter == null) {
        findChatCharacters();
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    setInitialized(true);
  }
  
  private void findPlayableCharacters() throws ParseException {
    List<com.ameron32.apps.projectbanditv3.object.Character> playableCharacters = Query._Character.getPlayableCharacters().find();
    
    mPlayableCharacters = playableCharacters;
    final int FIRST = 0;
    final Character character = playableCharacters.get(FIRST);
    setCurrentCharacter(character);
  }

  private void findChatCharacters() throws ParseException {
    List<Character> chatCharacters = Query._Character.getChatCharacters().find();
    
    mChatCharacters = chatCharacters;
    Character lastChatCharacter = UserManager.get().getCurrentUser().getLastChatCharacter();
    if (lastChatCharacter == null) {
      // did not find
      Log.i(TAG, "chat character was null");
      setChatCharacter(chatCharacters.get(0), 0);
      return; // done
    }
    
    for (int i = 0; i < chatCharacters.size(); i++) {
      if (lastChatCharacter.equals(chatCharacters.get(i))) {
        setChatCharacter(lastChatCharacter, i);
        return; // done
      }
    }
    
    // did not find
    Log.i(TAG, "no chat character found");
    setChatCharacter(chatCharacters.get(0), 0);
  }

  public interface OnCharacterManagerInitializationCompleteListener {
    public void onCharacterManagerInitializationComplete();
  }
  
  private List<Character> mPlayableCharacters;
  private List<Character> mChatCharacters;
  private Character mCurrentCharacter;
  private Character mCurrentChatCharacter;
  private int mCurrentChatCharacterPosition;
  
  protected CharacterManager() {
    mListeners = new ArrayList<OnCharacterChangeListener>();
  }
  
  public void setCurrentCharacter(
      Character character) {
    if (character.getBoolean("inGameCharacter")) {
      mCurrentCharacter = character;
      notifyListenersOfCharacterChange(character);
    }
    // did not set current character
    Log.i(TAG, "did not set current character. was not an inGameCharacter.");
  }
  
  public void setChatCharacter(
      Character character, int position) {
    mCurrentChatCharacter = character;
    mCurrentChatCharacterPosition = position;
    UserManager.get().getCurrentUser().setLastChatCharacter(character);
    notifyListenersOfChatCharacterChange(character);
  }
  
  public int getChatCharacterPosition() {
    return mCurrentChatCharacterPosition;
  }
  
  public Character getChatCharacter() {
    if (mCurrentChatCharacter == null) {
      throw new IllegalStateException("CurrentChatCharacter should not be null.");
    }
    return mCurrentChatCharacter;
  }
  
  public Character getCurrentCharacter() {
    if (mCurrentCharacter == null) {
      throw new IllegalStateException("CurrentCharacter should not be null.");
    }
    return mCurrentCharacter;
  }
  
  public static void destroy() {
    characterManager = null;
  }
  
  public void queryAllCharacters(
      FindCallback<Character> callback) {
    ParseQuery<Character> query = Query._Character.getChatCharacters();
    query.findInBackground(callback);
  }
  
  
  

  public List<OnCharacterChangeListener> mListeners;
  
  public boolean addOnCharacterChangeListener(
      OnCharacterChangeListener listener) {
    return mListeners.add(listener);
  }
  
  public boolean removeOnCharacterChangeListener(
      OnCharacterChangeListener listener) {
    return mListeners.remove(listener);
  }
  
  private void notifyListenersOfCharacterChange(Character character) {
    for (OnCharacterChangeListener listener : mListeners) {
      listener.onCharacterChange(this, character);
    }
  }
  
  private void notifyListenersOfChatCharacterChange(Character character) {
    for (OnCharacterChangeListener listener : mListeners) {
      listener.onChatCharacterChange(this, character);
    }
  }
  
  public interface OnCharacterChangeListener {
    
    public void onCharacterChange(
        CharacterManager manager,
        Character newCharacter);
    
    public void onChatCharacterChange(
        CharacterManager manager,
        Character newCharacter);
  }
}
