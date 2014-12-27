package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;
import com.ameron32.apps.projectbanditv3.adapter.ChatViewThousandPQAdapter;


public class ChatViewThousand extends
    AbsChatView {
  
  public static final String ID = ChatViewThousand.class.getSimpleName();
  private static final String TAG = ChatViewThousand.class.getSimpleName();
  private static final boolean TOAST = true;
  private static final boolean LOG = true;
  
  public static ChatViewThousand create(
      LayoutInflater inflater,
      int resourceId,
      ViewGroup container) {
    ChatViewThousand item = (ChatViewThousand) inflater.inflate(resourceId, container, false);
    item.create();
    return item;
  }
  
  public ChatViewThousand(
      Context context,
      AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  
  public ChatViewThousand(
      Context context,
      AttributeSet attrs) {
    super(context, attrs);
  }
  
  public ChatViewThousand(
      Context context) {
    super(context);
  }
  
  
  @Override protected AbsMessageAdapter createAdapter(
      Context context) {
    return new ChatViewThousandPQAdapter(context);
  }
  
  @Override protected void onCreateTitleView(
      TextView titleView) {
    titleView.setText("1000");
  }
  
  @Override public String getChatViewId() {
    return ID;
  }
  
  @Override protected void onCreate() {
    super.onCreate();
  }
  
}
