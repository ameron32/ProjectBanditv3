package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.view.View;

import com.ameron32.apps.projectbanditv3.view.FloatingHintTextView;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import butterknife.ButterKnife;


public class StatsTestFragment extends
    SectionContainerTestFragment {
  
  
  
  
  
  private View mStatsRootView;

  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mStatsRootView = view;
    ButterKnife.inject(this, view);
    
    insertParseCharacterValues(CharacterManager.get().getCurrentCharacter());
  }
  
  private void insertParseCharacterValues(ParseObject character) {
    if (character != null) {
      ParseQuery<com.ameron32.apps.projectbanditv3.object.Character> query = new ParseQuery<Character>("Character");
      query.whereContains("name", character.getString("name"));
      query.getFirstInBackground(new GetCallback<Character>() {

        @Override
        public void done(Character character, ParseException e) {
//          Character character = Character.getFromParseObject(characterParseObject);
          int currentHealth = character.getInt("currentHealth");
          int maxHealth = character.getInt("maxHealth");
          int currentLevel = character.getInt("currentLevel");
          int currentXP = character.getInt("currentXP");
          String name = character.getString("name");

          FloatingHintTextView currentHealthCV = (FloatingHintTextView) mStatsRootView
              .findViewById(R.id.textcomboview_current_health);
          FloatingHintTextView maxHealthCV = (FloatingHintTextView) mStatsRootView
              .findViewById(R.id.textcomboview_max_health);
          FloatingHintTextView levelCV = (FloatingHintTextView) mStatsRootView
              .findViewById(R.id.textcomboview_level);
          FloatingHintTextView experienceCV = (FloatingHintTextView) mStatsRootView
              .findViewById(R.id.textcomboview_xp);
          FloatingHintTextView characterCV = (FloatingHintTextView) mStatsRootView
              .findViewById(R.id.textcomboview_character);

          currentHealthCV.setText(currentHealth + "");
          maxHealthCV.setText(maxHealth + "");
          levelCV.setText(currentLevel + "");
          experienceCV.setText(currentXP + "");
          characterCV.setText(name);
        }
      });
    }
  }
}
