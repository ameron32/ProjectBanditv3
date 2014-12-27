package com.ameron32.apps.projectbanditv3;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.ameron32.apps.projectbanditv3.fragment.ChatManagerFragment;


public class ChatInputWatcher implements
    TextWatcher {
  
  // private final Activity activity;
  
  private final EditText editText;
  private ChatManagerFragment f;
  
  public ChatInputWatcher(
      EditText editText,
      ChatManagerFragment f) {
    this.editText = editText;
    this.f = f;
    // this.activity = activity;
  }
  
  boolean toggle = false;
  
  @Override public void onTextChanged(
      CharSequence s, int start,
      int before, int count) {
    Log.i("onTextChanged", "s=" + s
        + " start=" + start
        + " before=" + before
        + " count=" + count);
    
    /**
     * Logic: if the first character BECOMES '#', switch to Command mode. if
     * the first character CEASES TO BE '#', switch back to Note mode. getting
     * there was a lot of if statements.
     */
    if (s == null) return;
    
    if (s.length() == 0) {
      if (toggleOff()) {
        goStandardMode();
      }
      return;
    }
    
    switch (s.charAt(0)) {
    case '/':
      if (toggleOn()) {
        goEmoteMode();
      }
      return;
      // case '$':
      // if (toggleOn()) {
      // goSpeakerMode();
      // }
      // return;
    case '#':
      if (toggleOn()) {
        goCommandMode();
      }
      return;
    }
  }
  
  @Override public void beforeTextChanged(
      CharSequence s, int start,
      int count, int after) {}
  
  @Override public void afterTextChanged(
      Editable s) {}
  
  private boolean toggleOn() {
    if (!toggle) {
      toggle = true;
      Log.i("onTextChanged", "ON");
      return true;
    }
    return false;
  }
  
  private boolean toggleOff() {
    if (toggle) {
      toggle = false;
      Log.i("onTextChanged", "OFF");
      return true;
    }
    return false;
  }
  
  private void goStandardMode() {
    final Context context = editText.getContext();
    int fontColorRes = context.getResources().getColor(R.color.myTextPrimaryColor);
    setEditTextFontColor(fontColorRes);
    int colorRes = context.getResources().getColor(R.color.chat_manager_background);
    setEditTextBGColor(colorRes);
    f.updateEditTextHint();
  }

  private void goCommandMode() {
    final String message = "Command line";
    final Context context = editText.getContext();
    int fontColorRes = context.getResources().getColor(R.color.myAccentColor);
    setEditTextFontColor(fontColorRes);
    int colorRes = context.getResources().getColor(R.color.myWindowBackground);
    setEditTextBGColor(colorRes);
    setHint(message);
  }
  
  private void goEmoteMode() {
    final String message = "Emote mode";
    final Context context = editText.getContext();
    int fontColorRes = context.getResources().getColor(R.color.myAccentColor);
    setEditTextFontColor(fontColorRes);
    int colorRes = context.getResources().getColor(R.color.chat_manager_background);
    setEditTextBGColor(colorRes);
    setHint(message);
  }
  
  private void setHint(
      final String message) {
    editText.setHint(message);
  }
  
  private void setEditTextFontColor(
      int colorResource) {
    editText.setTextColor(colorResource);
  }
  
  private void setEditTextBGColor(
      int colorResource) {
    editText.setBackgroundColor(colorResource);
  }
}