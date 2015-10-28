package com.ameron32.apps.projectbanditv3.parseui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import bolts.Capture;

/**
 * Created by klemeilleur on 10/27/2015.
 */
public class ParseQueryAdapter<T extends ParseObject> extends BaseAdapter {
  public static final int IMAGEVIEW_ID = android.R.id.icon1;
  public static final int TEXTVIEW_ID = android.R.id.text1;
  private String textKey;
  private String imageKey;
  private int objectsPerPage;
  private boolean paginationEnabled;
  private Drawable placeholder;
  private WeakHashMap<ParseImageView, Void> imageViewSet;
  private WeakHashMap<DataSetObserver, Void> dataSetObservers;
  private boolean autoload;
  private Context context;
  private List<T> objects;
  private List<List<T>> objectPages;
  private int currentPage;
  private Integer itemResourceId;
  private boolean hasNextPage;
  private ParseQueryAdapter.QueryFactory<T> queryFactory;
  private List<ParseQueryAdapter.OnQueryLoadListener<T>> onQueryLoadListeners;
  private static final int VIEW_TYPE_ITEM = 0;
  private static final int VIEW_TYPE_NEXT_PAGE = 1;

//  public ParseQueryAdapter(Context context, Class<? extends ParseObject> clazz) {
//    this(context, clazz.getSim);
//  }

  public ParseQueryAdapter(Context context, final String className) {
    this(context, new ParseQueryAdapter.QueryFactory() {
      public ParseQuery<T> create() {
        ParseQuery query = ParseQuery.getQuery(className);
        query.orderByDescending("createdAt");
        return query;
      }
    });
    if(className == null) {
      throw new RuntimeException("You need to specify a className for the ParseQueryAdapter");
    }
  }

//  public ParseQueryAdapter(Context context, Class<? extends ParseObject> clazz, int itemViewResource) {
//    this(context, clazz.getSimpleName(), itemViewResource);
//  }

  public ParseQueryAdapter(Context context, final String className, int itemViewResource) {
    this(context, new ParseQueryAdapter.QueryFactory() {
      public ParseQuery<T> create() {
        ParseQuery query = ParseQuery.getQuery(className);
        query.orderByDescending("createdAt");
        return query;
      }
    }, itemViewResource);
    if(className == null) {
      throw new RuntimeException("You need to specify a className for the ParseQueryAdapter");
    }
  }

  public ParseQueryAdapter(Context context, ParseQueryAdapter.QueryFactory<T> queryFactory) {
    this(context, queryFactory, (Integer)null);
  }

  public ParseQueryAdapter(Context context, ParseQueryAdapter.QueryFactory<T> queryFactory, int itemViewResource) {
    this(context, queryFactory, Integer.valueOf(itemViewResource));
  }

  private ParseQueryAdapter(Context context, ParseQueryAdapter.QueryFactory<T> queryFactory, Integer itemViewResource) {
    this.objectsPerPage = 25;
    this.paginationEnabled = true;
    this.imageViewSet = new WeakHashMap();
    this.dataSetObservers = new WeakHashMap();
    this.autoload = true;
    this.objects = new ArrayList();
    this.objectPages = new ArrayList();
    this.currentPage = 0;
    this.hasNextPage = true;
    this.onQueryLoadListeners = new ArrayList();
    this.context = context;
    this.queryFactory = queryFactory;
    this.itemResourceId = itemViewResource;
  }

  public Context getContext() {
    return this.context;
  }

  public T getItem(int index) {
    return index == this.getPaginationCellRow()?null:(T)this.objects.get(index);
  }

  public long getItemId(int position) {
    return (long)position;
  }

  public int getItemViewType(int position) {
    return position == this.getPaginationCellRow()?1:0;
  }

  public int getViewTypeCount() {
    return 2;
  }

  public void registerDataSetObserver(DataSetObserver observer) {
    super.registerDataSetObserver(observer);
    this.dataSetObservers.put(observer, null);
    if(this.autoload) {
      this.loadObjects();
    }

  }

  public void unregisterDataSetObserver(DataSetObserver observer) {
    super.unregisterDataSetObserver(observer);
    this.dataSetObservers.remove(observer);
  }

  public void clear() {
    this.objectPages.clear();
    this.syncObjectsWithPages();
    this.notifyDataSetChanged();
    this.currentPage = 0;
  }

  public void loadObjects() {
    this.loadObjects(0, true);
  }

  private void loadObjects(final int page, final boolean shouldClear) {
    final ParseQuery query = this.queryFactory.create();
    if(this.objectsPerPage > 0 && this.paginationEnabled) {
      this.setPageOnQuery(page, query);
    }

    this.notifyOnLoadingListeners();
    if(page >= this.objectPages.size()) {
      this.objectPages.add(page, new ArrayList());
    }

    final Capture firstCallBack = new Capture(Boolean.valueOf(true));
    query.findInBackground(new FindCallback<T>() {
      @SuppressLint({"ShowToast"})
      public void done(List<T> foundObjects, ParseException e) {
        // OfflineStore.isEnabled() || query.getCachePolicy() != ParseQuery.CachePolicy.CACHE_ONLY ||
        if(e == null || e.getCode() != 120) {
          if(e != null && (e.getCode() == 100 || e.getCode() != 120)) {
            ParseQueryAdapter.this.hasNextPage = true;
          } else if(foundObjects != null) {
            if(shouldClear && ((Boolean)firstCallBack.get()).booleanValue()) {
              ParseQueryAdapter.this.objectPages.clear();
              ParseQueryAdapter.this.objectPages.add(new ArrayList());
              ParseQueryAdapter.this.currentPage = page;
              firstCallBack.set(Boolean.valueOf(false));
            }

            if(page >= ParseQueryAdapter.this.currentPage) {
              ParseQueryAdapter.this.currentPage = page;
              ParseQueryAdapter.this.hasNextPage = foundObjects.size() > ParseQueryAdapter.this.objectsPerPage;
            }

            if(ParseQueryAdapter.this.paginationEnabled && foundObjects.size() > ParseQueryAdapter.this.objectsPerPage) {
              foundObjects.remove(ParseQueryAdapter.this.objectsPerPage);
            }

            List currentPage = (List)ParseQueryAdapter.this.objectPages.get(page);
            currentPage.clear();
            currentPage.addAll(foundObjects);
            ParseQueryAdapter.this.syncObjectsWithPages();
            ParseQueryAdapter.this.notifyDataSetChanged();
          }

          ParseQueryAdapter.this.notifyOnLoadedListeners(foundObjects, e);
        }
      }
    });
  }

  private void syncObjectsWithPages() {
    this.objects.clear();
    Iterator i$ = this.objectPages.iterator();

    while(i$.hasNext()) {
      List pageOfObjects = (List)i$.next();
      this.objects.addAll(pageOfObjects);
    }

  }

  public void loadNextPage() {
    this.loadObjects(this.currentPage + 1, false);
  }

  public int getCount() {
    int count = this.objects.size();
    if(this.shouldShowPaginationCell()) {
      ++count;
    }

    return count;
  }

  public View getItemView(T object, View v, ViewGroup parent) {
    if(v == null) {
      v = this.getDefaultView(this.context);
    }

    TextView textView;
    try {
      textView = (TextView)v.findViewById(TEXTVIEW_ID);
    } catch (ClassCastException var8) {
      throw new IllegalStateException("Your object views must have a TextView whose id attribute is \'android.R.id.text1\'", var8);
    }

    if(textView != null) {
      if(this.textKey == null) {
        textView.setText(object.getObjectId());
      } else if(object.get(this.textKey) != null) {
        textView.setText(object.get(this.textKey).toString());
      } else {
        textView.setText((CharSequence)null);
      }
    }

    if(this.imageKey != null) {
      ParseImageView imageView;
      try {
        imageView = (ParseImageView)v.findViewById(IMAGEVIEW_ID);
      } catch (ClassCastException var7) {
        throw new IllegalStateException("Your object views must have a ParseImageView whose id attribute is \'android.R.id.icon\'", var7);
      }

      if(imageView == null) {
        throw new IllegalStateException("Your object views must have a ParseImageView whose id attribute is \'android.R.id.icon\' if an imageKey is specified");
      }

      if(!this.imageViewSet.containsKey(imageView)) {
        this.imageViewSet.put(imageView, null);
      }

      imageView.setPlaceholder(this.placeholder);
      imageView.setParseFile((ParseFile)object.get(this.imageKey));
      imageView.loadInBackground(new GetDataCallback() {
        @Override
        public void done(byte[] data, ParseException e) {

        }
      });
    }

    return v;
  }

  public View getNextPageView(View v, ViewGroup parent) {
    if(v == null) {
      v = this.getDefaultView(this.context);
    }

    TextView textView = (TextView)v.findViewById(TEXTVIEW_ID);
    textView.setText("Load more...");
    return v;
  }

  public final View getView(int position, View convertView, ViewGroup parent) {
    if(this.getItemViewType(position) == 1) {
      View nextPageView = this.getNextPageView(convertView, parent);
      nextPageView.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
          ParseQueryAdapter.this.loadNextPage();
        }
      });
      return nextPageView;
    } else {
      return this.getItemView(this.getItem(position), convertView, parent);
    }
  }

  protected void setPageOnQuery(int page, ParseQuery<T> query) {
    query.setLimit(this.objectsPerPage + 1);
    query.setSkip(page * this.objectsPerPage);
  }

  public void setTextKey(String textKey) {
    this.textKey = textKey;
  }

  public void setImageKey(String imageKey) {
    this.imageKey = imageKey;
  }

  public void setObjectsPerPage(int objectsPerPage) {
    this.objectsPerPage = objectsPerPage;
  }

  public int getObjectsPerPage() {
    return this.objectsPerPage;
  }

  public void setPaginationEnabled(boolean paginationEnabled) {
    this.paginationEnabled = paginationEnabled;
  }

  public void setPlaceholder(Drawable placeholder) {
    if(this.placeholder != placeholder) {
      this.placeholder = placeholder;
      Iterator iter = this.imageViewSet.keySet().iterator();

      while(iter.hasNext()) {
        ParseImageView imageView = (ParseImageView)iter.next();
        if(imageView != null) {
          imageView.setPlaceholder(this.placeholder);
        }
      }

    }
  }

  public void setAutoload(boolean autoload) {
    if(this.autoload != autoload) {
      this.autoload = autoload;
      if(this.autoload && !this.dataSetObservers.isEmpty() && this.objects.isEmpty()) {
        this.loadObjects();
      }
    }
  }

  public void addOnQueryLoadListener(ParseQueryAdapter.OnQueryLoadListener<T> listener) {
    this.onQueryLoadListeners.add(listener);
  }

  public void removeOnQueryLoadListener(ParseQueryAdapter.OnQueryLoadListener<T> listener) {
    this.onQueryLoadListeners.remove(listener);
  }

  private View getDefaultView(Context context) {
    if(this.itemResourceId != null) {
      return LayoutInflater.from(context).inflate(this.itemResourceId, null);
//      return View.inflate(context, this.itemResourceId.intValue(), (ViewGroup)null);
    } else {
      LinearLayout view = new LinearLayout(context);
      view.setPadding(8, 4, 8, 4);
      ParseImageView imageView = new ParseImageView(context);
      imageView.setId(IMAGEVIEW_ID);
      imageView.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
      view.addView(imageView);
      TextView textView = new TextView(context);
      textView.setId(TEXTVIEW_ID);
      textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      textView.setPadding(8, 0, 0, 0);
      view.addView(textView);
      return view;
    }
  }

  private int getPaginationCellRow() {
    return this.objects.size();
  }

  private boolean shouldShowPaginationCell() {
    return this.paginationEnabled && this.objects.size() > 0 && this.hasNextPage;
  }

  private void notifyOnLoadingListeners() {
    Iterator i$ = this.onQueryLoadListeners.iterator();

    while(i$.hasNext()) {
      ParseQueryAdapter.OnQueryLoadListener listener = (ParseQueryAdapter.OnQueryLoadListener)i$.next();
      listener.onLoading();
    }

  }

  private void notifyOnLoadedListeners(List<T> objects, Exception e) {
    Iterator i$ = this.onQueryLoadListeners.iterator();

    while(i$.hasNext()) {
      ParseQueryAdapter.OnQueryLoadListener listener = (ParseQueryAdapter.OnQueryLoadListener)i$.next();
      listener.onLoaded(objects, e);
    }

  }

  public interface OnQueryLoadListener<T extends ParseObject> {
    void onLoading();

    void onLoaded(List<T> var1, Exception var2);
  }

  public interface QueryFactory<T extends ParseObject> {
    ParseQuery<T> create();
  }
}
