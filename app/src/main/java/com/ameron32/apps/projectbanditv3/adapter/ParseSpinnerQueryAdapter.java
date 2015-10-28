package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;

import com.ameron32.apps.projectbanditv3.AutoReloader;
import com.parse.ParseObject;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter;

public class ParseSpinnerQueryAdapter<T extends ParseObject>
    extends ParseQueryAdapter<T>
    implements AutoReloader
{

//  public ParseSpinnerQueryAdapter(
//      Context context,
//      Class<? extends ParseObject> clazz,
//      int itemViewResource) {
//    super(context, clazz, itemViewResource);
//  }
//
//  public ParseSpinnerQueryAdapter(
//      Context context,
//      Class<? extends ParseObject> clazz) {
//    super(context, clazz);
//  }

  public ParseSpinnerQueryAdapter(
      Context context,
      QueryFactory<T> queryFactory,
      int itemViewResource) {
    super(context, queryFactory, itemViewResource);
  }

  public ParseSpinnerQueryAdapter(
      Context context,
      QueryFactory<T> queryFactory) {
    super(context, queryFactory);
  }

  public ParseSpinnerQueryAdapter(
      Context context,
      String className,
      int itemViewResource) {
    super(context, className, itemViewResource);
  }

  public ParseSpinnerQueryAdapter(
      Context context, String className) {
    super(context, className);
  }

  @Override public int getViewTypeCount() {
    return 1;
  }
}
