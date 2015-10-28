package com.ameron32.apps.projectbanditv3.adapter;

import android.support.v7.widget.RecyclerView;

import com.ameron32.apps.projectbanditv3.AutoReloader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter.QueryFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsParseSuperRecyclerQueryAdapter<T extends ParseObject, U extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<U>
    implements AutoReloader
{

  @NotNull
  private final QueryFactory<T> mFactory;
  private final boolean hasStableIds;
  @NotNull
  private final List<T> mItems;

  // PRIMARY CONSTRUCTOR
  public AbsParseSuperRecyclerQueryAdapter(final QueryFactory<T> factory, final boolean hasStableIds) {
    mFactory = factory;
    mItems = new ArrayList<T>();
    mDataSetListeners = new ArrayList<OnDataSetChangedListener>();
    mQueryListeners = new ArrayList<OnQueryLoadListener<T>>();
    this.hasStableIds = hasStableIds;

    setHasStableIds(hasStableIds);
    loadObjects();
  }

  // ALTERNATE CONSTRUCTOR
  public AbsParseSuperRecyclerQueryAdapter(final String className, final boolean hasStableIds) {
    this(new QueryFactory<T>() {

      @Override public ParseQuery<T> create() {
        return ParseQuery.getQuery(className);
      }
    }, hasStableIds);
  }

  // ALTERNATE CONSTRUCTOR
  public AbsParseSuperRecyclerQueryAdapter(final Class<T> clazz, final boolean hasStableIds) {
    this(new QueryFactory<T>() {

      @Override public ParseQuery<T> create() {
        return ParseQuery.getQuery(clazz);
      }
    }, hasStableIds);
  }


  /*
   *  REQUIRED RECYCLERVIEW METHOD OVERRIDES
   */

  @Override
  public long getItemId(int position) {
    if (hasStableIds) {
      return position;
    }
    return super.getItemId(position);
  }

  @Override public int getItemCount() {
    return mItems.size();
  }

  public T getItem(int position) { return mItems.get(position); }

  @NotNull
  public List<T> getItems() { return mItems; }






  protected void onFilterQuery(ParseQuery<T> query) {
    // provide override for filtering query
  }

  public void loadObjects() {
    dispatchOnLoading();
    final ParseQuery<T> query = mFactory.create();
    onFilterQuery(query);
    query.findInBackground(new FindCallback<T>() {;

      @Override public void done(
          List<T> queriedItems,
          @Nullable ParseException e) {
        if (e == null) {
          mItems.clear();
          mItems.addAll(queriedItems);
//          mItems = queriedItems;
          dispatchOnLoaded(queriedItems, e);
          notifyDataSetChanged();
          fireOnDataSetChanged();
        }
      }
    });
  }



  public interface OnDataSetChangedListener {
    public void onDataSetChanged();
  }

  @NotNull
  private final List<OnDataSetChangedListener> mDataSetListeners;

  public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
    mDataSetListeners.add(listener);
  }

  public void removeOnDataSetChangedListener(OnDataSetChangedListener listener) {
    if (mDataSetListeners.contains(listener)) {
      mDataSetListeners.remove(listener);
    }
  }

  protected void fireOnDataSetChanged() {
    for (int i = 0; i < mDataSetListeners.size(); i++) {
      mDataSetListeners.get(i).onDataSetChanged();
    }
  }

  public interface OnQueryLoadListener<T> {

    public void onLoaded(
        List<T> objects, Exception e);

    public void onLoading();
  }

  @NotNull
  private final List<OnQueryLoadListener<T>> mQueryListeners;

  public void addOnQueryLoadListener(
      OnQueryLoadListener<T> listener) {
    if (!(mQueryListeners.contains(listener))) {
      mQueryListeners.add(listener);
    }
  }

  public void removeOnQueryLoadListener(
      OnQueryLoadListener<T> listener) {
    if (mQueryListeners.contains(listener)) {
      mQueryListeners.remove(listener);
    }
  }

  private void dispatchOnLoading() {
    for (OnQueryLoadListener<T> l : mQueryListeners) {
      l.onLoading();
    }
  }

  private void dispatchOnLoaded(List<T> objects, ParseException e) {
    for (OnQueryLoadListener<T> l : mQueryListeners) {
      l.onLoaded(objects, e);
    }
  }
}
