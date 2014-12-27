package com.ameron32.apps.projectbanditv3.adapter;



import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.projectbanditv3.view.AbsChatView;
import com.ameron32.apps.projectbanditv3.view.ChatViewOOC;
import com.ameron32.apps.projectbanditv3.view.ChatViewRecent;
import com.ameron32.apps.projectbanditv3.view.ChatViewStory;
import com.ameron32.apps.projectbanditv3.view.ChatViewThousand;
import com.ameron32.apps.projectbanditv3.R;


public class ChatViewPagerAdapter extends PagerAdapter {

  private static final String  TAG     = ChatViewPagerAdapter.class.getSimpleName();
  private static final boolean TOAST   = false;
  private static final boolean LOG     = true;

  private final Context        mContext;
  private final ViewPager      mViewPager;
  private final LayoutInflater mInflater;
  private final ChatViewSelector mChatViewSelector;
  
  int[]                        viewIds = new int[5];

  public ChatViewPagerAdapter(Context context, ViewPager viewPager) {
    super();
    this.mContext = context;
    this.mViewPager = viewPager;
    mInflater = LayoutInflater.from(context);
    mChatViewSelector = new ChatViewSelector(this);
  }

  @Override
  public int getCount() {
    return viewIds.length;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((View) object);
  }
  
  @Override
  public Object instantiateItem(ViewGroup collection, int position) {
    if (LOG) Log.d(TAG, "position: " + position);
    View item = collection.getChildAt(position);
    
    if (item == null) {
      item = mChatViewSelector.getViewAt(position);
      collection.addView(item);
    }

    return item;
  }
  
  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    // super.destroyItem(container, position, object);
    AbsChatView item = (AbsChatView) object;
    item.destroy();
  }

  public ViewPager getViewPager() {
    return mViewPager;
  }
  
  public LayoutInflater getInflater() {
    return mInflater;
  }
  
  private ChatViewSelector getChatSelector() {
    return mChatViewSelector;
  }
  
//  public int getViewPositionFromId(String id) {
//    int position = chatViewManager.getViewFromId(id);
//    return position;
//  }
  
  public static class ChatViewSelector {
    
    private static final String  TAG     = ChatViewSelector.class.getSimpleName();
    private static final boolean TOAST   = false;
    private static final boolean LOG     = true;

    // public SparseArray<WeakReference<ChatView>> storage = new
    // SparseArray<WeakReference<ChatView>>();
    private final LayoutInflater mInflater;
    private final ViewGroup mViewGroup;

    public ChatViewSelector(ChatViewPagerAdapter adapter) {
      this.mViewGroup = adapter.getViewPager();
      this.mInflater = adapter.getInflater();
    }

    public AbsChatView getViewAt(int position) {
      AbsChatView item = null;
      // try {
      // WeakReference<ChatView> reference = storage.get(position);
      // item = reference.get();
      // }
      // catch (NullPointerException e) {
      // e.printStackTrace();
      // }
      //
      // if (item == null) {
      item = generateNewChatView(position);
      // }
      return item;
    }
    
//    public int getViewFromId(String id) {
//      for (int i = 0; i < chatViews.length; i++) {
//        Class chatView = chatViews[i];
//        Log.i(TAG, "chatView: " + chatView.getSimpleName() + " | id: " + id);
//        if (chatView.getSimpleName().equalsIgnoreCase(id)) { return i; }
//      }
//      return 0;
//    }

//    private Class[] chatViews = { ChatViewRecent.class, ChatViewOOC.class, ChatViewStory.class, ChatViewStory.class, ChatViewThousand.class };
    private AbsChatView generateNewChatView(final int position) {
      AbsChatView item = null;
      switch (position) {
      case 0:
        item = ChatViewRecent.create(mInflater, R.layout.view_recent_chat,
            mViewGroup);
        break;
      case 1:
        item = ChatViewOOC.create(mInflater, R.layout.view_ooc_chat,
            mViewGroup);
        break;
      case 2:
        // left blank for expansion
        // fall-through
      case 3:
        item = ChatViewStory.create(mInflater, R.layout.view_story_chat,
            mViewGroup);
        break;
      case 4:
        item = ChatViewThousand.create(mInflater,
            R.layout.view_thousand_chat, mViewGroup);
        break;
      default:
        item = ChatViewRecent.create(mInflater, R.layout.view_recent_chat,
            mViewGroup);
      }
      // storage.put(position, new WeakReference<ChatView>(item));
      return item;
    }

  }
}
