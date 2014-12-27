package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;
import com.ameron32.apps.projectbanditv3.adapter.ChatViewOOCPQAdapter;

public class ChatViewOOC extends AbsChatView {
  
  public static final String ID = ChatViewOOC.class.getSimpleName();

	public static ChatViewOOC create(LayoutInflater inflater, int resourceId,
			ViewGroup container) {
		ChatViewOOC item = (ChatViewOOC) inflater.inflate(resourceId,
				container, false);
		item.create();
		return item;
	}

	public ChatViewOOC(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChatViewOOC(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatViewOOC(Context context) {
		super(context);
	}

	@Override
	protected AbsMessageAdapter createAdapter(Context context) {
		return new ChatViewOOCPQAdapter(context);
	}

	@Override
	protected void onCreateTitleView(TextView titleView) {
		titleView.setText("Out of Character");
	}

	@Override
	public String getChatViewId() {
		return ID;
	}

}
