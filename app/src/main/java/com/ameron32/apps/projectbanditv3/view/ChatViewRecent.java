package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;
import com.ameron32.apps.projectbanditv3.adapter.ChatViewRecentPQAdapter;


public class ChatViewRecent extends AbsChatView {
  
  public static final String ID = ChatViewRecent.class.getSimpleName();

	public static ChatViewRecent create(LayoutInflater inflater,
			int resourceId, ViewGroup container) {
		ChatViewRecent item = (ChatViewRecent) inflater.inflate(resourceId,
				container, false);
		item.create();
		return item;
	}

	public ChatViewRecent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChatViewRecent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatViewRecent(Context context) {
		super(context);
	}

	@Override
	protected AbsMessageAdapter createAdapter(Context context) {
		return new ChatViewRecentPQAdapter(context);
	}

	@Override
	protected void onCreateTitleView(TextView titleView) {
		titleView.setText("Recent");
	}

	@Override
	public String getChatViewId() {
		return ID;
	}

}
