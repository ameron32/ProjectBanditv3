package com.ameron32.apps.projectbanditv3.adapter;


import android.content.Context;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.ParseQuery;

public class ChatCharacterSpinnerAdapter
    extends ParseSpinnerQueryAdapter<com.ameron32.apps.projectbanditv3.object.Character> {
  public ChatCharacterSpinnerAdapter(Context context) {
    super(context, makeFactory(), R.layout.row_dropdown);
  }

  private static QueryFactory<Character> makeFactory() {
    return new QueryFactory<Character>() {

      @Override
      public ParseQuery<Character> create() {
        return Query._Character.getChatCharacters();
      }
    };
  }
}
