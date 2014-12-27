package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;
import com.ameron32.apps.projectbanditv3.adapter.ChatViewStoryPQAdapter;


public class ChatViewStory extends AbsChatView {
  
  public static final String ID = ChatViewStory.class.getSimpleName();

	public static ChatViewStory create(LayoutInflater inflater, int resourceId,
			ViewGroup container) {
		ChatViewStory item = (ChatViewStory) inflater.inflate(resourceId,
				container, false);
		item.create();
		return item;
	}

	public ChatViewStory(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChatViewStory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatViewStory(Context context) {
		super(context);
	}

	@Override
	protected AbsMessageAdapter createAdapter(Context context) {
		return new ChatViewStoryPQAdapter(context);
	}

	@Override
	protected void onCreateTitleView(TextView titleView) {
		titleView.setText("Story");
	}

	@Override
	public String getChatViewId() {
		return ID;
	}

}
