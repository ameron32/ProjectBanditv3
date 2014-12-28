package com.ameron32.apps.projectbanditv3.adapter;

import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.QueryFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsParseRecyclerQueryAdapter<T extends ParseObject, U extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<U>
{

  
  private final QueryFactory<T> factory;
  private List<T> items;

  // PRIMARY CONSTRUCTOR
  public AbsParseRecyclerQueryAdapter(final QueryFactory<T> factory) {
    this.factory = factory;
    this.items = new ArrayList<T>();
    mListeners = new ArrayList<OnDataSetChangedListener>();
    
    loadObjects();
  }
  
  // ALTERNATE CONSTRUCTOR
  public AbsParseRecyclerQueryAdapter(final String className) {
    this(new QueryFactory<T>() {
      
      @Override public ParseQuery<T> create() {
        return ParseQuery.getQuery(className);
      }
    });
  }
  
  // ALTERNATE CONSTRUCTOR
  public AbsParseRecyclerQueryAdapter(final Class<T> c) {
    this(new QueryFactory<T>() {
      
      @Override public ParseQuery<T> create() {
        return ParseQuery.getQuery(c);
      }
    });
  }
  
  
  
  
  
  @Override public int getItemCount() {
    return items.size();
  }
  
  public T getItem(int position) { return items.get(position); }

  public List<T> getItems() { return items; }

  protected void onFilterQuery(ParseQuery<T> query) { 
    // provide override for filtering query
  }
  
  public void loadObjects() {
    dispatchOnLoading();
    ParseQuery<T> query = factory.create();
    onFilterQuery(query);
    query.findInBackground(new FindCallback<T>() {;
    
      @Override public void done(
          List<T> queriedItems,
          ParseException e) {
        if (e == null) {
          items = queriedItems;
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
  
  private List<OnDataSetChangedListener> mListeners;
  
  public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
    mListeners.add(listener);
  }
  
  public void removeOnDataSetChangedListener(OnDataSetChangedListener listener) {
    if (mListeners.contains(listener)) {
      mListeners.remove(listener);
    }
  }
  
  protected void fireOnDataSetChanged() {
    for (int i = 0; i < mListeners.size(); i++) {
      mListeners.get(i).onDataSetChanged();
    }
  }
  
  public interface OnQueryLoadListener<T> {
    
    public void onLoaded(
        List<T> objects, Exception e);
    
    public void onLoading();
  }
  
  private List<OnQueryLoadListener<T>> mQueryListeners = new ArrayList<OnQueryLoadListener<T>>();
  
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
