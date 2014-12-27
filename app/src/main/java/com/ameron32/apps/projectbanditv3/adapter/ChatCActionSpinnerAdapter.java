package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.CAction;
import com.parse.ParseQuery;

public class ChatCActionSpinnerAdapter extends ParseSpinnerQueryAdapter<CAction> {
  public ChatCActionSpinnerAdapter(Context context) {
    super(context, makeFactory(), R.layout.row_dropdown);
  }

  private static QueryFactory<CAction> makeFactory() {
    return new QueryFactory<CAction>() {

      @Override
      public ParseQuery<CAction> create() {
        return Query._CAction.getChatCActionsQuery();
      }
    };
  }
}